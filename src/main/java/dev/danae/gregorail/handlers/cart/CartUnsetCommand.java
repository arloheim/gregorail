package dev.danae.gregorail.handlers.cart;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.commands.CommandContext;
import dev.danae.gregorail.commands.CommandException;
import dev.danae.gregorail.commands.CommandUsageException;
import dev.danae.gregorail.location.LocationException;
import dev.danae.gregorail.location.LocationUtils;
import dev.danae.gregorail.minecart.InvalidMinecartCodeException;
import dev.danae.gregorail.minecart.MinecartUtils;


public class CartUnsetCommand extends CartCommand
{
  // Constructor
  public CartUnsetCommand(RailPlugin plugin)
  {
    super(plugin, "gregorail.cart.unset");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {
    try
    {      
      // Assert that the command sender has a location
      context.assertSenderHasLocation();
    
      // Parse the arguments      
      var cart = this.findMinecart(context, 0);
      if (cart == null)
        throw new CommandException("No cart found");
      
      // Remove the code from the cart
      MinecartUtils.setCode(cart, null);
      
      cart.setCustomNameVisible(false);
      cart.setCustomName(null);
      
      // Send information about the updated cart
      context.getSender().sendMessage(String.format("%s now has no code", LocationUtils.formatEntity(cart)));
    }
    catch (LocationException | InvalidMinecartCodeException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
}
