package org.yugoccp.samples;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.microsoft.semantickernel.connectors.ai.openai.util.OpenAIClientProvider;
import com.microsoft.semantickernel.exceptions.ConfigurationException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Function;

public class JavaGPT {
    private static OpenAIAsyncClient getClient(List<File> configFileList) {
        try {
            return OpenAIClientProvider.getWithAdditional(configFileList);
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private static Function<String, String> getFunction(String funcType, OpenAIAsyncClient client) {
        return switch (funcType) {
            case "chat" -> new ChatFunction(client);
            case "emoji" -> new EmojiFunction(client);
            case "ascii" -> new AsciiFunction(client);
            default -> ((s) -> "");
        };
    }

    public static void main(String[] args) throws IOException {
        var configFile = new File(args[0]);
        var funcType = args[1];

        var client = getClient(List.of(configFile));
        var sampleFunction = getFunction(funcType, client);
        var reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Escreva sua pergunta (digite 'exit' para sair):");
        var inputText = reader.readLine();

        while (!inputText.equals("exit")) {
            var result = sampleFunction.apply(inputText);
            System.out.println(result);

            System.out.println("Resposta (digite 'exit' para sair): ");
            inputText = reader.readLine();
        }
    }
}
