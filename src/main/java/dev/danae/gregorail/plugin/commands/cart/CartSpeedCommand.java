package dev.danae.gregorail.plugin.commands.cart;

import java.util.List;
import dev.danae.common.commands.CommandContext;
import dev.danae.common.commands.CommandException;
import dev.danae.common.commands.CommandUsageException;
import dev.danae.common.commands.arguments.ArgumentType;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.commands.ManagerQueryCommand;
import dev.danae.gregorail.plugin.commands.ManagerQueryCommandType;
import dev.danae.gregorail.plugin.Formatter;


public class CartSpeedCommand extends ManagerQueryCommand
{  
  // Constructor
  public CartSpeedCommand(Manager manager, ManagerQueryCommandType type)
  {
    super(manager, type, "gregorail.cart.speed");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException
  {
    try
    {      
      // Assert that the command sender has a location
      var senderLocation = context.assertSenderHasLocation();
      
      // Validate the number of arguments
      if (!context.hasAtLeastArgumentsCount(this.getType() == ManagerQueryCommandType.CONDITIONAL ? 2 : 1))
        throw new CommandUsageException();
      
      // Create a scanner for the arguments
      var scanner = context.getArgumentsScanner();
      
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
      {
        if (context.hasAtLeastArgumentsCount(2))
          return this.handleLocationTabCompletion(context, 1, false);
        else if (context.hasArgumentsCount(1))
          return this.handleSpeedMultiplierTabCompletion(context.getArgument(0));
        else
          return List.of();
      }
        
      case CONDITIONAL:
      {
        var separatorIndex = context.findLastArgumentIndex("||");
        if (separatorIndex == 2)
          return this.handleSpeedMultiplierTabCompletion(context.getLastArgument(0));
        else if (separatorIndex == 1)
          return this.handleCodesTabCompletion(context.getLastArgument(0));
        else if (context.hasAtLeastArgumentsCount(3))
          return this.handleLocationTabCompletion(context, separatorIndex >= 0 ? context.getArgumentsCount() - separatorIndex + 2 : 2, true);
        else if (context.hasArgumentsCount(2))
          return this.handleSpeedMultiplierTabCompletion(context.getArgument(1));
        else if (context.hasArgumentsCount(1))
          return this.handleCodesTabCompletion(context.getArgument(0));
        else
          return List.of();
      }
        
      default:
        return List.of();
    }
  }
}
