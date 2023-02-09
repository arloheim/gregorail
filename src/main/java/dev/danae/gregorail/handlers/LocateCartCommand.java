package dev.danae.gregorail.handlers;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.commands.CommandContext;
import dev.danae.gregorail.commands.CommandException;
import dev.danae.gregorail.commands.CommandHandler;
import dev.danae.gregorail.commands.CommandUsageException;
import dev.danae.gregorail.location.LocationException;
import dev.danae.gregorail.location.LocationUtils;
import org.bukkit.entity.minecart.RideableMinecart;


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
      // Assert that the command sender has a location
      var senderLocation = context.assertHasLocation();
    
      // Parse the arguments
      if (!context.hasAtLeastArgumentsCount(1))
        throw new CommandUsageException();
      
      var cart = LocationUtils.parseEntity(senderLocation, context.getJoinedArguments(), RideableMinecart.class);
      if (cart == null)
        throw new CommandException(String.format("No cart found for \"%s\"", context.getJoinedArguments()));
      
      // Send information about the cart
      context.getSender().sendMessage(LocationUtils.formatEntity(cart));
    }
    catch (LocationException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
}
