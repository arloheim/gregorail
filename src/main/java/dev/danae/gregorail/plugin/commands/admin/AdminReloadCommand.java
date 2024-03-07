package dev.danae.gregorail.plugin.commands.admin;

import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.GregoRailPlugin;
import dev.danae.gregorail.plugin.commands.ManagerCommand;
import dev.danae.gregorail.plugin.commands.CommandContext;
import dev.danae.gregorail.plugin.commands.CommandException;
import dev.danae.gregorail.plugin.commands.CommandUsageException;
import java.util.List;


public class AdminReloadCommand extends ManagerCommand
{
  // The plugin of the command
  private final GregoRailPlugin plugin;
  
  
  // Constructor
  public AdminReloadCommand(Manager manager, GregoRailPlugin plugin)
  {
    super(manager, "gregorail.admin");
    
    this.plugin = plugin;
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {     
    // Reload the plugin
    this.plugin.loadPlugin();
    
    // Send information about the reload
    context.sendMessage(String.format("Reloaded %s", this.plugin.getDescription().getName()));
  }

  // Handle tab completion of the command
  @Override
  public List<String> handleTabCompletion(CommandContext context)
  {
    return List.of();
  }
}
