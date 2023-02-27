package dev.danae.gregorail.commands;

import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandHandler;
import dev.danae.gregorail.util.commands.CommandUsageException;
import dev.danae.gregorail.util.location.InvalidLocationException;
import dev.danae.gregorail.util.location.LocationUtils;
import java.util.EnumSet;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.data.Rail;


public class RailSwitchCommand extends CommandHandler
{
  // Constructor
  public RailSwitchCommand()
  {
    super("gregorail.rail.switch");
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
    
      var block = LocationUtils.parseBlockAtLocation(senderLocation, context.getJoinedArguments(1));
      if (block == null)
        throw new CommandException("No block found");
      if (!EnumSet.of(Material.RAIL, Material.POWERED_RAIL, Material.DETECTOR_RAIL, Material.ACTIVATOR_RAIL).contains(block.getType()))
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
      return CommandUtils.handleShapeTabCompletion(context.getArgument(0));
    else
      return null;
  }
}
