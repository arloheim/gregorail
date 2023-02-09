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
        
      // Send information about the location
      var block = location.getWorld().getBlockAt(location);
      context.getSender().sendMessage(String.format("Found location [%d %d %d], block %s", location.getBlockX(), location.getBlockY(), location.getBlockZ(), block.getType().name()));
    }
    catch (LocationException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
}
