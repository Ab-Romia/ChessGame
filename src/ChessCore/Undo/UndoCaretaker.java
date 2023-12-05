package ChessCore.Undo;

import java.util.Stack;

public class UndoCaretaker {
    private Stack<UndoMemento> meme = new Stack<>();

    public void addMemento(UndoMemento memento) {
        meme.push(memento);
    }
    public UndoMemento getMemento() {
        return meme.pop();
    }
        public boolean isEmpty() {
        return meme.isEmpty();
    }

}
