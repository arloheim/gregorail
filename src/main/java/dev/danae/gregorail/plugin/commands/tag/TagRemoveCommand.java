package dev.danae.gregorail.plugin.commands.tag;

import java.util.List;
import java.util.Map;
import dev.danae.common.commands.CommandContext;
import dev.danae.common.commands.CommandException;
import dev.danae.common.commands.CommandUsageException;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.commands.ManagerCommand;


public class TagRemoveCommand extends ManagerCommand
{
  // Constructor
  public TagRemoveCommand(Manager manager)
  {
    super(manager, "gregorail.tag.remove");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {    
    // Validate the number of arguments
    if (!context.hasArgumentsCount(1))
      throw new CommandUsageException();
    
    // Create a scanner for the arguments
    var scanner = context.getArgumentsScanner();
    
    // Parse the arguments
    var code = this.getManager().getCodeArgumentType().parse(scanner);
    
    // Remove the code tag
    this.getManager().removeCodeTag(code);
    
    // Send a message about the removed code tag
    context.sendMessage(this.getManager().deserializeMessage("tag-removed", Map.of(
      "code", code)));
  }
  
  // Handle tab completion of the command
  @Override
  public List<String> handleTabCompletion(CommandContext context)
  {
    if (context.hasArgumentsCount(1))
    return this.getManager().getCodeArgumentType().suggest(context, 0).toList();
    else
      return List.of();
  }
}
