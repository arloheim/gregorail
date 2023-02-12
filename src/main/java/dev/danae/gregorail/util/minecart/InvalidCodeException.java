package dev.danae.gregorail.util.minecart;


public class InvalidCodeException extends Exception
{  
  // Constructor
  public InvalidCodeException(String message)
  {
    super(message);
  }
  public InvalidCodeException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
