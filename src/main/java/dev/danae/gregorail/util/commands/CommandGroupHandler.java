package dev.danae.gregorail.util.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CommandGroupHandler extends CommandHandler
{  
  // Map of subcommands
  private final Map<String, CommandHandler> subcommands = new HashMap<>();
  
  
  // Constructor
  public CommandGroupHandler(List<String> permissions)
  {
    super(permissions);
  }
  public CommandGroupHandler(String... permissions)
  {
    super(permissions);
  }
  public CommandGroupHandler()
  {
    super();
  }
  
  
  // Register the command handler as a listener
  @Override
  public void registerListener()
  {
    super.registerListener();
    
    for (var handler : this.subcommands.values())
      handler.registerListener();
  }
  
  
  // Register a subcommand
  public CommandGroupHandler registerSubcommand(String name, CommandHandler handler)
  {
    this.subcommands.put(name, handler);
    return this;
  }
  
  // Unregister a subcommand
  public CommandGroupHandler unregisterSubcommand(String name)
  {
    this.subcommands.remove(name);
    return this;
  }
  
  
  // Return a subcommand handler for the specified name
  public CommandHandler getSubcommand(String name)
  {
    return this.subcommands.entrySet().stream()
      .filter(e -> e.getKey().equalsIgnoreCase(name))
      .map(e -> e.getValue())
      .findFirst()
      .orElse(null);
  }
  
  
  // Handle a command
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
    handler.handle(context.withArguments(Arrays.copyOfRange(context.getArguments(), 1, context.getArguments().length)));
  }
  
  // Handle tab completion of a command
  @Override
  public List<String> handleTabCompletion(CommandContext context)
  {
    // Check if the arguments provide a subcommand
    if (!context.hasAtLeastArgumentsCount(1))
      return null;
    
    // If there only is one argument, then tab complete the subcommand
    if (context.hasArgumentsCount(1))
      return this.subcommands.keySet().stream().sorted().toList();
      
    // Otherwise, check if there is a subcommand that matches and delegate to that
    var handler = this.getSubcommand(context.getArgument(0));
    if (handler == null)
      return null;
    
    return handler.handleTabCompletion(context.withArguments(Arrays.copyOfRange(context.getArguments(), 1, context.getArguments().length)));
  }
}
