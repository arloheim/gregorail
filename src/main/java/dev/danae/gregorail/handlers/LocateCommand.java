package dev.danae.gregorail.handlers;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.commands.CommandContext;
import dev.danae.gregorail.commands.CommandException;
import dev.danae.gregorail.commands.CommandHandler;
import dev.danae.gregorail.commands.CommandUsageException;
import dev.danae.gregorail.location.LocationException;
import dev.danae.gregorail.location.LocationUtils;


public class LocateCommand extends CommandHandler
{
  // Constructor
  public LocateCommand(RailPlugin plugin)
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
      if (!context.hasPermissions("gregorail.locate"))
        throw new CommandException("You have insufficient permissions to execute the command");
      
      // Assert that the command sender has a location
      var senderLocation = context.assertHasLocation();
    
      // Parse the arguments
      if (!context.hasAtLeastArgumentsCount(1))
        throw new CommandUsageException();
      
      var block = LocationUtils.parseBlock(senderLocation, context.getJoinedArguments());
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
