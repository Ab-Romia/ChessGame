package Exceptions;

public class Won extends Exception{
    public Won(String message) {
        super(message+" Won");
    }
}