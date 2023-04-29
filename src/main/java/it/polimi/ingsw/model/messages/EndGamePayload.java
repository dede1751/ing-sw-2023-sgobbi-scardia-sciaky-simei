package it.polimi.ingsw.model.messages;

import java.util.Map;

public record EndGamePayload(String winner, Map<String, Integer> points) {}
