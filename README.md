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

<!--## Phase 2 Diagram

[![Phase2-Diagram](https://github.com/mailiap/chess/assets/103383293/3dfba1eb-34eb-4104-bd5e-65f6a5116541)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0shttps://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgCwKYGcVwDYEskDswBQADsFGFiFifjAMpJQBuDxp5l1YdDjFqrZClWA0AIsDDAAgiBCoUA9sLETgAI2AokBAgHMoEAK5EYAYihIAJvAxJSMYESLYQErBFxmA7gixhtiPKYODQAtAB83EwMAFwwANqiAKIAMkkAKkkAujAA9OKSAEKa2vTRsBFRvHIocSC2pAXqJQAUAJQEZdWoMJVNMjW1MJZItv5SGBgAqlpQTe0E-bLyvZFNGloxyWmZMABUMABiAEoA8gCyMDMMTZ08fCirMEuDcSNjSBMYAOLAALZIeYdF4rPqqDZILapDJJfZHM6XX4A25dB5PEFoN6jJDjSZSQxgBBAxaqAagtbgkpQnawg4nC4wfGE24Yx6hSqompxXCGSZ3Jho9mRLqxGA8vkiiqVQJoYJ4MBxeIAJgADCqsgQ8JYdPojCZTNARLokDBjkhdFgUP5YKYfH4kBqZehsPKnpLFQAFU60dI5XKGWYAHVwAG8AwxcP8kAAaGAkNBeaCWWNIP7ALAYAC+-OYFWF9y5MAsFqtDGuUBa4bmqg6nJ6YMkZMxMBAyBAAGskgAPS3kXC6cuV2aRgHA0nLNDoymbWjQuDpGBVkcm+mXcss8eDXocguoGJVmDQMW8jA5wUUxsToYgCwSJCDqtNMeXrcN5qbACSADlZ8cF0uo1jeMUETKBkxgVN0wwGBv3SU4rlmFFdzZC9pCvOpb3GAkEHSCB2zwIcIyjZ80NfVCIRib9f3-YdAIcbDcPwzxYPgpkiVUEkX1BHcBULYAGLwvAzxqbd83KOJ+MJRihMlUT4GQWUXXwRVVXVGAQxgAAiACAU0mJNM02NNMknDBNwPSDJgbMtR1AxjDMABPUYMAgLwYBSCALU8Uw1AwUB2wIJ05TCMTc09b1fTyLQ0HcXBgxDbTaN0-TDK04DQMsCzNOzWSeO6IYXK8h9EJrYT61QpshmNMBy0KBzy2XQioGXEjKqnSQKNnNJ50XJKVwRBCbg41k5LrIYD0sVRD1gcVTzG9rSPkDC7CwqSzKalrOMWyc3woqikj-XqiIBIDNBApMUzTDMYK-ODBurSQtraoUqgeCSBKYsq2Ty0V6LWz7culBTnRCBUElUnINMS46kCyoyTOk8yUqszVcG1PQ7P1fQkDwdzPKMLhbV8fxAuB4KuB+qBFW2GE-WilBYuDEzoCwAAvNwPDiAAeBGzPCHMGDdZC4kKgnB15pja2QhbKqxD42MRloJbwVqrwWiiad2OkBrYjcuJ2vK3uPCVpZe91jbmnhBZeoKlLB5U1Q1GyMb1MxfMME0Ul7GAkR6In7VJoI7aF8T4m+DI-V0KMmewln2fITmYB5j68H53LQqN7ArVOAAzX2UCVlPcCl3jyueTclpgar84WEbdqpLqkh67WGV9vXtu+jPCyjgEFHms2rapnv+EByJbdBmIHbUqHh5QPSEgS4eP1EeeAEYlQAZgAFiMu1-AaqM4a03z-IP5KDKM4evyjFLMxgLJrLR2zXdMIhDCgZwTTgTCTV97xiYCGTYOlNwo+kjtHSMscoBsw5rgbmytcDhHippK+h9b4CzzK9QsN4VpIF9oXf6eBYyoNHF9GW6Eq44nFkXVWZFy4dSpHQOcNEYbwgZLrYaFcDZdz3EdZqUYyFvllrgJAXh8E6RjFXKM19SF13IlSfah0JHEOkVGG6d026cP1p3LBvCl6iEESAqRAJl4YLkuPeUKlHbqS0votem8t4o2di7eyr8sC4HbDAAAUhAdxPs1H+xJhYkKVRRTxA9FMSKuRh6BmZtA+OsV4FFyQaGTSzhgBOSgHACALkoAWQAOoAAkPyZFyIUFIUg4AAGlUooKjMvex28Ubp10UMAAVr43A+CEFAT8pk7JuSVEmNECXfK5DXiULAL7Oqy8Wj6NoeSeh75IRMO6guFuiIozt2eobbuajJqPX7kY4ewwOKj3kkHCe4NHao21EAAAI49nyMG6ElQQA)-->

## UPDATED Phase 2 Diagram
[![image](https://github.com/mailiap/chess/assets/103383293/fb7da8a0-3532-4eb2-bf60-0ad37917ee88)](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgCwKYGcVwDYEskDswBQADsFGFiFifjAMpJQBuDxp5l1YdDjFqrZClWA0AIsDDAAgiBCoUA9sLETgAI2AokBAgHMoEAK5EYAYihIAJvAxJSMYESLYQErBFxmA7gixhtiPKYODQAtAB83EwMAFwwANqiAKIAMkkAKkkAujAA9OKSAEKa2vTRsBFRvHIocSC2pAXqJQAUAJQEZdWoMJVNMjW1MJZItv5SGBgAqlpQTe0E-bLyvZFNGlpxyWmZMABUMABiAEoA8gCyMDMMTZ08fCirMEuDcSNjSBMYAOLAALZIeYdF4rPqqDZILapDJJfZHM6XX4A25dB5PEFoN6jJDjSZSQxgBBAxaqAagtbgkpQnawg4nC4wfGE24Yx6hSqompxXCGSZ3Jho9mRLqxGA8vkiiqVQJoYJ4MBxeIAJgADCqsgQ8JYdPojCZTNARLokDBjkhdFgUP5YKYfH4kBqZehsPKnpLFQAFU60dI5XKGWYAHVwAG8AwxcP8kAAaGAkNBeaCWWNIP7ALAYAC+-OYFWF9y5MAsFqtDGuUBa4bmqg6nJ6YMkZMxMBAyBAAGskgAPS3kXC6cuV2aRgHA0nLNDoymbOjQuDpGBVkcm+mXcss8eDXocguoGJVmDQMW8jA5wUUxsToYgCwSJCDqtNMeXrcN5ozgCSADlaEljgulyjWN4xQRMoGTGBU3TDAYG-dJTiuWYUV3NkL2kK86lvcYCQQdIIHbPAhwjKNn3Q180IhOJv1-f9F2HICHBwvCCM8OCEKZIlVBJF9QR3AVC2AJj8LwM8am3fNyjiQTCWYkTJXE+BkFlF18EVVV1RgEMYAAIkAgFtJibTtNjbTpNw4TcAMoyYGzLUdQMYwzAAT1GDAIC8GAUggC1PFMNQMFAdsCCdOUwgk3NPW9X08i0NB3FwYMQ10+j9MM4ydJAsDLCs7Ts3kvjuiGNyfIfJCa1E+s0KbIZjTActCic8tlyIqBl1I6qp0kSjZzSec6OIgF4QZdcuNZBS6yGA9LFUQ9YHFU8Js6sj5EwuxsJkiyWra7jlsnN9uuov8AJSmM400UCkxTNMM1gr94MQm5Rs3XjwoeKShJYiq2QK0UzNk3AcwYBSQpUhUEnUnItOSgakByky-osnKbM1XBtT0Bz9X0JA8E87yjC4W1fH8YKlOdEIuB+qBFW2GE-VilB4uDMzoCwAAvNwPDiAAeBGWPCQG8yqN6YGK-HB15vBaxQpbqqxD4OP+loJdwdqryW7qad2OkEUZHCNx4vaCuF+avvGnhRRN-LpVJ0KweVNUNTs9G9TMfzDBNFJexgJEekJ+0SaCUG3XNqmEm+DI-V0KMmZwln2fITmYB5j68H5-LXsLbArVOAAzH2UCVlOVdNt9ZZgWr6qchXNuV1XyOeadIR6pI+r0lcdY4-Xdu+jO9361qo1m48JWl0uMJbLD71mOAIDciso4BD9RFjNvgIClyoBnueOh2jr9qpW6aIXBekCXleTtu+6fa7vejcLE-hi4xax9ecucR9loT6XuvyQbrqD9-L1Bc2sGTXyegbHuQt75RkeEeS2o9KZxBPgoK2kQQbkzUg7TSOlkEGQSElL+og8EAEYlQAGYAAsJk7T+CalGOGOl-KBToalIyJkT5fijGlTMMAsi2VRvZF2pgiCGCgM4E0cBJ7e0Hn7Ym6DXSIISF6H0kdo6RljlANmHNcDc2VuERK2kOH0O4QLYO-E+43jWkgD+ytYxGNHCXKq48K6zELhtFiP89oUQAXOY6MMhqXE7uA7uZtzGTROo4v+3cYi4CQF4D+q9y5Rk4Q4sa+9Pw-iOv3ZcdjkmDzYtI5EwTb69xqlGJeJdFGENMUKRSgcMHgywVDQhJDyEUORk7XUjlhFYFwO2GAAApCAvTCkmikE4Fw2jvBEwCDbIOij4geimNFXIJ8Y6Ejjto3RRd9Ghm0s4YAG8t7QDwdpAA6gACQ-JkXIhQUhSDgAAaXSoY8pRC4ikMocjdOUC+4ACthm4BsUXNehyGDHKgLkxeogpZhJls4nELCkANWrixNx5kPG7zVukpugCW5+IHoNVcutmTFOxXfPubch7wLhS-Fab8wA+wakvT+bzPGoSid1PFfUQGIijDfclpSkGD2mpIalJ5ImsiQTiCFUhGDQXUBmPwTlWXQtBUc2e0B2Xqx8UAuExKwFiqxa+ClQwDkb0fmKuBErn5ONfsYUVSAPTr3BZq+ebzz4MAAPpYAgua1129jW-3WAfKYHpRBSF2Ia4AQbDZCqSYNUVMbn5VJFU-EOwM5kNPtuqFGlggA)
