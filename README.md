# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

## Phase 2 Diagram

[![Phase 2 Diagram](https://github.com/mailiap/chess/assets/103383293/27d0d7d4-d4af-419d-9e64-529ae08d5539)](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgCwKYGcVwDYEskDswBQADsFGFiFifjAMpJQBuDxp5l1YdDjFqrZClWA0AIsDDAAgiBCoUA9sLETgAI2AokBAgHMoEAK5EYAYihIAJvAxJSMYESLYQErBFxmA7gixhtiPKYODQAtAB83EwMAFwwANqiAKIAMkkAKkkAujAA9OKSAEKa2vTRsBFRvHIocSC2pAXqJQAUAJQEZdWoMJVNMjW1MJZItv5SGBgAqlpQTe0E-bLyvZFNGlpxyWmZMABUMABiAEoA8gCyMDMMTZ08fCirMEuDcSNjSBMYAOLAALZIeYdF4rPqqDZILapDJJfZHM6XX4A25dB5PEFoN6jJDjSZSQxgBBAxaqAagtbgkpQnawg4nC4wfGE24Yx6hSqompxXCGSZ3Jho9mRLqxGA8vkiiqVQJoYJ4MBxeIAJgADCqsgQ8JYdPojCZTNARLokDBjkhdFgUP5YKYfH4kBqZehsPKnpLFQAFU60dI5XKGWYAHVwAG8AwxcP8kAAaGAkNBeaCWWNIP7ALAYAC+-OYFWF9y5MAsFqtDGuUBa4bmqg6nJ6YMkZMxMBAyBAAGskgAPS3kXC6cuV2aRgHA0nLNDoymbOjQuDpGBVkcm+mXcss8eDXocguoGJVmDQMW8jA5wUUxsToYgCwSJCDqtNMeXrcN5ozgCSADlaEljgulyjWN4xQRMoGTGBU3TDAYG-dJTiuWYUV3NkL2kK86lvcYCQQdIIHbPAhwjKNn3Q180IhOJv1-f9F2HICHBwvCCM8OCEKZIlVBJF9QR3AVC2AJj8LwM8am3fNyjiQTCWYkTJXE+BkFlF18EVVV1RgEMYAAIkAgFtJibTtNjbTpNw4TcAMoyYGzLUdQMYwzAAT1GDAIC8GAUggC1PFMNQMFAdsCCdOUwgk3NPW9X08i0NB3FwYMQ10+j9MM4ydJAsDLCs7Ts3kvjuiGNyfIfJCa1E+s0KbIZjTActCic8tlyIqBl1I6qp0kSjZzSec6OIgF4QZdcuNZBS6yGA9LFUQ9YHFU8Js6sj5EwuxsJkiyWra7jlsnN9uuov8AJSmM400UCkxTNMM1gr94MQm5Rs3XjwoeKShJYiq2QK0UzNk3AcwYBSQpUhUEnUnItOSgakByky-osnKbM1XBtT0Bz9X0JA8E87yjC4W1fH8YKlOdEIuB+qBFW2GE-VilB4uDMzoCwAAvNwPDiAAeBGWPCQG8yqN6YGK-HB15vBaxQpbqqxD4OP+loJdwdqryW7qad2OkEUZHCNx4vaCuF+avvGnhRRN-LpVJ0KweVNUNTs9G9TMfzDBNFJexgJEekJ+0SaCUG3XNqmEm+DI-V0KMmZwln2fITmYB5j68H5-LXsLbArVOAAzH2UCVlOVdNt9ZZgWr6qchXNuV1XyOeadIR6pI+r0lcdY4-Xdu+jO9361qo1m48+RLqqMPLnEfZaKOAQ-UQ6-JBuuqpZu+u1hkfa7jqhSFwsZ56I9Lelnf3XLqMFCtyIQfJtSHc0nT95QAyEiS-e5+fgBGJUAGYABYTLtP4JqUY4Y6X8oFYBqUjImX3l+KMaVMwwCyLZVG9kXamCIIYKAzgTRwCwiaH23giYBBtkHSmkUfSR2jpGWOUA2Yc1wNzZW4REraVgSAhBAtg78T7jeNaSAp7K1jOw0co8l7dziBXWYhcNosQXntCiK9fy9WOjDIalxO5PQNj3Xefc25iNZDEXASAvBTzbsIqMcDRFjX2ivQ6tFzFnwBFYk0bFvZRi3mrE+KFJFRjniXchTikD+MvopQON9wZ3yhm-UQn8f6-2Rk7XUjkMFYFwO2GAAApCAaT3GDSkE4FwDCiH+2vq6QJ8QPRTGirkfeMdCRxwYUwouLDQzaWcMAFyUA4AQDclAZ+2kADqAAJD8mRciFBSFIOAABpdKbC-GxLiF-P+yN066KGAAKxybgQRRdgIBS6T0vpFjZ7zwMc9ZsUiYYNWrixGR5k5E7W3oomcyiW6qIHoNVcutmRaO7mbHhk0TpDyPkCmW49ao+wanPaeiz5GoXEd1d5a91F5MBP87eRs96D2mpIUFJ4LnaLiMYPFSAPSHIYMc6AcKzmxkcR0o5vToAIvVivKYHpRBSF2JvLii1vGSS4TvMpqlInqhRpYIAA)
