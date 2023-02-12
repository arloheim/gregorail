package dev.danae.gregorail.commands;

import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandHandler;
import dev.danae.gregorail.util.commands.CommandUsageException;
import dev.danae.gregorail.util.location.InvalidLocationException;
import dev.danae.gregorail.util.location.LocationUtils;
import dev.danae.gregorail.util.minecart.InvalidQueryException;
import dev.danae.gregorail.util.minecart.MinecartUtils;
import dev.danae.gregorail.util.minecart.QueryUtils;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.data.Rail;
import org.bukkit.entity.minecart.RideableMinecart;


public class RailSwitchIfCommand extends CommandHandler
{
  // Constructor
  public RailSwitchIfCommand()
  {
    super("gregorail.rail.switchif");
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
      if (!context.hasAtLeastArgumentsCount(3))
        throw new CommandUsageException();
      
      var query = QueryUtils.parseQuery(context.getArgument(0));
      
      var shape = CommandUtils.parseShape(context.getArgument(1));
    
      var block = LocationUtils.parseBlockAtLocation(senderLocation, context.getJoinedArguments(2));
      if (block == null)
        throw new CommandException("No block found");
      if (block.getType() != Material.RAIL)
        throw new CommandException(String.format("%s is not a rail block", LocationUtils.formatBlock(block)));
      
      var blockData = (Rail)block.getBlockData();
      if (!blockData.getShapes().contains(shape))
        throw new CommandException(String.format("%s cannot be set to shape %s", LocationUtils.formatBlock(block), shape));
      
      // Check for a minecart with the code at the sender location
      var cart = LocationUtils.findNearestEntity(senderLocation, RideableMinecart.class);
      if (cart != null && MinecartUtils.matchCode(cart, query))
      {
        // Set the shape of the block
        blockData.setShape(shape);
        block.setBlockData(blockData);
        
        // Send information about the updated block
        context.getSender().sendMessage(String.format("%s now has shape %s (%s with code \"%s\")", LocationUtils.formatBlock(block), shape, LocationUtils.formatEntity(cart), MinecartUtils.getCode(cart)));
      }
      else
      {
        // Send information about the block
        context.getSender().sendMessage(String.format("%s still has its original shape (%s)", LocationUtils.formatBlock(block), LocationUtils.formatEntity(cart)));
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
      return CommandUtils.handleShapeTabCompletion(context.getArgument(1));
    else
      return null;
  }
}
