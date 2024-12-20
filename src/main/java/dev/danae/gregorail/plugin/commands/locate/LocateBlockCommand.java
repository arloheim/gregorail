package dev.danae.gregorail.plugin.commands.locate;

import java.util.Map;
import java.util.stream.Stream;
import dev.danae.common.commands.CommandContext;
import dev.danae.common.commands.CommandException;
import dev.danae.common.commands.CommandUsageException;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.commands.ManagerCommand;


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
    // Assert that the command sender has a location
    var senderLocation = context.assertSenderHasLocation();
    
    // Create a scanner for the arguments
    var scanner = context.getArgumentsScanner();
    
    // Parse the properties
    var properties = this.getManager().getBlockPropertiesArgumentType("radius").parse(scanner);
    var radius = this.getManager().getBlockSearchRadiusProperty(properties, "radius");
    
    // Parse the arguments
    var block = this.getManager().getLocationArgumentType(senderLocation, radius).parse(scanner).getBlock();
    
    // Send information about the block
    if (block != null)
      context.sendMessage(this.getManager().formatMessage("block-found", Map.of("block", block)));
    else
      context.sendMessage(this.getManager().formatMessage("block-not-found"));
  }
  
  // Return suggestions for the specified command context
  @Override
  public Stream<String> suggest(CommandContext context)
  {    
    if (context.hasAtLeastArgumentsCount(1))
    return this.getManager().getLocationArgumentType(null, 0).suggest(context, 0);
    else
      return Stream.empty();
  }
}
