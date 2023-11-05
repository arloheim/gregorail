package dev.danae.gregorail.commands;

import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandUsageException;
import dev.danae.gregorail.util.location.InvalidLocationException;
import dev.danae.gregorail.util.location.LocationUtils;
import dev.danae.gregorail.util.minecart.MinecartUtils;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;


public class CartSpeedCommand extends AbstractCartCommand
{  
  // Constructor
  public CartSpeedCommand()
  {
    super("gregorail.cart.speed");
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
     
      var speedMultiplier = CommandUtils.parseSpeedMultiplier(context.getArgument(0));
      
      var cart = this.findMinecart(context, 1);
      if (cart == null)
        throw new CommandException("No cart found");
      
      // Set the speed multiplier to the cart
      MinecartUtils.setSpeedMultiplier(cart, speedMultiplier);
      
      // Send information about the updated cart
      context.sendMessage(new ComponentBuilder()
        .append(LocationUtils.formatEntity(cart), ComponentBuilder.FormatRetention.NONE)
        .append(" now has speed multiplier ", ComponentBuilder.FormatRetention.NONE)
        .append(CommandUtils.formatSpeedMultiplier(speedMultiplier), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
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
    if (context.hasAtLeastArgumentsCount(2))
      return CommandUtils.handleLocationTabCompletion(context, 1);
    else if (context.hasArgumentsCount(1))
      return CommandUtils.handleSpeedMultiplierTabCompletion(context.getArgument(0));
    else
      return null;
  }
}
