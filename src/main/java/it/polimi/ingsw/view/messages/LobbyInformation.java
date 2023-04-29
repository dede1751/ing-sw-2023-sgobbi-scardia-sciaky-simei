package it.polimi.ingsw.view.messages;

import java.io.Serializable;

public record LobbyInformation(Integer size, String name) implements Serializable {}