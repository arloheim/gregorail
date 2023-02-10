package dev.danae.gregorail.commands;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;


public class CommandContext
{
  // The command that is being executed
  private final Command command;
  
  // The arguments of the command that is being executed
  private final String[] arguments;
  
  // The sender of the command that is being executed
  private final CommandSender sender;
  
  
  // Constructor
  public CommandContext(Command command, String[] arguments, CommandSender sender)
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
  public CommandContext withCommand(Command command)
  {
    return new CommandContext(command, this.arguments, this.sender);
  }
  
  // Return a context with new arguments
  public CommandContext withArguments(String[] arguments)
  {
    return new CommandContext(this.command, arguments, this.sender);
  }
  
  // Return a context with a new sender
  public CommandContext withSender(CommandSender sender)
  {
    return new CommandContext(this.command, this.arguments, sender);
  }
  
  
  // Return if the context has exactly the specified number of arguments
  public boolean hasArgumentsCount(int length)
  {
    return this.arguments.length == length;
  }
  
  // Return if the context has at least the specified number of arguments
  public boolean hasAtLeastArgumentsCount(int length)
  {
    return this.arguments.length >= length;
  }
  
  // Return if the context has at most the specified number of arguments
  public boolean hasAtMostArgumentsCount(int length)
  {
    return this.arguments.length <= length;
  }
  
  // Return a single argument
  public String getArgument(int index)
  {
    return this.arguments[index];
  }
  
  // Return multiple arguments joined by a single space starting at the specified index
  public String getJoinedArguments(int index)
  {
    return String.join(" ", Arrays.copyOfRange(this.arguments, index, this.arguments.length));
  }
  
  // Return all arguments joined by a single space
  public String getJoinedArguments()
  {
    return String.join(" ", this.arguments);
  }
  
  
  
  // Assert if the sender is a console sender
  public ConsoleCommandSender assertSenderIsConsole() throws CommandException
  {
    if (this.sender instanceof ConsoleCommandSender console)
      return console;
    else
      throw new CommandException("This command must be executed by the console");
  }  
  
  // Assert if the sender is a player sender
  public Player assertSenderIsPlayer() throws CommandException
  {
    if (this.sender instanceof Player player)
      return player;
    else
      throw new CommandException("This command must be executed by a player");
  }
  
  // Assert if the sender has a location
  public Location assertSenderHasLocation() throws CommandException
  {
    if (this.sender instanceof BlockCommandSender blockCommandSender)
      return blockCommandSender.getBlock().getLocation();
    if (this.sender instanceof Entity entity)
      return entity.getLocation();
    else
      throw new CommandException("This command must be executed by a block or entity");
  }
  
  // Assert if the sender has sufficient permissions
  public void assertSenderHasPermissions(List<String> permissions) throws CommandException
  {
    if (!permissions.stream().allMatch(p -> this.sender.hasPermission(p)))
      throw new CommandException("You don't have sufficient permission to execute this command");
  }
  public void assertSenderHasPermissions(String... permissions) throws CommandException
  {
    this.assertSenderHasPermissions(Arrays.asList(permissions));
  }
}
