public class FoosballException extends Exception{
    public int statusCode;
    public FoosballException(String message, int statusCode){
        super(message);
        this.statusCode = statusCode;
    }
}
