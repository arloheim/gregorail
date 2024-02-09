package dev.danae.gregorail.commands.admin;

import dev.danae.gregorail.commands.CommandUtils;
import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandHandler;
import dev.danae.gregorail.util.commands.CommandUsageException;
import dev.danae.gregorail.util.minecart.CodeUtils;
import dev.danae.gregorail.util.parser.ParserException;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;


public class AdminCodeSetCommand extends CommandHandler
{
  // Constructor
  public AdminCodeSetCommand()
  {
    super("gregorail.admin");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {     
    try
    {
      // Parse the arguments
      if (!context.hasAtLeastArgumentsCount(2))
        throw new CommandUsageException();
      
      var code = CodeUtils.createCode(context.getArgument(0));
      
      var displayName = context.getJoinedArguments(1);
      if (displayName.isBlank())
        throw new CommandException("No display name provided");
      displayName = ChatColor.translateAlternateColorCodes('&', displayName);
      
      // Set the display name of the code
      CodeUtils.setDisplayName(code, displayName);
      
      // Send information about the updated code
      context.sendMessage(new ComponentBuilder()
        .append("The display name for code ", ComponentBuilder.FormatRetention.NONE)
        .append(code.toString()).color(ChatColor.GREEN)
        .append(" is now \"", ComponentBuilder.FormatRetention.NONE)
        .append(TextComponent.fromLegacyText(displayName), ComponentBuilder.FormatRetention.NONE)
        .append("\"", ComponentBuilder.FormatRetention.NONE)
        .create());
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
      return CommandUtils.handleCodeTabCompletion(context.getArgument(0));
    else
      return null;
  }
}
