package dev.danae.gregorail.commands;

import dev.danae.gregorail.util.EnumUtils;
import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
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
  
  
  // Handle tab completion of a location argument
  public static List<String> handleLocationTabCompletion(CommandContext context, int argumentIndex)
  {
    if (!context.hasAtLeastArgumentsCount(argumentIndex + 1))
      return null;
    
    var arg = context.getArgument(argumentIndex);
    
    // If there is between 2 and 3 arguments, return just the current relative location
    if (context.hasAtLeastArgumentsCount(argumentIndex + 4))
      return null;
    if (context.hasAtLeastArgumentsCount(argumentIndex + 2))
      return List.of("~");
    
    // If the argument is a block location, then return a list of suitable materials
    if (arg.startsWith("@") || arg.startsWith("^"))
    {
      var prefix = arg.substring(0, 1);
      var material = arg.substring(1);
      
      return handleMaterialTabCompletion(material, true).stream()
        .map(s -> prefix + s)
        .toList();
    }
    
    // If the argument is another type of location, return nothing
    if (!arg.isEmpty())
      return null;
    
    // Return all location options
    var list = new ArrayList<String>();
    list.add("~");
    list.addAll(materialNamesStream(true, "@").toList());
    list.addAll(materialNamesStream(true, "^").toList());
    return list;
  }
  
  // Handle tab completion of a material argument
  public static List<String> handleMaterialTabCompletion(String arg, boolean requireBlock)
  {
    if (!arg.isEmpty())
      return materialNamesStream(requireBlock).filter(s -> s.startsWith(arg)).toList();
    else
      return materialNamesStream(requireBlock).toList();
  }
  
  // Handle tab completion of a shape argument
  public static List<String> handleShapeTabCompletion(String arg)
  {
    if (!arg.isEmpty())
      return shapeNamesStream().filter(s -> s.startsWith(arg)).toList();
    else
      return shapeNamesStream().toList();
  }
  
  
  // Return a stream of all block material names prefixed with the specified string
  public static Stream<String> materialNamesStream(boolean requireBlock, String prefix)
  {
    return Arrays.stream(Material.values())
      .filter(material -> !requireBlock || material.isBlock())
      .map(material -> (prefix != null ? prefix : "") + material.name().toLowerCase())
      .sorted();
  }
  
  // Return a stream of all block material names
  public static Stream<String> materialNamesStream(boolean requireBlock)
  {
    return materialNamesStream(requireBlock, null);
  }
  
  // Return a stream of all shape names
  public static Stream<String> shapeNamesStream()
  {
    return Arrays.stream(Rail.Shape.values())
      .map(shape -> shape.name().toLowerCase())
      .sorted();
  }
}
