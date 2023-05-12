package it.polimi.ingsw.model.messages;

import java.io.Serializable;
import java.util.List;

public record StartGamePayload(List<String> nicknames, Integer personalGoalId, int XCGnumber,
                               int YCGnumber) implements Serializable {
}
