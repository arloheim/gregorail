package dev.danae.gregorail.commands;

import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandUsageException;
import dev.danae.gregorail.util.location.InvalidLocationException;
import dev.danae.gregorail.util.location.LocationUtils;
import dev.danae.gregorail.util.minecart.InvalidMinecartCodeException;
import dev.danae.gregorail.util.minecart.MinecartUtils;


public class CartClearCommand extends AbstractCartCommand
{
  // Constructor
  public CartClearCommand()
  {
    super("gregorail.cart.clear");
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
    catch (InvalidLocationException | InvalidMinecartCodeException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
}
