package dev.danae.gregorail.util.commands;

import dev.danae.gregorail.util.location.LocationUtils;
import java.util.Arrays;
import java.util.List;
import net.md_5.bungee.api.chat.BaseComponent;
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
  
  // Return an iterator over the arguments of the context
  public CommandArgumentsIterator getArgumentsIterator()
  {
    return new CommandArgumentsIterator(this.arguments);
  }
  
  // Return the argument of the context with the specified index
  public String getArgument(int index)
  {
    return this.arguments[index];
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
  
  // Return the sender of the command that is being executed
  public CommandSender getSender()
  {
    return this.sender;
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
  
  
  // Return the nearest player to the sender
  public Player nearestPlayer(int radius) throws CommandException
  {
    var senderLocation = this.assertSenderHasLocation();
    return LocationUtils.findNearestEntity(senderLocation, Player.class, radius);
  }
  
  // Return the nearest player to the sender, or the sender itself if it is a player
  public Player nearestPlayerOrSender(int radius) throws CommandException
  {
    if (this.sender instanceof Player player)
      return player;
    else
      return this.nearestPlayer(radius);
  }
  
  
  // Send a message to the sender of the command
  public void sendMessage(String message)
  {
    this.sender.sendMessage(message);
  }
  public void sendMessage(String... messages)
  {
    this.sender.sendMessage(messages);
  }
  
  // Send a message using text components to the sender of the command
  public void sendMessage(BaseComponent component)
  {
    this.sender.spigot().sendMessage(component);
  }
  public void sendMessage(BaseComponent... components)
  {
    this.sender.spigot().sendMessage(components);
  }
  
  
  // Slice the arguments of the context with the specified range
  public CommandContext sliceArguments(int from, int to)
  {
    return new CommandContext(this.command, Arrays.copyOfRange(this.arguments, from, to), this.sender);
  }
  
  // Slice the arguments of the context with the specified start
  public CommandContext sliceArguments(int from)
  {
    return this.sliceArguments(from, this.arguments.length);
  }
}
