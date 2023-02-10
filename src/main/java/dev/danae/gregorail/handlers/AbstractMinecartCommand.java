package dev.danae.gregorail.handlers;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.commands.CommandContext;
import dev.danae.gregorail.commands.CommandException;
import dev.danae.gregorail.commands.CommandHandler;
import dev.danae.gregorail.location.LocationException;
import dev.danae.gregorail.location.LocationUtils;
import dev.danae.gregorail.minecart.MinecartUtils;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.RideableMinecart;


public abstract class AbstractMinecartCommand extends CommandHandler
{
  // Constructor
  public AbstractMinecartCommand(RailPlugin plugin)
  {
    super(plugin);
  }
  
  
  // Find the minecart from the command
  public RideableMinecart findMinecart(CommandContext context, int argumentIndex) throws CommandException, LocationException
  {
    var senderLocation = context.assertSenderHasLocation();
    
    if (context.hasAtLeastArgumentsCount(argumentIndex + 1))
    {
      // Return the nearest minecart at the location in the argument
      var cartLocation = LocationUtils.parseLocation(senderLocation, context.getJoinedArguments(argumentIndex));
      if (cartLocation == null)
        throw new CommandException("No location found");
      
      var cart = MinecartUtils.findMinecart(cartLocation);
      if (cart == null)
        throw new CommandException("No cart found");
        
      return cart;
    }
    else if (context.getSender() instanceof Player player && player.getVehicle() instanceof RideableMinecart playerCart)
    {
      // Return the cart the player is currently riding
      return playerCart;
    }
    else
    {
      // Return the nearest minecart at the sender location
      return MinecartUtils.findMinecart(senderLocation);
    }
  }
}
