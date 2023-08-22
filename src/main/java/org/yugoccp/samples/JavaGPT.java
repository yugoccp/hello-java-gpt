package org.yugoccp.samples;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.microsoft.semantickernel.connectors.ai.openai.util.OpenAIClientProvider;
import com.microsoft.semantickernel.exceptions.ConfigurationException;
import com.microsoft.semantickernel.orchestration.SKContext;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

public class JavaGPT {
    public static OpenAIAsyncClient getClient(List<File> configFileList) {
        try {
            return OpenAIClientProvider.getWithAdditional(configFileList);
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        var configFile = new File(args[0]);

        var client = getClient(List.of(configFile));
        var sampleFunction = new SampleFunction(client);
        var reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Escreva sua pergunta (digite 'exit' para sair):");
        var inputText = reader.readLine();

        while (!inputText.equals("exit")) {
            Mono<SKContext> result = sampleFunction.run(inputText);
            System.out.println(Objects.requireNonNull(result.block()).getResult());

            System.out.println("Resposta (digite 'exit' para sair): ");
            inputText = reader.readLine();
        }
    }
}
