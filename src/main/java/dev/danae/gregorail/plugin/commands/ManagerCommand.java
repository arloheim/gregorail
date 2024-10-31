package dev.danae.gregorail.plugin.commands;

import java.util.List;
import java.util.stream.Stream;
import dev.danae.common.commands.Command;
import dev.danae.common.commands.arguments.ArgumentType;
import dev.danae.common.commands.arguments.StringArgumentType;
import dev.danae.gregorail.model.Code;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.model.Query;
import dev.danae.gregorail.model.arguments.CodeArgumentType;
import dev.danae.gregorail.model.arguments.CodeListArgumentType;
import dev.danae.gregorail.model.arguments.QueryArgumentType;
import dev.danae.gregorail.plugin.Formatter;


public abstract class ManagerCommand extends Command
{    
  // The manager of the command
  private final Manager manager;
  
  
  // Constructor
  public ManagerCommand(Manager manager, String... permissions)
  {
    super(permissions);
    
    this.manager = manager;
  }
  
  // Constructor without permissions
  public ManagerCommand(Manager manager)
  {    
    this.manager = manager;
  }
  
  
  // Return the manager of the command
  protected Manager getManager()
  {
    return this.manager;
  }
  
  
  // Handle tab completion of a code argument
  protected List<String> handleCodeTabCompletion(String arg)
  {
    return this.getManager().getDefinedCodeTags().keySet().stream()
      .filter(code -> arg.isEmpty() || code.getId().startsWith(arg))
      .map(code -> code.getId())
      .toList();
  }
  
  // Handle tab completion of a code list argument
  protected List<String> handleCodesTabCompletion(String arg)
  {
    var delimiterPos = arg.lastIndexOf("|");
    
    var prefix = delimiterPos > -1 ? arg.substring(0, delimiterPos + 1) : "";
    var currentArg = delimiterPos > -1 ? arg.substring(delimiterPos + 1) : arg;
    
    return this.getManager().getDefinedCodeTags().keySet().stream()
      .filter(code -> arg.isEmpty() || code.getId().startsWith(currentArg))
      .map(code -> prefix + code.getId())
      .toList();
  }
  
  // Handle tab completion of a location argument
  protected List<String> handleLocationTabCompletion(CommandContext context, int argumentIndex, boolean isQuery)
  {
    if (!context.hasAtLeastArgumentsCount(argumentIndex + 1))
      return List.of();
    
    var arg = context.getArgument(argumentIndex);
    
    // If there is between 2 and 3 arguments, return just the current relative location
    if (context.hasAtLeastArgumentsCount(argumentIndex + 4))
      return List.of();
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
      return List.of();
    
    // Return all location options
    var list = new ArrayList<String>();
    if (isQuery)
      list.add("||");
    list.add("~");
    list.addAll(Formatter.getAllMaterials(true, s -> "@" + s).toList());
    list.addAll(Formatter.getAllMaterials(true, s -> "^" + s).toList());
    return list;
  }
  
  // Handle tab completion of a speed multiplier argument
  protected List<String> handleSpeedMultiplierTabCompletion(String arg)
  {
    return List.of("0.0", "0.5", "1.0", "1.5", "2.0", "2.5", "3.0", "3.5", "4.0");
  }
  
  // Handle tab completion of a shape argument
  protected List<String> handleShapeTabCompletion(String arg)
  {
    return Formatter.getAllShapes()
      .filter(s -> arg.isEmpty() || s.startsWith(arg))
      .toList();
  }
  
  // Handle tab completion of a material argument
  protected List<String> handleMaterialTabCompletion(String arg, boolean requireBlock)
  {
    return Formatter.getAllMaterials(requireBlock)
      .filter(s -> arg.isEmpty() || s.startsWith(arg))
      .toList();
  }
  
  // Handle tab completion of a sound argument
  protected List<String> handleSoundTabCompletion(String arg)
  {
    return Formatter.getAllSounds()
      .filter(s -> arg.isEmpty() || s.startsWith(arg))
      .toList();
  }
}
