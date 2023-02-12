package dev.danae.gregorail.commands;

import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandHandler;
import dev.danae.gregorail.util.commands.CommandUsageException;
import dev.danae.gregorail.util.location.InvalidLocationException;
import dev.danae.gregorail.util.location.LocationUtils;
import dev.danae.gregorail.util.minecart.MinecartUtils;
import dev.danae.gregorail.util.query.InvalidQueryException;
import dev.danae.gregorail.util.query.QueryUtils;
import org.bukkit.entity.minecart.RideableMinecart;


public class RailBlockIfCommand extends CommandHandler
{
  // Constructor
  public RailBlockIfCommand()
  {
    super("gregorail.rail.blockif");
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
      
      var material = CommandUtils.parseMaterial(context.getArgument(1));
    
      var block = LocationUtils.parseBlockAtLocation(senderLocation, context.getJoinedArguments(2));
      if (block == null)
        throw new CommandException("No block found");
      
      // Check for a minecart with the code at the sender location
      var cart = LocationUtils.findNearestEntity(senderLocation, RideableMinecart.class);
      if (cart != null && MinecartUtils.matchCode(cart, query))
      {
        // Set the material of the block
        block.setType(material);
        
        // Send information about the updated block
        context.getSender().sendMessage(String.format("%s now has material %s (%s with code \"%s\")", LocationUtils.formatBlock(block), material, LocationUtils.formatEntity(cart), MinecartUtils.getCode(cart)));
      }
      else
      {
        // Send information about the block
        context.getSender().sendMessage(String.format("%s still has its original material (%s)", LocationUtils.formatBlock(block), LocationUtils.formatEntity(cart)));
      }
    }
    catch (InvalidLocationException | InvalidQueryException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
}
