package Exceptions;

public class Incheck extends Exception{
    public Incheck(String message) {
        super(message+" is in check");
    }
}
