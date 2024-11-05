package dev.danae.gregorail.plugin.commands.cart;

import java.util.Map;
import java.util.stream.Stream;
import dev.danae.common.commands.CommandContext;
import dev.danae.common.commands.CommandException;
import dev.danae.common.commands.CommandUsageException;
import dev.danae.gregorail.model.Code;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.commands.QueryType;
import dev.danae.gregorail.plugin.commands.QueryMatcherCommand;


public class CartSetCommand extends QueryMatcherCommand<Code>
{
  // Constructor
  public CartSetCommand(Manager manager, QueryType type)
  {
    super(manager, type, manager.getCodeQueryMatcherArgumentType(), "gregorail.cart.set");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {  
    // Assert that the command sender has a location
    var senderLocation = context.assertSenderHasLocation();
    
    // Validate the number of arguments
    if (!context.hasAtLeastArgumentsCount(this.getType() == QueryType.CONDITIONAL ? 2 : 1))
      throw new CommandUsageException();
    
    // Create a scanner for the arguments
    var scanner = context.getArgumentsScanner();
    
    // Parse the properties
    var properties = this.getManager().getCartBlockPropertiesArgumentType("radius", "distance").parse(scanner);
    var radius = this.getManager().getBlockSearchRadiusProperty(properties, "radius");
    var distance = this.getManager().getCartSearchDistanceProperty(properties, "distance");
    
    // Parse the arguments
    var location = this.getManager().getLocationArgumentType(senderLocation, radius).parse(scanner);
    var result = this.matchQueryMatcher(scanner, () -> this.getManager().findNearestOrRidingCart(location, distance, context.getSender()));
    
    // Execute the command
    if (result.getCart() != null)
    {
      var originalCode = result.getCart().getCode();
      if (this.getManager().updateCartCode(result.getCart(), result.getValue()))
      {
        context.sendMessage(this.getManager().formatMessage("cart-code-changed", Map.of(
          "cart", this.getManager().formatMinecart(result.getCart()),
          "original-code", originalCode)));
      }
      else
      {
        context.sendMessage(this.getManager().formatMessage("cart-code-retained", Map.of(
          "cart", this.getManager().formatMinecart(result.getCart()),
          "original-code", originalCode)));
      }
    }
    else
    {
      context.sendMessage(this.getManager().formatMessage("cart-not-found"));
    }
  }

  // Return suggestions for the specified command context and argument after the query matcher arguments
  @Override
  public Stream<String> suggestAfterQueryMatcher(CommandContext context)
  {
    return Stream.concat(
      this.getManager().getLocationArgumentType(null).suggest(context, 0),
      super.suggestAfterQueryMatcher(context));
  }
}
