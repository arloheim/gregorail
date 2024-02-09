package dev.danae.gregorail.commands.cart;

import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandHandler;
import dev.danae.gregorail.util.location.LocationParser;
import dev.danae.gregorail.util.location.LocationUtils;
import dev.danae.gregorail.util.parser.ParserException;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.RideableMinecart;


public abstract class CartCommand extends CommandHandler
{
  // Constructor
  public CartCommand(List<String> permissions)
  {
    super(permissions);
  }
  public CartCommand(String... permissions)
  {
    super(permissions);
  }
  public CartCommand()
  {
    super();
  }
  
  
  // Find the minecart from the command
  public RideableMinecart findMinecart(CommandContext context, int argumentIndex, int radius) throws CommandException, ParserException
  {
    var senderLocation = context.assertSenderHasLocation();
    
    if (context.hasAtLeastArgumentsCount(argumentIndex + 1))
    {
      // Return the nearest minecart at the location in the argument
      var cartLocation = LocationParser.parseLocation(context.getJoinedArguments(argumentIndex), senderLocation, radius);
      if (cartLocation == null)
        throw new CommandException("No location found");
      
      return LocationUtils.findNearestEntity(cartLocation, RideableMinecart.class, radius);
    }
    else if (context.getSender() instanceof Player player && player.getVehicle() instanceof RideableMinecart playerCart)
    {
      // Return the cart the player is currently riding
      return playerCart;
    }
    else
    {
      // Return the nearest minecart at the sender location
      return LocationUtils.findNearestEntity(senderLocation, RideableMinecart.class, radius);
    }
  }
}
