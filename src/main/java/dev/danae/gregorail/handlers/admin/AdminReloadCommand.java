package dev.danae.gregorail.handlers.admin;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.commands.CommandContext;
import dev.danae.gregorail.commands.CommandException;
import dev.danae.gregorail.commands.CommandHandler;
import dev.danae.gregorail.commands.CommandUsageException;


public class AdminReloadCommand extends CommandHandler
{
  // Constructor
  public AdminReloadCommand(RailPlugin plugin)
  {
    super(plugin, "gregorail.admin");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {     
    // Reload the plugin
    this.plugin.reload();
    
    // Send information about the reload
    context.getSender().sendMessage(String.format("Reloaded %s", this.plugin.getDescription().getName()));
  }
}
