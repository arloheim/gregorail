package dev.danae.gregorail.plugin.commands.locate;

import java.util.List;
import dev.danae.common.commands.CommandContext;
import dev.danae.common.commands.CommandException;
import dev.danae.common.commands.CommandUsageException;
import dev.danae.common.commands.arguments.ArgumentType;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.commands.ManagerCommand;
import dev.danae.gregorail.plugin.Formatter;


public class LocateBlockCommand extends ManagerCommand
{
  // Constructor
  public LocateBlockCommand(Manager manager)
  {
    super(manager, "gregorail.locate.block");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException
  {
    try
    {      
      // Assert that the command sender has a location
      var senderLocation = context.assertSenderHasLocation();
      
      // Create a scanner for the arguments
      var scanner = context.getArgumentsScanner();
      
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
      return this.handleLocationTabCompletion(context, 0, false);
    else
      return List.of();
  }
}
