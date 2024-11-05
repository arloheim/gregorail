package dev.danae.gregorail.plugin.commands.tag;

import java.util.Map;
import java.util.stream.Stream;
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
    context.sendMessage(this.getManager().formatMessage("tag-removed", Map.of(
      "code", code)));
  }
  
  // Return suggestions for the specified command context
  @Override
  public Stream<String> suggest(CommandContext context)
  {
    if (context.hasArgumentsCount(1))
    return this.getManager().getCodeArgumentType().suggest(context, 0);
    else
      return Stream.empty();
  }
}
