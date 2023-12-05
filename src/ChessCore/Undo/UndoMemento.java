package ChessCore.Undo;

import BoardGUI.Move;
import ChessCore.Pieces.Piece;

public class UndoMemento {
    private Move move;
    private String turn;

    public UndoMemento(Move m, String t) {
        this.move = m;
        this.turn = t;
    }
    public Move getMove() {
        return move;
    }
    public String getTurn() {
        return turn;
    }

}
