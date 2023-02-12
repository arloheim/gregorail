package dev.danae.gregorail.commands;

import dev.danae.gregorail.util.EnumUtils;
import dev.danae.gregorail.util.commands.CommandException;
import org.bukkit.Material;
import org.bukkit.block.data.Rail;


public class CommandUtils
{  
  // Parse a block material from a string
  public static Material parseMaterial(String string, boolean requireBlock) throws CommandException
  {
    var material = Material.matchMaterial(string);
    if (material == null)
      throw new CommandException(String.format("Material \"%s\" could not be found", string.toLowerCase()));
    if (requireBlock && !material.isBlock())
      throw new CommandException(String.format("Material \"%s\" is not a block material", string.toLowerCase()));
      
    return material;
  }
  
  // Parse a rail shape from a string
  public static Rail.Shape parseShape(String string) throws CommandException
  {
    try
    {
      return EnumUtils.parseEnum(string, Rail.Shape.class);
    }
    catch (IllegalArgumentException | NullPointerException ex)
    {
      throw new CommandException(String.format("Shape \"%s\" could not be found", string.toLowerCase()), ex);
    }
  }
}
