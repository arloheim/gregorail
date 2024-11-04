package dev.danae.gregorail.plugin.commands.cart;

import java.util.List;
import java.util.stream.Stream;
import dev.danae.common.commands.CommandContext;
import dev.danae.common.commands.CommandException;
import dev.danae.common.commands.CommandUsageException;
import dev.danae.common.commands.arguments.ArgumentType;
import dev.danae.common.commands.arguments.PropertyList;
import dev.danae.gregorail.model.Code;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.model.arguments.CodeArgumentType;
import dev.danae.gregorail.plugin.commands.QueryType;
import dev.danae.gregorail.plugin.commands.QueryMatcherCommand;
import dev.danae.gregorail.plugin.Formatter;


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
        context.sendRichMessage(Formatter.formatCartCodeChangedMessage(result.getCart(), originalCode, result.getValue()));
      else
        context.sendRichMessage(Formatter.formatCartCodeRetainedMessage(result.getCart(), originalCode));
    }
    else
    {
      context.sendRichMessage(Formatter.formatCart(result.getCart()));
    }
  }

  // Return suggestions for the specified command context and argument after the query matcher arguments
  @Override
  public Stream<String> suggestAfterQueryMatcher(CommandContext context, int argumentIndex)
  {
    return Stream.concat(
      this.getManager().getLocationArgumentType(null).suggest(context, argumentIndex),
      super.suggestAfterQueryMatcher(context, argumentIndex));
  }
}
