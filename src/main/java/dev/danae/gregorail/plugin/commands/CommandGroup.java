package dev.danae.gregorail.plugin.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.plugin.Plugin;


public class CommandGroup extends Command
{  
  // Constructor
  public CommandGroup(String... permissions)
  {
    super(permissions);
  }
  
  // Constructor without permissions
  public CommandGroup()
  {
  }
  
  // Map of subcommands
  private final Map<String, Command> subcommands = new HashMap<>();
  
  
  // Register a subcommand
  public CommandGroup registerSubcommand(String name, Command command)
  {
    this.subcommands.put(name, command);
    return this;
  }
  
  // Unregister a subcommand
  public CommandGroup unregisterSubcommand(String name)
  {
    this.subcommands.remove(name);
    return this;
  }
  
  
  // Return a subcommand for the specified name
  public Command getSubcommand(String name)
  {
    return this.subcommands.entrySet().stream()
      .filter(e -> e.getKey().equalsIgnoreCase(name))
      .map(e -> e.getValue())
      .findFirst()
      .orElse(null);
  }
  
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {
    // Check if the arguments provide a subcommand
    if (!context.hasAtLeastArgumentsCount(1))
      throw new CommandException("You must provide a subcommand");
    
    // Check if there is a subcommand that matches
    var handler = this.getSubcommand(context.getArgument(0));
    if (handler == null)
      throw new CommandException("You must provide a valid subcommand");
    
    // Handle the subcommand
    handler.handle(context.sliceArguments(1));
  }
  
  // Handle tab completion of the command
  @Override
  public List<String> handleTabCompletion(CommandContext context)
  {
    // Check if the arguments provide a subcommand
    if (!context.hasAtLeastArgumentsCount(1))
      return null;
    
    // If there only is one argument, then tab complete the name of the subcommand
    if (context.hasArgumentsCount(1))
    {
      var arg = context.getArgument(0);
      
      var list = this.subcommands.keySet().stream().sorted().toList();
      if (!arg.isEmpty())
        return list.stream().filter(s -> s.startsWith(arg)).toList();
      else
        return list;
    }
      
    // Otherwise, check if there is a subcommand that matches and delegate to that
    var handler = this.getSubcommand(context.getArgument(0));
    if (handler == null)
      return null;
    
    return handler.handleTabCompletion(context.sliceArguments(1));
  }
  
  // Register the command handler as a listener for the specified plugin
  @Override
  public void publishEvents(Plugin plugin)
  {    
    for (var handler : this.subcommands.values())
      handler.publishEvents(plugin);
  }
}
