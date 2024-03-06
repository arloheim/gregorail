package dev.danae.gregorail.plugin.commands.locate;

import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.commands.ManagerCommand;
import dev.danae.gregorail.plugin.commands.CommandContext;
import dev.danae.gregorail.plugin.commands.CommandException;
import dev.danae.gregorail.plugin.commands.CommandUsageException;
import dev.danae.gregorail.plugin.Formatter;
import dev.danae.gregorail.util.parser.ParserException;
import java.util.List;


public class LocateBlockCommand extends ManagerCommand
{
  // Constructor
  public LocateBlockCommand(Manager manager)
  {
    super(manager, "gregorail.locate.block");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {
    try
    {      
      // Assert that the command sender has a location
      var senderLocation = context.assertSenderHasLocation();
      
      // Create a scanner for the arguments
      var scanner = context.getArgumentsScanner(this.getManager());
      
      // Parse the properties
      var properties = scanner.wrapInPropertyBag();
      var radius = properties.getUnsignedInt("radius", this.getManager().getBlockSearchRadius());
      
      // Parse the arguments
      var block = scanner.nextLocation(senderLocation, radius).getBlock();
      
      // Send information about the block
      context.sendMessage(Formatter.formatBlock(block));
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
    if (context.hasAtLeastArgumentsCount(1))
      return this.handleLocationTabCompletion(context, 0);
    else
      return null;
  }
}
