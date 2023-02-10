package dev.danae.gregorail.handlers.rail;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.commands.CommandContext;
import dev.danae.gregorail.commands.CommandException;
import dev.danae.gregorail.commands.CommandHandler;
import dev.danae.gregorail.commands.CommandUsageException;
import dev.danae.gregorail.handlers.CommandUtils;
import dev.danae.gregorail.util.location.LocationException;
import dev.danae.gregorail.util.location.LocationUtils;
import org.bukkit.Material;
import org.bukkit.block.data.Rail;


public class RailSwitchCommand extends CommandHandler
{
  // Constructor
  public RailSwitchCommand(RailPlugin plugin)
  {
    super(plugin, "gregorail.rail.switch");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {
    try
    {      
      // Assert that the command sender has a location
      var senderLocation = context.assertSenderHasLocation();
      
      // Parse the arguments
      if (!context.hasAtLeastArgumentsCount(2))
        throw new CommandUsageException();
      
      var shape = CommandUtils.parseShape(context.getArgument(0));
      if (shape == null)
        throw new CommandException(String.format("Shape \"%s\" is an invalid rail shape", context.getArgument(0)));
    
      var block = LocationUtils.parseBlockAtLocation(senderLocation, context.getJoinedArguments(1));
      if (block.getType() != Material.RAIL)
        throw new CommandException(String.format("%s is not a rail block", LocationUtils.formatBlock(block)));
      
      var blockData = (Rail)block.getBlockData();
      if (!blockData.getShapes().contains(shape))
        throw new CommandException(String.format("%s cannot be set to shape %s", LocationUtils.formatBlock(block), shape));
      
      // Set the shape of the block
      blockData.setShape(shape);
      block.setBlockData(blockData);
        
      // Send information about the updated block
      context.getSender().sendMessage(String.format("%s now has shape %s", LocationUtils.formatBlock(block), shape));
    }
    catch (LocationException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
}
