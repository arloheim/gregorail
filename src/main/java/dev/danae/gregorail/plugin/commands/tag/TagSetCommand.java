package dev.danae.gregorail.plugin.commands.tag;

import java.util.Map;
import java.util.stream.Stream;
import dev.danae.common.commands.CommandContext;
import dev.danae.common.commands.CommandException;
import dev.danae.common.commands.CommandUsageException;
import dev.danae.common.commands.arguments.ArgumentType;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.commands.ManagerCommand;


public class TagSetCommand extends ManagerCommand
{
  // Constructor
  public TagSetCommand(Manager manager)
  {
    super(manager, "gregorail.tag.set");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {
    // Validate the number of arguments
    if (!context.hasAtLeastArgumentsCount(3))
      throw new CommandUsageException();
    
    // Create a scanner for the arguments
    var scanner = context.getArgumentsScanner();
    
    // Parse the arguments
    var code = this.getManager().getCodeArgumentType().parse(scanner);
    var propertyName = ArgumentType.getIdentifierArgumentType(Stream.of("name", "url")).parse(scanner);
    var propertyValue = scanner.takeRemaining(" ", "value");

    // Set the property of the code tag
    if (propertyName.equals("name"))
    {
      // Set the name of the code tag
      var name = propertyValue;
      this.getManager().setCodeTag(code, codeTag -> codeTag.withName(name));
    
      // Send a message about the updated name of the code tag
      context.sendMessage(this.getManager().deserializeMessage("tag-name-changed", Map.of(
        "code", code,
        "name", name)));
    }
    else if (propertyName.equals("url"))
    {
      // Set the URL of the code tag
      var url = propertyValue;
      this.getManager().setCodeTag(code, codeTag -> codeTag.withUrl(url));
    
      // Send a message about the updated URL of the code tag
      context.sendMessage(this.getManager().deserializeMessage("tag-url-changed", Map.of(
        "code", code,
        "url", url)));
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
