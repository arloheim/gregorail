package dev.danae.gregorail.plugin.commands.tag;

import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.commands.ManagerCommand;
import dev.danae.gregorail.plugin.Formatter;
import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandUsageException;
import dev.danae.gregorail.util.parser.ParserException;
import java.util.List;
import net.md_5.bungee.api.ChatColor;


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
    try
    {
      // Validate the number of arguments
      if (!context.hasAtLeastArgumentsCount(3))
        throw new CommandUsageException();
      
      // Create a scanner for the arguments
      var scanner = context.getArgumentsScanner(this.getManager());
      
      // Parse the arguments
      var code = scanner.nextCode();
      var propertyName = scanner.nextIdentifier();
      var propertyValue = scanner.rest("value");

      // Set the property of the code tag
      if (propertyName.equals("name"))
      {
        // Set the name of the code tag
        var name = ChatColor.translateAlternateColorCodes('&', propertyValue);
        this.getManager().setCodeTag(code, codeTag -> codeTag.withName(name));
      
        // Send a message about the updated name of the code tag
        context.sendMessage(Formatter.formatTagNameChangedMessage(code, name));
      }
      else if (propertyName.equals("url"))
      {
        // Set the URL of the code tag
        var url = propertyValue;
        this.getManager().setCodeTag(code, codeTag -> codeTag.withUrl(url));
      
        // Send a message about the updated URL of the code tag
        context.sendMessage(Formatter.formatTagUrlChangedMessage(code, url));
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
