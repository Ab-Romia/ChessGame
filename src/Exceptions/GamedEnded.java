package Exceptions;

public class GamedEnded extends Exception{
    public GamedEnded() {
        super("Game Already Ended");
    }
}
