package dev.danae.gregorail.commands;

import dev.danae.gregorail.util.EnumUtils;
import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.minecart.CodeUtils;
import dev.danae.gregorail.util.minecart.MinecartUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Material;
import org.bukkit.block.data.Rail;


public class CommandUtils
{    
  // Parse a block material from a string
  public static Material parseMaterial(String string, boolean requireBlock) throws CommandException
  {
    var material = Material.matchMaterial(string);
    if (material == null)
      throw new CommandException(String.format("Material \"%s\" is an invalid material", string.toLowerCase()));
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
      throw new CommandException(String.format("Shape \"%s\" is an invalid shape", string.toLowerCase()), ex);
    }
  }
  
  // Parse a speed multiplier from a string
  public static double parseSpeedMultiplier(String string) throws CommandException
  {
    try
    {
      var speedMultiplier = Double.parseDouble(string);
      if (speedMultiplier < 0 || speedMultiplier > 4)
        throw new CommandException("Speed multiplier must be between 0 and 4");
      
      return speedMultiplier;
    }
    catch (NumberFormatException ex)
    {
      throw new CommandException(String.format("Speed multiplier \"%s\" is an invalid speed multiplier", string), ex);
    }
  }
  
  
  // Handle tab completion for a property
  public static List<String> handlePropertyTabCompletion(String arg, String... properties)
  {
    if (!arg.startsWith("#"))
      return null;
    
    var stream = Arrays.stream(properties).map(p -> String.format("#%s", p)).sorted();
    
    if (!arg.isEmpty())
      return stream.filter(s -> s.startsWith(arg)).toList();
    else
      return stream.toList();
  }
  
  // Handle tab completion of a code argument
  public static List<String> handleCodeTabCompletion(String arg)
  {
    if (!arg.isEmpty())
      return codesWithDisplayNameNamesAsStream().filter(s -> s.startsWith(arg)).toList();
    else
      return codesWithDisplayNameNamesAsStream().toList();
  }
  
  // Handle tab completion of a code list argument
  public static List<String> handleCodesTabCompletion(String arg)
  {
    var delimiterPos = arg.lastIndexOf("|");
    
    var prefix = delimiterPos > -1 ? arg.substring(0, delimiterPos + 1) : "";
    var current = delimiterPos > -1 ? arg.substring(delimiterPos + 1) : arg;
    
    if (!current.isEmpty())
      return codesWithDisplayNameNamesAsStream(prefix).filter(s -> s.startsWith(current, delimiterPos + 1)).toList();
    else
      return codesWithDisplayNameNamesAsStream(prefix).toList();
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
    list.addAll(materialNamesAsStream(true, "@").toList());
    list.addAll(materialNamesAsStream(true, "^").toList());
    return list;
  }
  
  // Handle tab completion of a material argument
  public static List<String> handleMaterialTabCompletion(String arg, boolean requireBlock)
  {
    if (!arg.isEmpty())
      return materialNamesAsStream(requireBlock).filter(s -> s.startsWith(arg)).toList();
    else
      return materialNamesAsStream(requireBlock).toList();
  }
  
  // Handle tab completion of a shape argument
  public static List<String> handleShapeTabCompletion(String arg)
  {
    if (!arg.isEmpty())
      return shapeNamesAsStream().filter(s -> s.startsWith(arg)).toList();
    else
      return shapeNamesAsStream().toList();
  }
  
  // Handle tab completion of a speed multiplier argument
  public static List<String> handleSpeedMultiplierTabCompletion(String arg)
  {
    return List.of("0.0", "1.0", "2.0", "3.0", "4.0");
  }
  
  
  // Return a stream containing the codes for which a display names has been defined prefixed with the specified string
  public static Stream<String> codesWithDisplayNameNamesAsStream(String prefix)
  {
    return CodeUtils.codesWithDisplayNameAsStream()
      .map(code -> (prefix != null ? prefix : "") + code.getId());
  }
  
  // Return a stream containing the codes for which a display names has been defined
  public static Stream<String> codesWithDisplayNameNamesAsStream()
  {
    return codesWithDisplayNameNamesAsStream(null);
  }
  
  // Return a stream of all block material names prefixed with the specified string
  public static Stream<String> materialNamesAsStream(boolean requireBlock, String prefix)
  {
    return Arrays.stream(Material.values())
      .filter(material -> !requireBlock || material.isBlock())
      .map(material -> (prefix != null ? prefix : "") + material.name().toLowerCase())
      .sorted();
  }
  
  // Return a stream of all block material names
  public static Stream<String> materialNamesAsStream(boolean requireBlock)
  {
    return CommandUtils.materialNamesAsStream(requireBlock, null);
  }
  
  // Return a stream of all shape names
  public static Stream<String> shapeNamesAsStream()
  {
    return Arrays.stream(Rail.Shape.values())
      .map(shape -> shape.name().toLowerCase())
      .sorted();
  }
  
  
  // Convert a speed multiplier to a text component
  public static BaseComponent[] formatSpeedMultiplier(double speedMultiplier)
  {
    return new ComponentBuilder()
      .append(String.format(Locale.ENGLISH, "%.2f", speedMultiplier))
        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(String.format(Locale.ENGLISH, "%.2f m/s, %.2f km/h", speedMultiplier * MinecartUtils.defaultSpeed * 20, speedMultiplier * MinecartUtils.defaultSpeed * 20 * 3.6))))
      .create();
  }
}
