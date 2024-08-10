package dev.danae.gregorail.plugin.commands.admin;

import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.GregoRailPlugin;
import dev.danae.gregorail.plugin.commands.ManagerCommand;
import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandUsageException;
import java.util.List;


public class AdminVersionCommand extends ManagerCommand
{
  // The plugin of the command
  private final GregoRailPlugin plugin;
  
  
  // Constructor
  public AdminVersionCommand(Manager manager, GregoRailPlugin plugin)
  {
    super(manager, "gregorail.admin");
    
    this.plugin = plugin;
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {     
    // Send information about the version
    var desc = this.plugin.getDescription();
    context.sendMessage(String.format("%s %s (API version %s)", desc.getName(), desc.getVersion(), desc.getAPIVersion()));
  }

  // Handle tab completion of the command
  @Override
  public List<String> handleTabCompletion(CommandContext context)
  {
    return List.of();
  }
}
