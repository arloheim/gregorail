package dev.danae.gregorail.commands;

import dev.danae.gregorail.RailPlugin;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public abstract class CommandHandler implements CommandExecutor
{
  // Reference to the plugin
  protected final RailPlugin plugin;
  
  // The required permissions to execute this command
  protected final List<String> permissions;
  
  
  // Constructor
  public CommandHandler(RailPlugin plugin, List<String> permissions)
  {
    this.plugin = plugin;
    this.permissions = permissions;
  }
  public CommandHandler(RailPlugin plugin, String... permissions)
  {
    this(plugin, Arrays.asList(permissions));
  }
  public CommandHandler(RailPlugin plugin)
  {
    this(plugin, Collections.emptyList());
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
      // Create the command context
      var context = new CommandContext(command, args, sender);
      
      // Assert the permissions of the sender
      context.assertSenderHasPermissions(args);
      
      // Handle the command
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
