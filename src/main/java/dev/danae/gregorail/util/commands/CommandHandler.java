package dev.danae.gregorail.util.commands;

import dev.danae.gregorail.RailPlugin;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;


public abstract class CommandHandler implements CommandExecutor, TabCompleter, Listener
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
  
  
  // Register the command handler as a listener
  public void registerListener()
  {
    Bukkit.getPluginManager().registerEvents(this, RailPlugin.getInstance());
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
      sender.spigot().sendMessage(new ComponentBuilder(ex.getMessage()).color(ChatColor.RED).create());
      return true;
    }
  }
  
  // Handle tab completion of a command
  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
  {
    var context = new CommandContext(command, args, sender);
    return this.handleTabCompletion(context);
  }
  
  
  // Actual function to handle a command
  public abstract void handle(CommandContext context) throws CommandException, CommandUsageException;
  
  // Actual function to handle tab completion of a command
  public List<String> handleTabCompletion(CommandContext context)
  {
    return null;
  }
}
