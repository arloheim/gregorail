package dev.danae.gregorail.util.commands;

import dev.danae.gregorail.util.location.LocationUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
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
  // Patterns for parsing properties
  private static final Pattern propertyPattern = Pattern.compile("#(?<key>[a-z][a-z0-9-]*)(?:=(?<value>.+))?", Pattern.CASE_INSENSITIVE);
  
  
  // The command that is being executed
  private final Command command;
  
  // The arguments of the command that is being executed
  private final String[] arguments;
  
  // The properties of the command that is being executed
  private final Map<String, String> properties;
  
  // The sender of the command that is being executed
  private final CommandSender sender;
  
  
  // Constructor
  private CommandContext(Command command, String[] arguments, Map<String, String> properties, CommandSender sender)
  {
    this.command = command;
    this.arguments = arguments;
    this.properties = properties;
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
  
  // Return the properties of the command that is being executed
  public Map<String, String> getProperties()
  {
    return this.properties;
  }
  
  // Return the sender of the command that is being executed
  public CommandSender getSender()
  {
    return this.sender;
  }
  
  
  // Return a context with a new command
  public CommandContext withCommand(Command command)
  {
    return new CommandContext(command, this.arguments, this.properties, this.sender);
  }
  
  // Return a context with new arguments
  public CommandContext withArguments(String[] arguments)
  {
    return new CommandContext(this.command, arguments, this.properties, this.sender);
  }
  
  // Return a context with new properties
  public CommandContext withProperties(Map<String, String> properties)
  {
    return new CommandContext(this.command, arguments, properties, this.sender);
  }
  
  // Return a context with a new sender
  public CommandContext withSender(CommandSender sender)
  {
    return new CommandContext(this.command, this.arguments, this.properties, sender);
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
  
  // Return the last argument
  public String getLastArgument()
  {
    return this.arguments[this.arguments.length - 1];
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
  
  
  // Return if the context contains a property with the specified key
  public boolean hasProperty(String key)
  {
    return this.properties.containsKey(key);
  }
  
  // Return a single property
  public String getProperty(String key)
  {
    return this.properties.get(key);
  }
  
  // Return a single property with a default value if it doesn't exist
  public String getProperty(String key, String defaultValue)
  {
    return this.properties.getOrDefault(key, defaultValue);
  }
  
  // Return a single boolean property with a default value if it doesn't exist or can't be parsed
  public boolean getPropertyAsBoolean(String key, boolean defaultValue)
  {
    var value = this.getProperty(key, null);
    return value != null ? Boolean.parseBoolean(key) : defaultValue;
  }
  
  // Return a single integer property with a default value if it doesn't exist
  public int getPropertyAsInt(String key, int defaultValue) throws CommandException
  {
    try
    {
      var value = this.getProperty(key, null);
      return value != null ? Integer.parseInt(value) : defaultValue;
    }
    catch (NumberFormatException ex)
    {
      throw new CommandException(String.format("Invalid number format for property %s", key), ex);
    }
  }
  
  // Return a single unsigned integer property with a default value if it doesn't exist
  public int getPropertyAsUnsignedInt(String key, int defaultValue) throws CommandException
  {
    try
    {
      var value = this.getProperty(key, null);
      return value != null ? Integer.parseUnsignedInt(value) : defaultValue;
    }
    catch (NumberFormatException ex)
    {
      throw new CommandException(String.format("Invalid number format for property %s", key), ex);
    }
  }
  
  // Return a single float property with a default value if it doesn't exist
  public float getPropertyAsFloat(String key, float defaultValue) throws CommandException
  {
    try
    {
      var value = this.getProperty(key, null);
      return value != null ? Float.parseFloat(key) : defaultValue;
    }
    catch (NumberFormatException ex)
    {
      throw new CommandException(String.format("Invalid number format for property %s", key), ex);
    }
  }
  
  // Return a single double property with a default value if it doesn't exist
  public double getPropertyAsDouble(String key, double defaultValue) throws CommandException
  {
    try
    {
      var value = this.getProperty(key, null);
      return value != null ? Double.parseDouble(key) : defaultValue;
    }
    catch (NumberFormatException ex)
    {
      throw new CommandException(String.format("Invalid number format for property %s", key), ex);
    }
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
  
  
  // Parse a command context
  public static CommandContext parse(Command command, String[] rawArguments, CommandSender sender)
  {
    var arguments = new LinkedList<String>();
    var properties = new HashMap<String, String>();
    
    for (var rawArgument : rawArguments)
    {
      var matcher = propertyPattern.matcher(rawArgument);
      if (matcher.matches())
        properties.put(matcher.group("key"), matcher.group("value"));
      else
        arguments.add(rawArgument);
    }
    
    return new CommandContext(command, arguments.toArray(new String[0]), properties, sender);
  }
}
