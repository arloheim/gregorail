package dev.danae.gregorail.commands;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;


public class CommandHandlerContext
{
  // The command that is being executed
  private final Command command;
  
  // The arguments of the command that is being executed
  private final String[] arguments;
  
  // The sender of the command that is being executed
  private final CommandSender sender;
  
  
  // Constructor
  public CommandHandlerContext(Command command, String[] arguments, CommandSender sender)
  {
    this.command = command;
    this.arguments = arguments;
    this.sender = sender;
  }
  
  // Return the command that is being executed
  public Command getCommand()
  {
    return this.command;
  }
  
  // Return the arguments of the command that is being executed
  public String[] getArguments()
  {
    return this.arguments;
  }
  
  // Return the sender of the command that is being executed
  public CommandSender getSender()
  {
    return this.sender;
  }
  
  
  // Return a context with a new command
  public CommandHandlerContext withCommand(Command command)
  {
    return new CommandHandlerContext(command, this.arguments, this.sender);
  }
  
  // Return a context with new arguments
  public CommandHandlerContext withArguments(String[] arguments)
  {
    return new CommandHandlerContext(this.command, arguments, this.sender);
  }
  
  // Return a context with a new sender
  public CommandHandlerContext withSender(CommandSender sender)
  {
    return new CommandHandlerContext(this.command, this.arguments, sender);
  }
  
  
  // Check if the number of arguments matches
  public boolean checkArgumentsLength(int length)
  {
    return this.arguments.length == length;
  }
  
  // Return a single argument of the command that is being executed
  public String getArgument(int index)
  {
    return this.arguments[index];
  }
  
  // Return the arguments joined by a single space
  public String getJoinedArguments()
  {
    return String.join(" ", this.arguments);
  }
  
  
  // Check if the sender is a console
  public ConsoleCommandSender checkSenderIsConsole()
  {
    if (this.sender instanceof ConsoleCommandSender console)
      return console;
    return null;
  }
  public ConsoleCommandSender assertSenderIsConsole() throws CommandException
  {
    var console = this.checkSenderIsConsole();
    if (console == null)
      throw new CommandException("This command must be executed by the console");
    return console;
  }  
  
  // Check if the sender is a player
  public Player checkSenderIsPlayer()
  {
    if (this.sender instanceof Player player)
      return player;
    return null;
  }
  public Player assertSenderIsPlayer() throws CommandException
  {
    var player = this.checkSenderIsPlayer();
    if (player == null)
      throw new CommandException("This command must be executed by a player");
    return player;
  }
  
  // Check if the sender has a location
  public Location checkSenderHasLocation()
  {
    if (this.sender instanceof BlockCommandSender blockCommandSender)
      return blockCommandSender.getBlock().getLocation();
    if (this.sender instanceof Entity entity)
      return entity.getLocation();
    else
      return null;
  }
  public Location assertSenderHasLocation() throws CommandException
  {
    var location = this.checkSenderHasLocation();
    if (location == null)
      throw new CommandException("This command must be executed by a block or entity");
    return location;
  }

  
  // Check if the sender has sufficient permissions
  public boolean checkPermissions(List<String> permissions)
  {
    return permissions.stream().allMatch(p -> this.sender.hasPermission(p));
  }
  public boolean checkPermissions(String... permissions)
  {
    return this.checkPermissions(Arrays.asList(permissions));
  }
  public void assertPermissions(List<String> permissions) throws CommandException
  {
    if (!this.checkPermissions(permissions))
      throw new CommandException("You don't have sufficient permission to execute this command");
  }
  public void assertPermissions(String... permissions) throws CommandException
  {
    this.assertPermissions(Arrays.asList(permissions));
  }
}
