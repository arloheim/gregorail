package dev.danae.gregorail.plugin.commands.cart;

import java.util.List;
import dev.danae.common.commands.CommandContext;
import dev.danae.common.commands.CommandException;
import dev.danae.common.commands.CommandUsageException;
import dev.danae.common.commands.arguments.ArgumentType;
import dev.danae.gregorail.model.Code;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.commands.ManagerQueryCommand;
import dev.danae.gregorail.plugin.commands.ManagerQueryCommandType;
import dev.danae.gregorail.plugin.Formatter;


public class CartClearCommand extends ManagerQueryCommand
{
  // Constructor
  public CartClearCommand(Manager manager, ManagerQueryCommandType type)
  {
    super(manager, type, "gregorail.cart.clear");
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
      if (!context.hasAtLeastArgumentsCount(this.getType() == ManagerQueryCommandType.CONDITIONAL ? 1 : 0))
        throw new CommandUsageException();
      
      // Create a scanner for the arguments
      var scanner = context.getArgumentsScanner();
      
      // Parse the properties
      var properties = scanner.wrapInPropertyBag();
      var radius = properties.getUnsignedInt("radius", this.getManager().getBlockSearchRadius());
      var distance = properties.getUnsignedInt("distance", this.getManager().getCartSearchDistance());
      
      // Parse the arguments
      var result = this.matchQuery(scanner, () -> this.getManager().findNearestOrRidingCart(scanner.nextLocation(senderLocation, radius, null), distance, context.getSender()));
      
      // Execute the command
      if (result.getCart() != null)
      {
        var originalCode = result.getCart().getCode();
        if (result.getValue() && this.getManager().updateCartCode(result.getCart(), Code.empty()))
          context.sendMessage(Formatter.formatCartCodeClearedMessage(result.getCart(), originalCode));
        else
          context.sendMessage(Formatter.formatCartCodeRetainedMessage(result.getCart(), originalCode));
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
        if (context.hasAtLeastArgumentsCount(1))
          return this.handleLocationTabCompletion(context, 0, false);
        else
          return List.of();
      }
        
      case CONDITIONAL:
      {
        if (context.hasAtLeastArgumentsCount(2))
          return this.handleLocationTabCompletion(context, 1, false);
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
