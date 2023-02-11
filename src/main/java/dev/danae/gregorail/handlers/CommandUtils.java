package dev.danae.gregorail.handlers;

import dev.danae.gregorail.commands.CommandException;
import org.bukkit.block.data.Rail;


public class CommandUtils
{  
  // Parse a rail shape from a string
  public static Rail.Shape parseShape(String string) throws CommandException
  {
    try
    {
      return Rail.Shape.valueOf(string.toUpperCase());
    }
    catch (IllegalArgumentException | NullPointerException ex)
    {
      throw new CommandException(String.format("Shape \"%s\" is an invalid rail shape", string.toLowerCase()), ex);
    }
  }
}
