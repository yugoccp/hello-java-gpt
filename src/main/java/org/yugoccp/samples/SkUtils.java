package org.yugoccp.samples;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.SKBuilders;
import com.microsoft.semantickernel.connectors.ai.openai.util.OpenAIClientProvider;
import com.microsoft.semantickernel.exceptions.ConfigurationException;

import java.io.File;
import java.util.List;

public class SkUtils {

    public static Kernel getChatCompletionKernel(OpenAIAsyncClient client, String model) {
        return SKBuilders.kernel()
                .withDefaultAIService(SKBuilders.chatCompletion()
                        .setModelId(model)
                        .withOpenAIClient(client)
                        .build())
                .build();
    }

    public static OpenAIAsyncClient getClient(List<File> configFileList) {
        try {
            return OpenAIClientProvider.getWithAdditional(configFileList);
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
