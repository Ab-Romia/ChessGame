package ChessCore.Undo;

import ChessCore.ChessBoard.ChessBoard;

import java.util.Stack;

public class Caretaker {
    private static final Stack<Memento> mementos = new Stack<>();

    public void saveState(ChessBoard chessBoard) {
        mementos.push(chessBoard.createMemento());
    }

    public void undo(ChessBoard chessBoard) {
        if (!mementos.isEmpty()) {
            chessBoard.restoreFromMemento(mementos.pop());

        }
    }
}