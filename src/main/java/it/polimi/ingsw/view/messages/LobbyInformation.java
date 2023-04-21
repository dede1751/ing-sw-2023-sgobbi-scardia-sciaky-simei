package it.polimi.ingsw.view.messages;

import java.io.Serializable;

public record LobbyInformation(int size, String name) implements Serializable {}