package org.yugoccp.samples;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.SKBuilders;
import com.microsoft.semantickernel.orchestration.ContextVariables;
import com.microsoft.semantickernel.semanticfunctions.PromptTemplateConfig;
import com.microsoft.semantickernel.textcompletion.CompletionSKFunction;

import java.util.Objects;
import java.util.function.Function;

public class EmojiFunction implements Function<String, String>{
    private final CompletionSKFunction myFunction;
    private final Kernel myKernel;
    private String contextHistory = "";

    public EmojiFunction(OpenAIAsyncClient client) {
        this.myKernel = getKernel(client);
        this.myFunction = buildFunction(myKernel);
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
                ChatBot can have a conversation with you about any topic.
                It can give concise answers but only using emoji.
                    
                {{$history}}
                User: {{$input}}
                ChatBot:""";

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

    public String apply(String inputText) {

        var newContext = ContextVariables.builder()
                .withVariable("history", contextHistory)
                .withVariable("input", inputText)
                .build();

        var skContext = myKernel.runAsync(newContext, myFunction);
        var result = Objects.requireNonNull(skContext.block()).getResult();

        contextHistory =  contextHistory + String.format("\\nUser: %s\\nChatBot: %s\\n", inputText, result);

        return result;
    }

}
