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


public class CartSetCommand extends ManagerQueryCommand
{  
  // Constructor
  public CartSetCommand(Manager manager, QueryCommandType type)
  {
    super(manager, type, "gregorail.cart.set");
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
      var result = this.matchQueryMatcher(scanner, () -> scanner.nextCode(), () -> this.getManager().findNearestOrRidingCart(scanner.nextLocation(senderLocation, radius, null), distance, context.getSender()));
      
      // Execute the command
      if (result.getCart() != null)
      {
        var originalCode = result.getCart().getCode();
        if (this.getManager().updateCartCode(result.getCart(), result.getValue()))
          context.sendMessage(Formatter.formatCartCodeChangedMessage(result.getCart(), originalCode, result.getValue()));
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
        if (context.hasAtLeastArgumentsCount(2))
          return this.handleLocationTabCompletion(context, 1, false);
        else if (context.hasArgumentsCount(1))
          return this.handleCodeTabCompletion(context.getArgument(0));
        else
          return List.of();
      }
        
      case CONDITIONAL:
      {
        var separatorIndex = context.findLastArgumentIndex("||");
        if (separatorIndex == 2)
          return this.handleCodeTabCompletion(context.getLastArgument(0));
        else if (separatorIndex == 1)
          return this.handleCodesTabCompletion(context.getLastArgument(0));
        else if (context.hasAtLeastArgumentsCount(3))
          return this.handleLocationTabCompletion(context, separatorIndex >= 0 ? context.getArgumentsCount() - separatorIndex + 2 : 2, true);
        else if (context.hasArgumentsCount(2))
          return this.handleCodeTabCompletion(context.getArgument(1));
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
