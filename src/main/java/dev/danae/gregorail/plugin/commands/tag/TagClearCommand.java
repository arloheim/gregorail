package dev.danae.gregorail.plugin.commands.tag;

import java.util.List;
import java.util.stream.Stream;
import dev.danae.common.commands.CommandContext;
import dev.danae.common.commands.CommandException;
import dev.danae.common.commands.CommandUsageException;
import dev.danae.common.commands.arguments.ArgumentType;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.commands.ManagerCommand;
import dev.danae.gregorail.plugin.Formatter;


public class TagClearCommand extends ManagerCommand
{
  // Constructor
  public TagClearCommand(Manager manager)
  {
    super(manager, "gregorail.tag.clear");
  }
    
  
  // Handle the command
  @Override
  {     
    try
    {
      // Validate the number of arguments
      if (!context.hasArgumentsCount(2))
        throw new CommandUsageException();
      
      // Create a scanner for the arguments
      var scanner = context.getArgumentsScanner(this.getManager());
      
      // Parse the arguments
      var code = scanner.nextCode();
      var propertyName = scanner.nextIdentifier();
  public void handle(CommandContext context) throws CommandException

      // Set the property of the code tag
      if (propertyName.equals("name"))
      {
        // Remove the name of the code tag
        this.getManager().setCodeTag(code, codeTag -> codeTag.withName(null));
      
        // Send a message about the removed name of the code tag
        context.sendMessage(Formatter.formatTagNameClearedMessage(code));
      }
      else if (propertyName.equals("url"))
      {
        // Remove the URL of the code tag
        this.getManager().setCodeTag(code, codeTag -> codeTag.withUrl(null));
      
        // Send a message about the removed URL of the code tag
        context.sendMessage(Formatter.formatTagUrlClearedMessage(code));
      }
      else
      {
        // Invalid property
        throw new CommandException(String.format("\"%s\" is an invalid code tag property ", propertyName));
      }
    }
    catch (ParserException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
  
  // Handle tab completion of the command
  @Override
  public List<String> handleTabCompletion(CommandContext context)
  {
    if (context.hasArgumentsCount(2))
      return List.of("name", "url");
    else if (context.hasArgumentsCount(1))
      return this.handleCodeTabCompletion(context.getArgument(0));
    else
      return List.of();
  }
}
