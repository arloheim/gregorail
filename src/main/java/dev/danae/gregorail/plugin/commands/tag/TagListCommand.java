package dev.danae.gregorail.plugin.commands.tag;

import java.util.List;
import dev.danae.common.commands.CommandContext;
import dev.danae.common.commands.CommandException;
import dev.danae.common.commands.CommandUsageException;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.commands.ManagerCommand;
import dev.danae.gregorail.plugin.Formatter;


public class TagListCommand extends ManagerCommand
{
  // Constructor
  public TagListCommand(Manager manager)
  {
    super(manager, "gregorail.tag.list");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException
  {    
    // Validate the number of arguments
    if (!context.hasArgumentsCount(0))
      throw new CommandUsageException();
      
    // Send a message listing the code tags
    context.sendMessage(Formatter.formatTagListMessage(this.getManager().getDefinedCodeTags()));
  }

  // Handle tab completion of the command
  @Override
  public List<String> handleTabCompletion(CommandContext context)
  {
    return List.of();
  }
}
