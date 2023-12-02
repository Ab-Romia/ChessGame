package Exceptions;

public class InCheck extends Exception{
    public InCheck(String message) {
        super(message+" is in check");
    }
}
