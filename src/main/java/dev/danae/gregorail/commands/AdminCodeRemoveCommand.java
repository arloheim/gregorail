package dev.danae.gregorail.commands;

import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandHandler;
import dev.danae.gregorail.util.commands.CommandUsageException;
import dev.danae.gregorail.util.minecart.CodeUtils;
import dev.danae.gregorail.util.minecart.InvalidCodeException;
import java.util.List;


public class AdminCodeRemoveCommand extends CommandHandler
{
  // Constructor
  public AdminCodeRemoveCommand()
  {
    super("gregorail.admin");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {     
    try
    {
      // Parse the arguments
      if (!context.hasArgumentsCount(1))
        throw new CommandUsageException();
      
      var code = CodeUtils.createCode(context.getArgument(0));
      
      // Remove the display name of the code
      CodeUtils.removeDisplayName(code);
      
      // Send information about the updated code
      context.getSender().sendMessage(String.format("The display name for code \"%s\" has been removed", code));
    }
    catch (InvalidCodeException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
  
  // Handle tab completion of the command
  @Override
  public List<String> handleTabCompletion(CommandContext context)
  {
    if (context.hasArgumentsCount(1))
      return CommandUtils.handleCodeTabCompletion(context.getArgument(0));
    else
      return null;
  }
}
