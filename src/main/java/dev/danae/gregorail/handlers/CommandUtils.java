package dev.danae.gregorail.handlers;

import org.bukkit.block.data.Rail;


public class CommandUtils
{  
  // Parse a rail shape from a string
  public static Rail.Shape parseShape(String string)
  {
    try
    {
      return Rail.Shape.valueOf(string.toUpperCase());
    }
    catch (IllegalArgumentException | NullPointerException ex)
    {
      return null;
    }
  }
}
