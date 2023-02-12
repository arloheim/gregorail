package dev.danae.gregorail.commands;

import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandHandler;
import dev.danae.gregorail.util.location.InvalidLocationException;
import dev.danae.gregorail.util.location.LocationUtils;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.RideableMinecart;


public abstract class AbstractCartCommand extends CommandHandler
{
  // Constructor
  public AbstractCartCommand(List<String> permissions)
  {
    super(permissions);
  }
  public AbstractCartCommand(String... permissions)
  {
    super(permissions);
  }
  public AbstractCartCommand()
  {
    super();
  }
  
  
  // Find the minecart from the command
  public RideableMinecart findMinecart(CommandContext context, int argumentIndex) throws CommandException, InvalidLocationException
  {
    var senderLocation = context.assertSenderHasLocation();
    
    if (context.hasAtLeastArgumentsCount(argumentIndex + 1))
    {
      // Return the nearest minecart at the location in the argument
      var cartLocation = LocationUtils.parseLocation(senderLocation, context.getJoinedArguments(argumentIndex));
      if (cartLocation == null)
        throw new CommandException("No location found");
      
      return LocationUtils.findNearestEntity(cartLocation, RideableMinecart.class);
    }
    else if (context.getSender() instanceof Player player && player.getVehicle() instanceof RideableMinecart playerCart)
    {
      // Return the cart the player is currently riding
      return playerCart;
    }
    else
    {
      // Return the nearest minecart at the sender location
      return LocationUtils.findNearestEntity(senderLocation, RideableMinecart.class);
    }
  }
}
