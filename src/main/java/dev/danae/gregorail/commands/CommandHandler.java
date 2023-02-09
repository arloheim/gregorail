package dev.danae.gregorail.commands;

import dev.danae.gregorail.RailPlugin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public abstract class CommandHandler implements CommandExecutor
{
  // Reference to the plugin
  protected final RailPlugin plugin;
  
  
  // Constructor
  public CommandHandler(RailPlugin plugin)
  {
    this.plugin = plugin;
  }
  
  // Return the plugin
  public RailPlugin getPlugin()
  {
    return this.plugin;
  }
  

  // Handle a command
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
  {
    try
    {
      var context = new CommandHandlerContext(command, args, sender);
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
  public abstract void handle(CommandHandlerContext context) throws CommandException, CommandUsageException;
}
