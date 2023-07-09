![Title][title-image]

## <div align="center"> Andrea Sgobbi, Roberto Scardia, Jonatan Sciaky, Luca Simei </div>

This is the final project for the Software Engineering course @ Politecnico di Milano.
Objective of this project is to build a distributed version of the game [MyShelfie][game-link] by 
**Cranio Creations** using a Model-View-Controller architecture. This project has been awarded the
maximum grade of **30L**.

### Implemented Features
- [x] Simplified rules
- [x] Complete rules
- [x] RMI
- [x] Socket
- [x] GUI
- [x] TUI
- [x] Multiple games
- [x] Server Persistence
- [x] Chat
- [ ] Disconnection Resilience

### Testing Coverage
![Full project coverage][tests-image]

### Dependencies
The game runs on **JavaSE19** downloadable [here][java-link]. To build the project also download [Maven][maven-link].
All dependencies are bundled in the provided JARs.

### Installation
Either download the client/server *.jar* in the deliverables folder, or build the app from source:

```bash
git clone https://github.com/dede1751/ing-sw-2023-sgobbi-scardia-sciaky-simei.git
mvn package
```

### Playing the game
To be able to run the game, one player must host an instance of the MyShelfie Server.
They will be prompted for the ip address of the interface they want to expose for communication with the clients.

```bash
java -jar softeng-gc05-server.jar
```

Other players wishing to connect to the server must simply run the Client App:
```bash
java -jar softeng-gc05-client.jar
```

They will then be prompted for View type (GUI or TUI) and the type of connection to use (RMI or Socket). Clients must
also know the IP address of the server to be able to connect to it.

### TUI
![TUI][tui-image]

### GUI
![GUI][gui-image]

### Copyright Notice

> My Shelfie è un gioco da tavolo sviluppato ed edito da Cranio Creations Srl. I contenuti grafici di questo progetto riconducibili
al prodotto editoriale da tavolo sono utilizzati previa approvazione di Cranio Creations Srl a solo scopo didattico.
È vietata la distribuzione, la copia o la riproduzione dei contenuti e immagini in qualsiasi forma al di fuori del progetto, così
come la redistribuzione e la pubblicazione dei contenuti e immagini a fini diversi da quello sopracitato.
È inoltre vietato l'utilizzo commerciale di suddetti contenuti.

[title-image]:images/title.jpeg
[tests-image]:images/coverage.png
[tui-image]:images/tui.png
[gui-image]:images/gui.png

[game-link]:https://www.craniocreations.it/prodotto/my-shelfie
[java-link]:https://www.oracle.com/java/technologies/javase/jdk19-archive-downloads.html
[maven-link]:https://maven.apache.org/download.cgi
