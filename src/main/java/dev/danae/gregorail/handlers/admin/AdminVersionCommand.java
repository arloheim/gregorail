package dev.danae.gregorail.handlers.admin;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.commands.CommandContext;
import dev.danae.gregorail.commands.CommandException;
import dev.danae.gregorail.commands.CommandHandler;
import dev.danae.gregorail.commands.CommandUsageException;


public class AdminVersionCommand extends CommandHandler
{
  // Constructor
  public AdminVersionCommand(RailPlugin plugin)
  {
    super(plugin, "gregorail.admin");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {     
    // Send information about the version
    var desc = this.plugin.getDescription();
    context.getSender().sendMessage(String.format("%s %s (API version %s)", desc.getName(), desc.getVersion(), desc.getAPIVersion()));
  }
}
