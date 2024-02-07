package dev.danae.gregorail.commands.locate;

import dev.danae.gregorail.commands.CommandUtils;
import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandHandler;
import dev.danae.gregorail.util.commands.CommandUsageException;
import dev.danae.gregorail.util.location.InvalidLocationException;
import dev.danae.gregorail.util.location.LocationUtils;
import java.util.List;
import org.bukkit.entity.minecart.RideableMinecart;


public class LocateCartCommand extends CommandHandler
{
  // Constructor
  public LocateCartCommand()
  {
    super("gregorail.locate.cart");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {
    try
    {      
      // Assert that the command sender has a location
      var senderLocation = context.assertSenderHasLocation();
    
      // Parse the arguments
      if (!context.hasAtLeastArgumentsCount(1))
        throw new CommandUsageException();
      
      var cartLocation = LocationUtils.parseLocation(senderLocation, context.getJoinedArguments());
      if (cartLocation == null)
        throw new CommandException("No location found");
      
      var cart = LocationUtils.findNearestEntity(cartLocation, RideableMinecart.class);
      if (cart == null)
        throw new CommandException("No cart found");
      
      // Send information about the cart
      context.sendMessage(LocationUtils.formatEntity(cart));
    }
    catch (InvalidLocationException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
  
  // Handle tab completion of the command
  @Override
  public List<String> handleTabCompletion(CommandContext context)
  {
    if (context.hasAtLeastArgumentsCount(1))
      return CommandUtils.handleLocationTabCompletion(context, 0);
    else
      return null;
  }
}
