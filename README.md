# Hello Java GPT

Sample project to start using the power of Generative AI with Java!

## Requirements
- Java 17
- Maven 3
- OpenAI account

## Usage

1. Create a copy of `config.properties.example` and rename to `config.properties`
2. Fill the `config.properties` file with the OpenAI API token and organization ID
3. Build the application from the source code:
```bash
./mvnw clean package
```
4. Run the EmojiBot with
```bash
./mvnw exec:java -Dexec.mainClass="org.yugoccp.samples.EmojiBotGPT" -Dexec.args="./config.properties"
```

5. Or run the JavaBot with
```bash
./mvnw exec:java -Dexec.mainClass="org.yugoccp.samples.JavaBotGPT" -Dexec.args="./config.properties"
```

## Reference
Follow Soham Dasgupta (sohamda) and his awesome SK Basic repo for more!
- [AI tooling for Java developers with SK](https://devblogs.microsoft.com/semantic-kernel/ai-tooling-for-java-developers-with-sk/)
- [SemanticKernel-Basics](https://github.com/sohamda/SemanticKernel-Basics)
