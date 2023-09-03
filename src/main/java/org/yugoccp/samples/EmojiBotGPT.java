package org.yugoccp.samples;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.microsoft.semantickernel.connectors.ai.openai.util.OpenAIClientProvider;
import com.microsoft.semantickernel.exceptions.ConfigurationException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class EmojiBotGPT {

    private static OpenAIAsyncClient getClient(List<File> configFileList) {
        try {
            return OpenAIClientProvider.getWithAdditional(configFileList);
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {

        // Build client with config file settings
        var configFile = new File(args[0]);
        var client = getClient(List.of(configFile));

        // Build function using OpenAI client
        var sampleFunction = new EmojiBotFunction(client);

        // Use function to interact with user inputs
        var reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\n> Write a movie name (type 'exit' to exit):\n");
        var inputText = reader.readLine();

        while (!inputText.equals("exit")) {
            var result = sampleFunction.apply(inputText);

            System.out.println(result);

            System.out.println("\n> Write a movie name (type 'exit' to exit):\n");

            inputText = reader.readLine();
        }
    }
}
