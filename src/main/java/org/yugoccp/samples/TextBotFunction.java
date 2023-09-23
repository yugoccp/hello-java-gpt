package org.yugoccp.samples;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.SKBuilders;
import com.microsoft.semantickernel.orchestration.ContextVariables;
import com.microsoft.semantickernel.semanticfunctions.PromptTemplateConfig;
import com.microsoft.semantickernel.textcompletion.CompletionSKFunction;

import java.util.Objects;
import java.util.function.UnaryOperator;

public class TextBotFunction implements UnaryOperator<String> {
    private final CompletionSKFunction myFunction;
    private final Kernel myKernel;
    private String contextHistory = "";

    public TextBotFunction(OpenAIAsyncClient client, String text) {
        this.myKernel = SkUtils.getChatCompletionKernel(client, "gpt-3.5-turbo-16k");
        this.myFunction = buildFunction(myKernel, text);
    }

    private CompletionSKFunction buildFunction(Kernel kernel, String text) {

        var semanticFunctionInline = """
                TextBot can have a conversation with you only about the text below.
                TextBot summarizes answers in less than 100 words.
                TextBot can only give answers of something related to the text below or say 'I don't know' if it does not have an answer.
                
                
                --- TEXT START ---
                
                %s
                
                --- TEXT END ---           
                    
                {{$history}}
                User: {{$input}}
                TextBot:""".formatted(text);

        var promptConfig = new PromptTemplateConfig(
                new PromptTemplateConfig.CompletionConfigBuilder()
                        .maxTokens(10000)
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
