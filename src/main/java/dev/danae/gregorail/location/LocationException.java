package dev.danae.gregorail.location;


public class LocationException extends Exception
{
  // The string that was being parsed
  private final String string;
  
  
  // Constructor
  public LocationException(String string, String message)
  {
    super(message);
    this.string = string;
  }
  public LocationException(String string, String message, Throwable cause)
  {
    super(message, cause);
    this.string = string;
  }
  
  // Return the string that was being parsed
  public String getString()
  {
    return this.string;
  }
}
