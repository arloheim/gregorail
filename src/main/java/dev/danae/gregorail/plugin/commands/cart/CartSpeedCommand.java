package dev.danae.gregorail.plugin.commands.cart;

import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.commands.CommandContext;
import dev.danae.gregorail.plugin.commands.CommandException;
import dev.danae.gregorail.plugin.commands.CommandUsageException;
import dev.danae.gregorail.plugin.commands.ManagerQueryCommand;
import dev.danae.gregorail.plugin.commands.QueryCommandType;
import dev.danae.gregorail.plugin.Formatter;
import dev.danae.gregorail.util.parser.ParserException;
import java.util.List;


public class CartSpeedCommand extends ManagerQueryCommand
{  
  // Constructor
  public CartSpeedCommand(Manager manager, QueryCommandType type)
  {
    super(manager, type, "gregorail.cart.speed");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {
    try
    {      
      // Assert that the command sender has a location
      var senderLocation = context.assertSenderHasLocation();
      
      // Validate the number of arguments
      if (!context.hasAtLeastArgumentsCount(this.getType() == QueryCommandType.CONDITIONAL ? 2 : 1))
        throw new CommandUsageException();
      
      // Create a scanner for the arguments
      var scanner = context.getArgumentsScanner(this.getManager());
      
      // Parse the properties
      var properties = scanner.wrapInPropertyBag();
      var radius = properties.getUnsignedInt("radius", this.getManager().getBlockSearchRadius());
      var distance = properties.getUnsignedInt("distance", this.getManager().getCartSearchDistance());
      
      // Parse the arguments
      var result = this.matchQueryMatcher(scanner, () -> scanner.nextDouble(), () -> this.getManager().findNearestOrRidingCart(scanner.nextLocation(senderLocation, radius, null), distance, context.getSender()));
      
      // Execute the command
      if (result.getCart() != null)
      {
        var originalSpeedMultiplier = result.getCart().getSpeedMultiplier();
        if (this.getManager().updateCartSpeedMultiplier(result.getCart(), result.getValue()))
          context.sendMessage(Formatter.formatCartSpeedChangedMessage(result.getCart(), originalSpeedMultiplier, result.getValue()));
        else
          context.sendMessage(Formatter.formatCartSpeedRetainedMessage(result.getCart(), originalSpeedMultiplier));
      }
      else
      {
        context.sendMessage(Formatter.formatCart(result.getCart()));
      }
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
    switch (this.getType())
    {
      case ALWAYS:
        if (context.hasAtLeastArgumentsCount(2))
          return this.handleLocationTabCompletion(context, 1);
        else if (context.hasArgumentsCount(1))
          return this.handleSpeedMultiplierTabCompletion(context.getArgument(0));
        else
          return null;
        
      case CONDITIONAL:
        if (context.hasAtLeastArgumentsCount(3))
          return this.handleLocationTabCompletion(context, 2);
        else if (context.hasArgumentsCount(2))
          return this.handleSpeedMultiplierTabCompletion(context.getArgument(1));
        else if (context.hasArgumentsCount(1))
          return this.handleCodesTabCompletion(context.getArgument(0));
        else
          return null;
        
      default:
        return null;
    }
  }
}
