# tcdb-cli

TCDB CLI is a Command Line to help you generate your Celebrity Statement with the help of AI!

## Requirements
- Java 17
- Maven 3
- OpenAI account

## Usage

1. Create a copy of `config.properties.example` and rename to `config.properties`
2. Fill the `config.properties` file with the OpenAI API token and organization ID
3. Run the following command:
```
java -cp './bin/tcdb-cli-0.0.1.jar' org.yugoccp.samples.TcdbCli <INPUT_TEXT> -c ./config.properties
```

If you want to read the input from a text file, run the following command:
```
java -cp "./bin/tcdb-cli-0.0.1.jar" org.yugoccp.samples.TcdbCli -c config.properties "$(< <TEXT_FILE_PATH>)"
```

## Build
To build the application, run the following command:
```
mvn clean package
```