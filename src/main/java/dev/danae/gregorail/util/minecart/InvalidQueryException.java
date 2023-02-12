package dev.danae.gregorail.util.minecart;


public class InvalidQueryException extends Exception
{  
  // Constructor
  public InvalidQueryException(String message)
  {
    super(message);
  }
  public InvalidQueryException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
