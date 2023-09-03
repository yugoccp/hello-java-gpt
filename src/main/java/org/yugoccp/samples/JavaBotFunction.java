package org.yugoccp.samples;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.SKBuilders;
import com.microsoft.semantickernel.orchestration.ContextVariables;
import com.microsoft.semantickernel.semanticfunctions.PromptTemplateConfig;
import com.microsoft.semantickernel.textcompletion.CompletionSKFunction;

import java.util.Objects;
import java.util.function.UnaryOperator;

public class JavaBotFunction implements UnaryOperator<String> {
    private final CompletionSKFunction myFunction;
    private final Kernel myKernel;
    private String contextHistory = "";

    public JavaBotFunction(OpenAIAsyncClient client) {
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
                JavaBot is a Java technology expert and can answer any questions regarding to Java. 
                JavaBot can have a conversation with you about any topic related to Java.
                JavaBot always give a Java example for every answer it gives to you.
                JavaBot can give concise answers or say 'Boo! That's not Java' if it does not have relation with Java.
                    
                {{$history}}
                User: {{$input}}
                JavaBot:""";

        var promptConfig = new PromptTemplateConfig(
                new PromptTemplateConfig.CompletionConfigBuilder()
                        .maxTokens(5000)
                        .temperature(0.2)
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

        // Cria contexto para enviar memória da function
        var newContext = ContextVariables.builder()
                .withVariable("history", contextHistory)
                .withVariable("input", inputText)
                .build();

        var skContext = myKernel.runAsync(newContext, myFunction);
        var result = Objects.requireNonNull(skContext.block()).getResult();

        // Atualiza histórico com o resultado da resposta
        contextHistory = contextHistory + String.format("%nUser: %s%nJavaBot: %s%n", inputText, result);

        return result;
    }

}
