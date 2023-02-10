package dev.danae.gregorail.util.minecart;


public class InvalidMinecartCodeException extends Exception
{  
  // Constructor
  public InvalidMinecartCodeException(String message)
  {
    super(message);
  }
  public InvalidMinecartCodeException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
