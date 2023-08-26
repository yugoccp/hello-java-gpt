package org.yugoccp.samples;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.microsoft.semantickernel.connectors.ai.openai.util.OpenAIClientProvider;
import com.microsoft.semantickernel.exceptions.ConfigurationException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class JavaBotGPT {

    private static OpenAIAsyncClient getClient(List<File> configFileList) {
        try {
            return OpenAIClientProvider.getWithAdditional(configFileList);
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {

        // Busca o arquivo com as configurações de acesso para o OpenAI para criar o cliente de acesso
        var configFile = new File(args[0]);
        var client = getClient(List.of(configFile));

        // Contrói a function de Emoji utilizando o client do OpenAI
        var sampleFunction = new JavaBotFunction(client);

        var reader = new BufferedReader(new InputStreamReader(System.in));

        // Inicia a aplicação utilizando a function e imprime o resultado
        System.out.println("\n> Pergunte qualquer coisa sobre Java (digite 'sair' para sair):\n");
        var inputText = reader.readLine();

        while (!inputText.equals("sair")) {
            var result = sampleFunction.apply(inputText);
            System.out.println(result);

            System.out.println("\n> Pergunte qualquer coisa sobre Java (digite 'sair' para sair):\n");
            inputText = reader.readLine();
        }
    }
}
