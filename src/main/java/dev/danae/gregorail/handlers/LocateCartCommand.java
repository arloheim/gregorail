package dev.danae.gregorail.handlers;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.commands.CommandException;
import dev.danae.gregorail.commands.CommandHandler;
import dev.danae.gregorail.commands.CommandHandlerContext;
import dev.danae.gregorail.commands.CommandUsageException;
import dev.danae.gregorail.location.Cuboid;
import dev.danae.gregorail.location.LocationException;
import dev.danae.gregorail.location.LocationParser;
import org.bukkit.entity.Minecart;


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
    
      // Parse the location
      var string = context.getJoinedArguments();
      var location = LocationParser.parse(senderLocation, string);
      if (location == null)
        throw new CommandException("No location found");
      
      // Get the nearest cart at the location
      var cart = Cuboid.of(location, 10).findNearestEntityToCenter(Minecart.class);
      if (cart == null)
        throw new CommandException("No cart found");
      
      // TODO: Glow the cart for an instant
        
      // Send information about the location and cart
      context.getSender().sendMessage(String.format("Location [%d %d %d], Cart \"%s\" at [%d %d %d]", 
        location.getBlockX(), location.getBlockY(), location.getBlockZ(), 
        cart.isCustomNameVisible() ? cart.getCustomName() : cart.getName(),
        cart.getLocation().getBlockX(), cart.getLocation().getBlockY(), cart.getLocation().getBlockZ()));
    }
    catch (LocationException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
}
