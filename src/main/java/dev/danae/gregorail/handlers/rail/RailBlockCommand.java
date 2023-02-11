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


public class RailBlockCommand extends CommandHandler
{
  // Constructor
  public RailBlockCommand(RailPlugin plugin)
  {
    super(plugin, "gregorail.rail.block");
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
      
      var material = CommandUtils.parseMaterial(context.getArgument(0));
    
      var block = LocationUtils.parseBlockAtLocation(senderLocation, context.getJoinedArguments(1));
      if (block == null)
        throw new CommandException("No block found");
      
      // Set the material of the block
      block.setType(material);
        
      // Send information about the updated block
      context.getSender().sendMessage(String.format("%s now has material %s", LocationUtils.formatBlock(block), material));
    }
    catch (LocationException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
}
