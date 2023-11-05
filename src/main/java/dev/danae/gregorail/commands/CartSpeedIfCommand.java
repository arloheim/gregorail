package dev.danae.gregorail.commands;

import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandUsageException;
import dev.danae.gregorail.util.location.InvalidLocationException;
import dev.danae.gregorail.util.location.LocationUtils;
import dev.danae.gregorail.util.minecart.InvalidQueryException;
import dev.danae.gregorail.util.minecart.MinecartUtils;
import dev.danae.gregorail.util.minecart.QueryUtils;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;


public class CartSpeedIfCommand extends AbstractCartCommand
{  
  // Constructor
  public CartSpeedIfCommand()
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
      if (!context.hasAtLeastArgumentsCount(2))
        throw new CommandUsageException();
      
      var query = QueryUtils.parseQuery(context.getArgument(0));
     
      var speedMultiplier = CommandUtils.parseSpeedMultiplier(context.getArgument(1));
      
      var cart = this.findMinecart(context, 2);
      if (cart == null)
        throw new CommandException("No cart found");
      
      // Check if the minecart has the code
      if (MinecartUtils.matchCode(cart, query))
      {
        // Set the speed multiplier to the cart
        MinecartUtils.setSpeedMultiplier(cart, speedMultiplier);
      
        // Send information about the updated cart
        context.sendMessage(new ComponentBuilder()
          .append(LocationUtils.formatEntity(cart), ComponentBuilder.FormatRetention.NONE)
          .append(" now has speed multiplier ", ComponentBuilder.FormatRetention.NONE)
          .append(CommandUtils.formatSpeedMultiplier(speedMultiplier), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
          .create());
      }
      else
      {
        // Send information about the cart
        context.sendMessage(new ComponentBuilder()
          .append(LocationUtils.formatEntity(cart), ComponentBuilder.FormatRetention.NONE)
          .append(" still has original speed multiplier ", ComponentBuilder.FormatRetention.NONE)
          .append(CommandUtils.formatSpeedMultiplier(MinecartUtils.getSpeedMultiplier(cart)), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
          .create());
      }      
    }
    catch (InvalidLocationException | InvalidQueryException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
  
  // Handle tab completion of the command
  @Override
  public List<String> handleTabCompletion(CommandContext context)
  {
    if (context.hasAtLeastArgumentsCount(3))
      return CommandUtils.handleLocationTabCompletion(context, 2);
    else if (context.hasArgumentsCount(2))
      return CommandUtils.handleSpeedMultiplierTabCompletion(context.getArgument(1));
    else if (context.hasArgumentsCount(1))
      return CommandUtils.handleCodesTabCompletion(context.getArgument(0));
    else
      return null;
  }
}
