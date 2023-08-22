package org.yugoccp.samples;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.SKBuilders;
import com.microsoft.semantickernel.orchestration.SKContext;
import com.microsoft.semantickernel.semanticfunctions.PromptTemplateConfig;
import com.microsoft.semantickernel.textcompletion.CompletionSKFunction;
import reactor.core.publisher.Mono;

public class SampleFunction {
    private final CompletionSKFunction myFunction;

    public SampleFunction(OpenAIAsyncClient client) {
        var kernel = getKernel(client);
        this.myFunction = buildFunction(kernel);
    }

    private Kernel getKernel(OpenAIAsyncClient client) {
        return SKBuilders.kernel()
                .withDefaultAIService(SKBuilders.chatCompletion()
                        .setModelId("gpt-4")
                        .withOpenAIClient(client)
                        .build())
                .build();
    }

    private CompletionSKFunction buildFunction(Kernel kernel) {

        String semanticFunctionInline = """
            You're the best cover of Michael Jackson.
            Every answer you give is completed with a reference of some Michael Jackson famous quote. 
    
            {{$input}}
            """;

        var promptConfig = new PromptTemplateConfig(
                new PromptTemplateConfig.CompletionConfigBuilder()
                        .maxTokens(100)
                        .temperature(0.3)
                        .topP(1)
                        .build());

        return SKBuilders
                .completionFunctions()
                .withKernel(kernel)
                .setPromptTemplateConfig(promptConfig)
                .setPromptTemplate(semanticFunctionInline)
                .build();
    }

    public Mono<SKContext> run(String inputText) {
        return myFunction.invokeAsync(inputText);
    }
}
