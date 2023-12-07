// File: src/ChessCore/Undo/Memento.java
package ChessCore.Undo;

import ChessCore.Enum.CoordinateEnum;
import ChessCore.Pieces.Piece;

import java.util.HashMap;
import java.util.Map;

public class Memento {
    private final Map<CoordinateEnum, Piece> boardState;

    public Memento(Map<CoordinateEnum, Piece> state) {
                this.boardState = new HashMap<>();
        for (Map.Entry<CoordinateEnum, Piece> entry : state.entrySet()) {
            this.boardState.put(entry.getKey(), entry.getValue().copy());
        }
    }

    public Map<CoordinateEnum, Piece> getState() {
        return boardState;
    }
}