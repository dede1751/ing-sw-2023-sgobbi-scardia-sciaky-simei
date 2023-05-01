package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.controller.LobbyController;

import java.io.Serializable;
import java.util.List;

public record LobbyPayload (List<LobbyController.LobbyView> lobbyViewList) implements Serializable {}
