package dev.danae.gregorail.commands;

import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandHandler;
import dev.danae.gregorail.util.commands.CommandUsageException;
import dev.danae.gregorail.util.minecart.CodeUtils;


public class AdminCodeListCommand extends CommandHandler
{
  // Constructor
  public AdminCodeListCommand()
  {
    super("gregorail.admin");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {
    // Parse the arguments
    if (!context.hasArgumentsCount(0))
      throw new CommandUsageException();
      
    // Get the codes with a defined display name
    var codeEntries = CodeUtils.codeEntriesWithDisplayName();
      
    // Send information about the codes
    context.sendMessage(String.format("%d display names are defined", codeEntries.size()));
    for (var e : codeEntries)
      context.sendMessage(String.format("- %s : %s", e.getKey(), e.getValue()));
  }
}
