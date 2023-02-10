package dev.danae.gregorail.commands;

import dev.danae.gregorail.RailPlugin;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CommandGroupHandler extends CommandHandler
{  
  // Map of subcommands
  private final Map<String, CommandHandler> subcommands = new HashMap<>();
  
  
  // Constructor
  public CommandGroupHandler(RailPlugin plugin, List<String> permissions)
  {
    super(plugin, permissions);
  }
  public CommandGroupHandler(RailPlugin plugin, String... permissions)
  {
    super(plugin, permissions);
  }
  public CommandGroupHandler(RailPlugin plugin)
  {
    super(plugin);
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
  
  
  // Handle a command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {
    // Check if the arguments provide a subcommand
    if (context.getArguments().length < 1)
      throw new CommandException("You must provide a subcommand");
    
    // Check if there is a subcommand that matches
    var handler = this.subcommands.entrySet().stream()
      .filter(e -> e.getKey().equalsIgnoreCase(context.getArgument(0)))
      .map(e -> e.getValue())
      .findFirst()
      .orElseThrow(() -> new CommandException("You must provide a valid subcommand"));
    
    // Handle the subcommand
    handler.handle(context.withArguments(Arrays.copyOfRange(context.getArguments(), 1, context.getArguments().length)));
  }
}
