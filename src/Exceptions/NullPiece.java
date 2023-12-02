package Exceptions;

public class NullPiece extends Exception{
    public NullPiece() {
        super("No piece! Choose a position with a piece");
    }
}
