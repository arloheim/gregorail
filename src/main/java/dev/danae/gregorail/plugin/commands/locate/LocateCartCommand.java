package dev.danae.gregorail.plugin.commands.locate;

import java.util.stream.Stream;
import dev.danae.common.commands.CommandContext;
import dev.danae.common.commands.CommandException;
import dev.danae.common.commands.CommandUsageException;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.commands.ManagerCommand;


public class LocateCartCommand extends ManagerCommand
{
  // Constructor
  public LocateCartCommand(Manager manager)
  {
    super(manager, "gregorail.locate.cart");
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
    var properties = this.getManager().getCartBlockPropertiesArgumentType("radius", "distance").parse(scanner);
    var radius = this.getManager().getBlockSearchRadiusProperty(properties, "radius");
    var distance = this.getManager().getCartSearchDistanceProperty(properties, "distance");
    
    // Parse the arguments
    var cart = this.getManager().findNearestCart(this.getManager().getLocationArgumentType(senderLocation, radius).parse(scanner), distance);
    
    // Send information about the cart
    context.sendMessage(this.getManager().formatMinecart(cart));
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
