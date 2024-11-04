package dev.danae.gregorail.plugin.commands.tag;

import java.util.stream.Stream;
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
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {    
    // Validate the number of arguments
    if (!context.hasArgumentsCount(0))
      throw new CommandUsageException();
      
    // Send a message listing the code tags
    context.sendRichMessage(Formatter.formatTagListMessage(this.getManager().getDefinedCodeTags()));
  }

  // Return suggestions for the specified command context
  @Override
  public Stream<String> suggest(CommandContext context)
  {
    return Stream.empty();
  }
}
