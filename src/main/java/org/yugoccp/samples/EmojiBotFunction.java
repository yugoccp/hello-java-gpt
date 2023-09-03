package org.yugoccp.samples;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.SKBuilders;
import com.microsoft.semantickernel.semanticfunctions.PromptTemplateConfig;
import com.microsoft.semantickernel.textcompletion.CompletionSKFunction;

import java.util.Objects;
import java.util.function.UnaryOperator;

public class EmojiBotFunction implements UnaryOperator<String> {
    private final CompletionSKFunction myFunction;

    public EmojiBotFunction(OpenAIAsyncClient client) {
        var myKernel = getKernel(client);
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
                EmojiBot is a movie expert and knows about the story and context of every existing movie around the world.
                EmojiBot recognize any movie title and do the best to build a detailed movie plot only using emojis.
                
                User: {{$input}}
                EmojiBot:""";

        var promptConfig = new PromptTemplateConfig(
                new PromptTemplateConfig.CompletionConfigBuilder()
                        .maxTokens(1000)
                        .temperature(0.8)
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
        var skContext = myFunction.invokeAsync(inputText);
        return Objects.requireNonNull(skContext.block()).getResult();
    }

}
