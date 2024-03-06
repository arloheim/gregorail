package dev.danae.gregorail.plugin.commands.admin;

import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.commands.ManagerCommand;
import dev.danae.gregorail.plugin.commands.CommandContext;
import dev.danae.gregorail.plugin.commands.CommandException;
import dev.danae.gregorail.plugin.commands.CommandUsageException;
import dev.danae.gregorail.plugin.Formatter;
import java.util.List;


public class AdminCodeListCommand extends ManagerCommand
{
  // Constructor
  public AdminCodeListCommand(Manager manager)
  {
    super(manager, "gregorail.admin");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {    
    // Validate the number of arguments
    if (!context.hasArgumentsCount(0))
      throw new CommandUsageException();
      
    // Send a message listing the codes
    context.sendMessage(Formatter.formatAdminCodeDisplayNameListMessage(this.getManager().getDefinedDisplayNames()));
  }

  // Handle tab completion of the command
  @Override
  public List<String> handleTabCompletion(CommandContext context)
  {
    return null;
  }
}
