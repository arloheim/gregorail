package dev.danae.gregorail.commands;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandHandler;
import dev.danae.gregorail.util.commands.CommandUsageException;


public class AdminReloadCommand extends CommandHandler
{
  // Constructor
  public AdminReloadCommand()
  {
    super("gregorail.admin");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {     
    // Reload the plugin
    RailPlugin.getInstance().reload();
    
    // Send information about the reload
    context.getSender().sendMessage(String.format("Reloaded %s", RailPlugin.getInstance().getDescription().getName()));
  }
}
