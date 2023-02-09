package dev.danae.gregorail.handlers;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.commands.CommandException;
import dev.danae.gregorail.commands.CommandHandler;
import dev.danae.gregorail.commands.CommandHandlerContext;
import dev.danae.gregorail.commands.CommandUsageException;
import dev.danae.gregorail.location.LocationException;
import dev.danae.gregorail.location.LocationParser;
import org.bukkit.entity.minecart.RideableMinecart;


public class LocateCartCommand extends CommandHandler
{
  // Constructor
  public LocateCartCommand(RailPlugin plugin)
  {
    super(plugin);
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandHandlerContext context) throws CommandException, CommandUsageException
  {
    try
    {
      // Assert that the command sender has a location
      var senderLocation = context.assertSenderHasLocation();
    
      // Parse the cart
      var string = context.getJoinedArguments();
      var cart = LocationParser.parseEntity(senderLocation, string, 10, RideableMinecart.class);
      if (cart == null)
        throw new CommandException("No cart found");
        
      // Send information about the location and cart
      context.getSender().sendMessage(String.format("Cart \"%s\" at [%d %d %d]",
        cart.getCustomName() != null ? cart.getCustomName() : cart.getName(),
        cart.getLocation().getBlockX(), cart.getLocation().getBlockY(), cart.getLocation().getBlockZ()));
    }
    catch (LocationException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
}
