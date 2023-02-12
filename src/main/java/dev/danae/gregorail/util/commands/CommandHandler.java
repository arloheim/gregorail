package dev.danae.gregorail.util.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public abstract class CommandHandler implements CommandExecutor
{  
  // The required permissions to execute this command
  protected final List<String> permissions;
  
  
  // Constructor
  public CommandHandler(List<String> permissions)
  {
    this.permissions = permissions;
  }
  public CommandHandler(String... permissions)
  {
    this(Arrays.asList(permissions));
  }
  public CommandHandler()
  {
    this(Collections.emptyList());
  }
  

  // Handle a command
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
  {
    try
    {
      var context = new CommandContext(command, args, sender);
      context.assertSenderHasPermissions(this.permissions);
      this.handle(context);
      
      return true;
    }
    catch (CommandUsageException ex)
    {
      return false;
    }
    catch (CommandException ex)
    {
      sender.sendMessage(ChatColor.RED + ex.getMessage() + ChatColor.RESET);
      return true;
    }
  }
  
  // Actual function to handle a command
  public abstract void handle(CommandContext context) throws CommandException, CommandUsageException;
}
