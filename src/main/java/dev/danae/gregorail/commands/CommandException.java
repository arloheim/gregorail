package dev.danae.gregorail.commands;


public class CommandException extends Exception
{
  // Constructor
  public CommandException(String message, Throwable cause)
  {
    super(message, cause);
  }
  public CommandException(String message)
  {
    super(message);
  }
  public CommandException(Throwable cause)
  {
    super(cause);
  }
  public CommandException()
  {
    super();
  }
}
