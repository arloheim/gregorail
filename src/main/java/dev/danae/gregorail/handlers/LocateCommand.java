package dev.danae.gregorail.handlers;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.commands.CommandException;
import dev.danae.gregorail.commands.CommandHandler;
import dev.danae.gregorail.commands.CommandHandlerContext;
import dev.danae.gregorail.commands.CommandUsageException;
import dev.danae.gregorail.location.LocationException;
import dev.danae.gregorail.location.LocationParser;


public class LocateCommand extends CommandHandler
{
  // Constructor
  public LocateCommand(RailPlugin plugin)
  {
    super(plugin);
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandHandlerContext context) throws CommandException, CommandUsageException
  {
    try
    {
      // Assert that the command sender has a location
      var senderLocation = context.assertSenderHasLocation();
    
      // Parse the location
      var string = context.getJoinedArguments();
      var location = LocationParser.parse(senderLocation, string);
      if (location == null)
        throw new CommandException("No location found");
      
      // Send information about the location and block
      context.getSender().sendMessage(String.format("Block %s at [%d %d %d]", 
        location.getBlock().getType().name(),
        location.getBlockX(), location.getBlockY(), location.getBlockZ()));
    }
    catch (LocationException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
}
