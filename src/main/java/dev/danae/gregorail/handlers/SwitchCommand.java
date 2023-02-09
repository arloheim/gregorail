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


public class SwitchCommand extends CommandHandler
{
  // Constructor
  public SwitchCommand(RailPlugin plugin)
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
      if (!context.hasAtLeastArgumentsCount(2))
        throw new CommandUsageException();
      
      var shape = ArgumentUtils.parseRailShape(context.getArgument(0));
      if (shape == null)
        throw new CommandException(String.format("The shape \"%s\" is an invalid rail shape", context.getArgument(0)));
    
      var block = LocationUtils.parseBlock(senderLocation, context.getJoinedArguments(1));
      if (block.getType() != Material.RAIL)
        throw new CommandException(String.format("The block at location [%d %d %d] is not a rail block", block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ()));
      
      var blockData = (Rail)block.getBlockData();
      if (!blockData.getShapes().contains(shape))
        throw new CommandException(String.format("The block at location [%d %d %d] cannot be set to shape %s", block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ(), shape));
      
      // Set the shape of the block
      blockData.setShape(shape);
      block.setBlockData(blockData);
        
      // Send information about the updated block
      context.getSender().sendMessage(String.format("%s has now shape %s", LocationUtils.formatBlock(block), shape));
    }
    catch (LocationException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
}
