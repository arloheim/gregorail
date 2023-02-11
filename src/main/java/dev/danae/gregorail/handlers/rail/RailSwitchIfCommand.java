package dev.danae.gregorail.handlers.rail;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.commands.CommandContext;
import dev.danae.gregorail.commands.CommandException;
import dev.danae.gregorail.commands.CommandHandler;
import dev.danae.gregorail.commands.CommandUsageException;
import dev.danae.gregorail.handlers.CommandUtils;
import dev.danae.gregorail.util.location.LocationException;
import dev.danae.gregorail.util.location.LocationUtils;
import dev.danae.gregorail.util.minecart.MinecartUtils;
import dev.danae.gregorail.util.query.InvalidQueryException;
import dev.danae.gregorail.util.query.QueryUtils;
import org.bukkit.Material;
import org.bukkit.block.data.Rail;


public class RailSwitchIfCommand extends CommandHandler
{
  // Constructor
  public RailSwitchIfCommand(RailPlugin plugin)
  {
    super(plugin, "gregorail.rail.switchif");
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
      if (block.getType() != Material.RAIL)
        throw new CommandException(String.format("%s is not a rail block", LocationUtils.formatBlock(block)));
      
      var blockData = (Rail)block.getBlockData();
      if (!blockData.getShapes().contains(shape))
        throw new CommandException(String.format("%s cannot be set to shape %s", LocationUtils.formatBlock(block), shape));
      
      // Check for a minecart with the code at the location of the rail block
      var cart = MinecartUtils.findMinecartWithCodeMatch(block.getLocation(), query);
      if (cart != null)
      {
        // Set the shape of the block
        blockData.setShape(shape);
        block.setBlockData(blockData);
        
        // Send information about the updated block
        context.getSender().sendMessage(String.format("%s now has shape %s (code \"%s\")", LocationUtils.formatBlock(block), shape, MinecartUtils.getCode(cart)));
      }
      else
      {
        // Send information about the block
        context.getSender().sendMessage(String.format("%s still has its original shape", LocationUtils.formatBlock(block)));
      }
    }
    catch (LocationException | InvalidQueryException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
}
