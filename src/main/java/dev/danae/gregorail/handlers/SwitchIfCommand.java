package dev.danae.gregorail.handlers;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.commands.CommandContext;
import dev.danae.gregorail.commands.CommandException;
import dev.danae.gregorail.commands.CommandHandler;
import dev.danae.gregorail.commands.CommandUsageException;
import dev.danae.gregorail.location.LocationException;
import dev.danae.gregorail.location.LocationUtils;
import dev.danae.gregorail.util.ArgumentUtils;
import org.bukkit.Material;
import org.bukkit.block.data.Rail;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.persistence.PersistentDataType;


public class SwitchIfCommand extends CommandHandler
{
  // Constructor
  public SwitchIfCommand(RailPlugin plugin)
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
      if (!context.hasPermissions("gregorail.switchif"))
        throw new CommandException("You have insufficient permissions to execute the command");
      
      // Assert that the command sender has a location
      var senderLocation = context.assertHasLocation();
      
      // Parse the arguments
      if (!context.hasAtLeastArgumentsCount(3))
        throw new CommandUsageException();
      
      var code = context.getArgument(0);
      
      var shape = ArgumentUtils.parseRailShape(context.getArgument(1));
      if (shape == null)
        throw new CommandException(String.format("The shape \"%s\" is an invalid rail shape", context.getArgument(0)));
    
      var block = LocationUtils.parseBlock(senderLocation, context.getJoinedArguments(2));
      if (block.getType() != Material.RAIL)
        throw new CommandException(String.format("The block at location [%d %d %d] is not a rail block", block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ()));
      
      var blockData = (Rail)block.getBlockData();
      if (!blockData.getShapes().contains(shape))
        throw new CommandException(String.format("The block at location [%d %d %d] cannot be set to shape %s", block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ(), shape));
      
      // Check for a minecart with the code at the location of the rail block
      var cart = LocationUtils.getEntity(block.getLocation(), RideableMinecart.class);
      if (cart != null && code.equals(cart.getPersistentDataContainer().get(this.plugin.getMinecartCodeKey(), PersistentDataType.STRING)))
      {
        // Set the shape of the block
        blockData.setShape(shape);
        block.setBlockData(blockData);
        
        // Send information about the updated block
        context.getSender().sendMessage(String.format("%s has now shape %s (code \"%s\")", LocationUtils.formatBlock(block), shape, code));
      }
      else
      {
        // Send information about the block
        context.getSender().sendMessage(String.format("%s has an unchanged shape (code \"%s\")", LocationUtils.formatBlock(block), code));
      }
    }
    catch (LocationException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
}
