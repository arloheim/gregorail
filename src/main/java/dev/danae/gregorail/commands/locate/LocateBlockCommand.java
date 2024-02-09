package dev.danae.gregorail.commands.locate;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.commands.CommandUtils;
import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandHandler;
import dev.danae.gregorail.util.commands.CommandUsageException;
import dev.danae.gregorail.util.location.LocationParser;
import dev.danae.gregorail.util.location.LocationUtils;
import dev.danae.gregorail.util.parser.ParserException;
import java.util.List;


public class LocateBlockCommand extends CommandHandler
{
  // Constructor
  public LocateBlockCommand()
  {
    super("gregorail.locate.block");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {
    try
    {      
      // Assert that the command sender has a location
      var senderLocation = context.assertSenderHasLocation();
      
      // Parse the properties
      var blockDistance = context.getPropertyAsUnsignedInt("block-distance", RailPlugin.getBlockSearchRadius());
    
      // Parse the arguments
      if (!context.hasAtLeastArgumentsCount(1))
        throw new CommandUsageException();
      
      var block = LocationParser.parseBlockAtLocation(context.getJoinedArguments(), senderLocation, blockDistance);
      if (block == null)
        throw new CommandException("No location found");
      
      // Send information about the block
      context.sendMessage(LocationUtils.formatBlock(block));
    }
    catch (ParserException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
  
  // Handle tab completion of the command
  @Override
  public List<String> handleTabCompletion(CommandContext context)
  {
    if (context.getLastArgument().startsWith("#"))
      return CommandUtils.handlePropertyTabCompletion(context.getLastArgument(), "block-distance=");
    
    if (context.hasAtLeastArgumentsCount(1))
      return CommandUtils.handleLocationTabCompletion(context, 0);
    else
      return null;
  }
}
