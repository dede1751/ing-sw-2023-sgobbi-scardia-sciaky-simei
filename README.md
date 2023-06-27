![Title][title-image]

## <div align="center"> Andrea Sgobbi, Roberto Scardia, Jonatan Sciaky, Luca Simei </div>

This is the final project for the Software Engineering course @ Politecnico di Milano.
Objective of this project is to build a distributed version of the game [MyShelfie][game-link] by 
**Cranio Creations** using a Model-View-Controller architecture.

### Implemented Features
| Feature                  | Status |
|:-------------------------|:------:|
| Simplified rules         |   ✅    |
| Complete rules           |   ✅    |
| RMI                      |   ✅    |
| Socket                   |   ✅    |
| GUI                      |   ✅    |
| TUI                      |   ✅    |
| Multiple games           |   ✅    |
| Server Persistence       |   ✅    |
| Chat                     |   ✅    |
| Disconneciton Resilience |   ❌    |

### Testing Coverage
[PLACEHOLDER][tests-image]

### Dependencies
The game runs on **JavaSE19** downloadable [here][java-link]. To build the project also download [Maven][maven-link].

### Installation
Either download the client/server *.jar* in the deliverables folder, or build the app from source:

```bash
git clone https://github.com/dede1751/ing-sw-2023-sgobbi-scardia-sciaky-simei.git
mvn package
```

### Playing the game
To be able to run the game, one player must host an instance of the MyShelfie Server:

```bash
java -jar softeng-gc05-server.jar
```

Other players wishing to connect to the server must simply run the Client App:
```bash
java -jar softeng-gc05-client.jar
```

They will then be prompted for View type (GUI or TUI) and the type of connection to use (RMI or Socket). Clients must
know the IP address of the server to be able to connect to it.

[title-image]:images/title.jpeg
[tests-image]:images/tests.jpeg

[game-link]:https://www.craniocreations.it/prodotto/my-shelfie
[java-link]:https://www.oracle.com/java/technologies/javase/jdk19-archive-downloads.html
[maven-link]:https://maven.apache.org/download.cgi