package dev.danae.gregorail.plugin.commands.tag;

import java.util.Map;
import java.util.stream.Stream;
import dev.danae.common.commands.CommandContext;
import dev.danae.common.commands.CommandException;
import dev.danae.common.commands.CommandUsageException;
import dev.danae.common.commands.arguments.ArgumentType;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.commands.ManagerCommand;


public class TagClearCommand extends ManagerCommand
{
  // Constructor
  public TagClearCommand(Manager manager)
  {
    super(manager, "gregorail.tag.clear");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {
    // Validate the number of arguments
    if (!context.hasArgumentsCount(2))
      throw new CommandUsageException();
    
    // Create a scanner for the arguments
    var scanner = context.getArgumentsScanner();
    
    // Parse the arguments
    var code = this.getManager().getCodeArgumentType().parse(scanner);
    var propertyName = ArgumentType.getIdentifierArgumentType(Stream.of("name", "url")).parse(scanner);

    // Set the property of the code tag
    if (propertyName.equals("name"))
    {
      // Remove the name of the code tag
      this.getManager().setCodeTag(code, codeTag -> codeTag.withName(null));
    
      // Send a message about the removed name of the code tag
      context.sendMessage(this.getManager().deserializeMessage("tag-name-cleared", 
        Map.of("code", code)));
    }
    else if (propertyName.equals("url"))
    {
      // Remove the URL of the code tag
      this.getManager().setCodeTag(code, codeTag -> codeTag.withUrl(null));
    
      // Send a message about the removed URL of the code tag
      context.sendMessage(this.getManager().deserializeMessage("tag-url-cleared", Map.of(
        "code", code)));
    }
    else
    {
      // Invalid property
      throw new CommandException(String.format("\"%s\" is an invalid code tag property ", propertyName));
    }
  }
  
  // Return suggestions for the specified command context
  @Override
  public Stream<String> suggest(CommandContext context)
  {
    if (context.hasArgumentsCount(2))
      return Stream.of("name", "url");
    else if (context.hasArgumentsCount(1))
      return this.getManager().getCodeArgumentType().suggest(context, 0);
    else
      return Stream.empty();
  }
}
