package dev.danae.gregorail.commands.admin;

import dev.danae.gregorail.commands.CommandUtils;
import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandHandler;
import dev.danae.gregorail.util.commands.CommandUsageException;
import dev.danae.gregorail.util.minecart.CodeUtils;
import dev.danae.gregorail.util.minecart.InvalidCodeException;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;


public class AdminCodeRemoveCommand extends CommandHandler
{
  // Constructor
  public AdminCodeRemoveCommand()
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
      if (!context.hasArgumentsCount(1))
        throw new CommandUsageException();
      
      var code = CodeUtils.createCode(context.getArgument(0));
      
      // Remove the display name of the code
      CodeUtils.removeDisplayName(code);
      
      // Send information about the updated code
      context.sendMessage(new ComponentBuilder()
        .append("The display name for code ", ComponentBuilder.FormatRetention.NONE)
        .append(code.toString(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
        .append(" has been removed", ComponentBuilder.FormatRetention.NONE)
        .create());
    }
    catch (InvalidCodeException ex)
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
