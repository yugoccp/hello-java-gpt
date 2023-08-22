# Hello Java GPT

Use the power of Generative AI with Java!

## Requirements
- Java 17
- Maven 3
- OpenAI account

## Usage

1. Create a copy of `config.properties.example` and rename to `config.properties`
2. Fill the `config.properties` file with the OpenAI API token and organization ID
3. Build the application from the source code:
```
mvn clean package
```
4. Run the following command:
```
java -cp './target/hello-java-gpt-cli-0.0.1.jar' org.yugoccp.samples.JavaGPT ./config.properties
```
