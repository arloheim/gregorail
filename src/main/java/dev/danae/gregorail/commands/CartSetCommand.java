package dev.danae.gregorail.commands;

import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandUsageException;
import dev.danae.gregorail.util.location.InvalidLocationException;
import dev.danae.gregorail.util.location.LocationUtils;
import dev.danae.gregorail.util.minecart.InvalidMinecartCodeException;
import dev.danae.gregorail.util.minecart.MinecartUtils;


public class CartSetCommand extends AbstractCartCommand
{
  // Constructor
  public CartSetCommand()
  {
    super("gregorail.cart.set");
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
      
      // Send information about the updated cart
      context.getSender().sendMessage(String.format("%s now has code \"%s\"", LocationUtils.formatEntity(cart), code));
    }
    catch (InvalidLocationException | InvalidMinecartCodeException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
}
