package dev.danae.gregorail.handlers.cart;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.commands.CommandContext;
import dev.danae.gregorail.commands.CommandException;
import dev.danae.gregorail.commands.CommandUsageException;
import dev.danae.gregorail.location.LocationException;
import dev.danae.gregorail.location.LocationUtils;
import dev.danae.gregorail.minecart.InvalidMinecartCodeException;
import dev.danae.gregorail.minecart.MinecartUtils;


public class CartSetCommand extends CartCommand
{
  // Constructor
  public CartSetCommand(RailPlugin plugin)
  {
    super(plugin, "gregorail.cart.set");
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
      if (!context.hasAtLeastArgumentsCount(1))
        throw new CommandUsageException();
      
      var code = context.getArgument(0);
      
      var cart = this.findMinecart(context, 1);
      if (cart == null)
        throw new CommandException("No cart found");
      
      // Assign the code to the cart
      MinecartUtils.setCode(cart, code);
      
      cart.setCustomNameVisible(true);
      cart.setCustomName(code);
      
      // Send information about the updated cart
      context.getSender().sendMessage(String.format("%s now has code \"%s\"", LocationUtils.formatEntity(cart), code));
    }
    catch (LocationException | InvalidMinecartCodeException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
}
