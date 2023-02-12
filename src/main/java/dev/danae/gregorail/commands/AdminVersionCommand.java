package dev.danae.gregorail.commands;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandHandler;
import dev.danae.gregorail.util.commands.CommandUsageException;


public class AdminVersionCommand extends CommandHandler
{
  // Constructor
  public AdminVersionCommand()
  {
    super("gregorail.admin");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {     
    // Send information about the version
    var desc = RailPlugin.getInstance().getDescription();
    context.getSender().sendMessage(String.format("%s %s (API version %s)", desc.getName(), desc.getVersion(), desc.getAPIVersion()));
  }
}
