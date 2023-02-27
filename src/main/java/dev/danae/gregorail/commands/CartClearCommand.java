package dev.danae.gregorail.commands;

import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandUsageException;
import dev.danae.gregorail.util.location.InvalidLocationException;
import dev.danae.gregorail.util.location.LocationUtils;
import dev.danae.gregorail.util.minecart.MinecartUtils;
import java.util.List;
import net.md_5.bungee.api.chat.ComponentBuilder;


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
      
      // Send information about the updated cart
      context.sendMessage(new ComponentBuilder()
        .append(LocationUtils.formatEntity(cart), ComponentBuilder.FormatRetention.NONE)
        .append(" now has no code", ComponentBuilder.FormatRetention.NONE)
        .create());
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
