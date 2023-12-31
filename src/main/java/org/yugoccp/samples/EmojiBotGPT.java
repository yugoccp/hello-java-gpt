package org.yugoccp.samples;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class EmojiBotGPT {

    public static void main(String[] args) throws IOException {

        // Build client with config file settings
        var configFile = new File(args[0]);
        var client = SkUtils.getClient(List.of(configFile));

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
