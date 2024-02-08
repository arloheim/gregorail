package dev.danae.gregorail.commands.rail;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.commands.CommandExecutionType;
import dev.danae.gregorail.commands.CommandUtils;
import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandHandler;
import dev.danae.gregorail.util.commands.CommandUsageException;
import dev.danae.gregorail.util.location.LocationUtils;
import dev.danae.gregorail.util.minecart.InvalidQueryException;
import dev.danae.gregorail.util.minecart.MinecartUtils;
import dev.danae.gregorail.util.minecart.QueryUtils;
import java.util.List;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.minecart.RideableMinecart;


public class RailSoundCommand extends CommandHandler
{
  // The execution type of the command
  private final CommandExecutionType executionType;
  
  
  // Constructor
  public RailSoundCommand(CommandExecutionType executionType)
  {
    super("gregorail.rail.sound");
    
    this.executionType = executionType;
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {
    try
    {      
      // Assert that the command sender has a location
      var senderLocation = context.assertSenderHasLocation();
      
      // Parse the properties
      var entityDistance = context.getPropertyAsUnsignedInt("distance", RailPlugin.getEntitySearchRadius());
      var volume = CommandUtils.clamp(context.getPropertyAsFloat("volume", 1.0f), 0.0f, 1.0f);
      var pitch = CommandUtils.clamp(context.getPropertyAsFloat("pitch", 1.0f), 0.0f, 2.0f);
      
      // Parse the arguments
      if (!context.hasAtLeastArgumentsCount(this.executionType == CommandExecutionType.CONDITIONAL ? 3 : 2))
        throw new CommandUsageException();
      
      var argumentIndex = 0;
      
      var query = this.executionType == CommandExecutionType.CONDITIONAL ? QueryUtils.parseQuery(context.getArgument(argumentIndex++)) : null;
      
      var sound = context.getArgument(argumentIndex++);
      if (NamespacedKey.fromString(sound) == null)
        throw new CommandException(String.format("\"%s\" is an invalid sound resource", sound));
      
      // Check if the minecart matches the query
      var cart = LocationUtils.findNearestEntity(senderLocation, RideableMinecart.class, entityDistance);
      if (query == null || (cart != null && MinecartUtils.matchCode(cart, query)))
      {
        // Play the sound
        senderLocation.getWorld().playSound(cart != null ? cart.getLocation() : senderLocation, sound, volume, pitch);
      }
    }
    catch (InvalidQueryException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
  
  // Handle tab completion of the command
  @Override
  public List<String> handleTabCompletion(CommandContext context)
  {
    if (context.getLastArgument().startsWith("#"))
      return CommandUtils.handlePropertyTabCompletion(context.getLastArgument(), "distance=", "volume=", "pitch=");
    
    switch (this.executionType)
    {
      case ALWAYS:
        if (context.hasAtLeastArgumentsCount(2))
          return CommandUtils.handleLocationTabCompletion(context, 1);
        else if (context.hasArgumentsCount(1))
          return CommandUtils.handleSoundTabCompletion(context.getArgument(0));
        else
          return null;
        
      case CONDITIONAL:
        if (context.hasAtLeastArgumentsCount(3))
          return CommandUtils.handleLocationTabCompletion(context, 2);
        else if (context.hasArgumentsCount(2))
          return CommandUtils.handleSoundTabCompletion(context.getArgument(1));
        else if (context.hasArgumentsCount(1))
          return CommandUtils.handleCodesTabCompletion(context.getArgument(0));
        else
          return null;
        
      default:
        return null;
    }
  }
}
