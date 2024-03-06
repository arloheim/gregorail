package dev.danae.gregorail.plugin.commands.admin;

import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.commands.ManagerCommand;
import dev.danae.gregorail.plugin.commands.CommandContext;
import dev.danae.gregorail.plugin.commands.CommandException;
import dev.danae.gregorail.plugin.commands.CommandUsageException;
import dev.danae.gregorail.plugin.Formatter;
import dev.danae.gregorail.util.parser.ParserException;
import java.util.List;
import net.md_5.bungee.api.ChatColor;


public class AdminCodeSetCommand extends ManagerCommand
{
  // Constructor
  public AdminCodeSetCommand(Manager manager)
  {
    super(manager, "gregorail.admin");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {     
    try
    {
      // Validate the number of arguments
      if (!context.hasAtLeastArgumentsCount(2))
        throw new CommandUsageException();
      
      // Create a scanner for the arguments
      var scanner = context.getArgumentsScanner(this.getManager());
      
      // Parse the arguments
      var code = scanner.nextCode();
      
      var displayName = ChatColor.translateAlternateColorCodes('&', scanner.rest("string"));
      if (displayName.isBlank())
        throw new CommandException("No display name provided");
      
      // Set the display name of the code
      this.getManager().setDisplayName(code, displayName);
      
      // Send a message about the updated display name
      context.sendMessage(Formatter.formatAdminCodeDisplayNameChangedMessage(code, displayName));
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
    if (context.hasArgumentsCount(1))
      return this.handleCodeTabCompletion(context.getArgument(0));
    else
      return null;
  }
}
