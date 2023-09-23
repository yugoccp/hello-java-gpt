package org.yugoccp.samples;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TextBotGPT {

    public static void main(String[] args) throws IOException {

        // Build client with config file settings
        var configFile = new File(args[0]);
        var client = SkUtils.getClient(List.of(configFile));

        var filePathStr = args[1];
        var filePath = Path.of(filePathStr);
        var filename = filePath.getFileName();
        var text = Files.readString(filePath);

        // Build function using OpenAI client
        var sampleFunction = new TextBotFunction(client, text);

        // Use function to interact with user inputs
        var reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\n> Ask me anything about " + filename + " (type 'exit' to exit):\n");
        var inputText = reader.readLine();

        while (!inputText.equals("exit")) {
            var result = sampleFunction.apply(inputText);

            System.out.println(result);

            System.out.println("\n> You (type 'exit' to exit): ");

            inputText = reader.readLine();
        }
    }
}
