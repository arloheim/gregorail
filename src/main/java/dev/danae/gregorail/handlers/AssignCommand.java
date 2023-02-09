package dev.danae.gregorail.handlers;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.commands.CommandContext;
import dev.danae.gregorail.commands.CommandException;
import dev.danae.gregorail.commands.CommandHandler;
import dev.danae.gregorail.commands.CommandUsageException;
import dev.danae.gregorail.location.LocationException;
import dev.danae.gregorail.location.LocationUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.persistence.PersistentDataType;


public class AssignCommand extends CommandHandler
{
  // Constructor
  public AssignCommand(RailPlugin plugin)
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
      if (!context.hasPermissions("gregorail.assign"))
        throw new CommandException("You have insufficient permissions to execute the command");
      
      // Assert that the command sender has a location
      var senderLocation = context.assertHasLocation();
    
      // Parse the arguments
      if (!context.hasAtLeastArgumentsCount(1))
        throw new CommandUsageException();
      
      var code = context.getArgument(0);
      
      var cart = this.getMinecart(context, senderLocation);
      if (cart == null)
        throw new CommandException("No cart found");
      
      // Assign the code to the cart
      cart.getPersistentDataContainer().set(this.plugin.getMinecartCodeKey(), PersistentDataType.STRING, code);
      cart.setCustomNameVisible(true);
      cart.setCustomName(code);
      
      // Send information about the updated cart
      context.getSender().sendMessage(String.format("%s has now code \"%s\"", LocationUtils.formatEntity(cart), code));
    }
    catch (LocationException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
  
  
  // Get a minecart based on the command context
  private RideableMinecart getMinecart(CommandContext context, Location senderLocation) throws LocationException
  {
    // If a location is specified, then get a minecart around that location
    if (context.hasAtLeastArgumentsCount(2))
      return LocationUtils.parseEntity(senderLocation, context.getJoinedArguments(1), RideableMinecart.class);
    
    // If the sender is a player and is riding a minecart, then use that minecart
    else if (context.getSender() instanceof Player player && player.getVehicle() instanceof RideableMinecart playerCart)
      return playerCart;
    
    // Otherwise get a minecart around the sender location
    else
       return LocationUtils.getEntity(senderLocation, RideableMinecart.class);
  }
}
