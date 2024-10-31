package dev.danae.gregorail.plugin.commands.tag;

import java.util.List;
import dev.danae.common.commands.CommandContext;
import dev.danae.common.commands.CommandException;
import dev.danae.common.commands.CommandUsageException;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.commands.ManagerCommand;
import dev.danae.gregorail.plugin.Formatter;


public class TagRemoveCommand extends ManagerCommand
{
  // Constructor
  public TagRemoveCommand(Manager manager)
  {
    super(manager, "gregorail.tag.remove");
  }
    
  
  // Handle the command
  @Override
  {     
    try
    {
      // Validate the number of arguments
      if (!context.hasArgumentsCount(1))
        throw new CommandUsageException();
      
      // Create a scanner for the arguments
      var scanner = context.getArgumentsScanner(this.getManager());
      
      // Parse the arguments
      var code = scanner.nextCode();
      
      // Remove the code tag
      this.getManager().removeCodeTag(code);
      
      // Send a message about the removed code tag
      context.sendMessage(Formatter.formatTagRemovedMessage(code));
    }
    catch (ParserException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  public void handle(CommandContext context) throws CommandException
  }
  
  // Handle tab completion of the command
  @Override
  public List<String> handleTabCompletion(CommandContext context)
  {
    if (context.hasArgumentsCount(1))
      return this.handleCodeTabCompletion(context.getArgument(0));
    else
      return List.of();
  }
}
