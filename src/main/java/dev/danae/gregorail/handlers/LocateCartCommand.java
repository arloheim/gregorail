package dev.danae.gregorail.handlers;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.commands.CommandContext;
import dev.danae.gregorail.commands.CommandException;
import dev.danae.gregorail.commands.CommandHandler;
import dev.danae.gregorail.commands.CommandUsageException;
import dev.danae.gregorail.location.LocationException;
import dev.danae.gregorail.location.LocationUtils;
import dev.danae.gregorail.minecart.MinecartUtils;


public class LocateCartCommand extends CommandHandler
{
  // Constructor
  public LocateCartCommand(RailPlugin plugin)
  {
    super(plugin);
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {
    try
    {
      // Check for permissions
      context.assertSenderHasPermissions("gregorail.locatecart");
      
      // Assert that the command sender has a location
      var senderLocation = context.assertSenderHasLocation();
    
      // Parse the arguments
      if (!context.hasAtLeastArgumentsCount(1))
        throw new CommandUsageException();
      
      var cartLocation = LocationUtils.parseLocation(senderLocation, context.getJoinedArguments());
      if (cartLocation == null)
        throw new CommandException("No location found");
      
      var cart = MinecartUtils.findMinecart(cartLocation);
      if (cart == null)
        throw new CommandException("No cart found");
      
      // Send information about the cart
      context.getSender().sendMessage(LocationUtils.formatEntity(cart));
    }
    catch (LocationException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
}
