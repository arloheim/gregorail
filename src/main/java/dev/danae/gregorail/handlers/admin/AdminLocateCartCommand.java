package dev.danae.gregorail.handlers.admin;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.commands.CommandContext;
import dev.danae.gregorail.commands.CommandException;
import dev.danae.gregorail.commands.CommandHandler;
import dev.danae.gregorail.commands.CommandUsageException;
import dev.danae.gregorail.util.location.LocationException;
import dev.danae.gregorail.util.location.LocationUtils;
import dev.danae.gregorail.util.minecart.MinecartUtils;


public class AdminLocateCartCommand extends CommandHandler
{
  // Constructor
  public AdminLocateCartCommand(RailPlugin plugin)
  {
    super(plugin, "gregorail.admin.locatecart");
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
