package dev.danae.gregorail.handlers.admin;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.commands.CommandContext;
import dev.danae.gregorail.commands.CommandException;
import dev.danae.gregorail.commands.CommandHandler;
import dev.danae.gregorail.commands.CommandUsageException;
import dev.danae.gregorail.util.location.LocationException;
import dev.danae.gregorail.util.location.LocationUtils;


public class AdminLocateCommand extends CommandHandler
{
  // Constructor
  public AdminLocateCommand(RailPlugin plugin)
  {
    super(plugin, "gregorail.admin.locate");
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
      if (!context.hasAtLeastArgumentsCount(1))
        throw new CommandUsageException();
      
      var block = LocationUtils.parseBlockAtLocation(senderLocation, context.getJoinedArguments());
      if (block == null)
        throw new CommandException("No location found");
      
      // Send information about the block
      context.getSender().sendMessage(LocationUtils.formatBlock(block));
    }
    catch (LocationException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
}
