package dev.danae.gregorail.util.location;


public class InvalidLocationException extends Exception
{  
  // Constructor
  public InvalidLocationException(String message)
  {
    super(message);
  }
  public InvalidLocationException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
