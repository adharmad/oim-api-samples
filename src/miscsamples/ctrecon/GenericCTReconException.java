package miscsamples.ctrecon;

public class GenericCTReconException extends Exception 
{
    
    private String message;
    
    public GenericCTReconException()
    {
        super();
    }
    
    public GenericCTReconException(String msg)
    {
        this();
        this.message = msg;
    }
    
    /**
     * @return Returns the message.
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Sets the value of the message.
     * 
     * @param message The message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
