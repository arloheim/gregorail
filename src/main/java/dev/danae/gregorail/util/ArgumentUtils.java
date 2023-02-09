package dev.danae.gregorail.util;

import org.bukkit.block.data.Rail;


public class ArgumentUtils
{
  // Parse a rail shape from a string
  public static Rail.Shape parseRailShape(String string)
  {
    try
    {
      return Rail.Shape.valueOf(string.toUpperCase());
    }
    catch (IllegalArgumentException ex)
    {
      return null;
    }
  }
}
