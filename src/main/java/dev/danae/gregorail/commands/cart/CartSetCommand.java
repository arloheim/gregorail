package dev.danae.gregorail.commands.cart;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.commands.CommandMessages;
import dev.danae.gregorail.commands.CommandExecutionType;
import dev.danae.gregorail.commands.CommandUtils;
import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandUsageException;
import dev.danae.gregorail.util.location.InvalidLocationException;
import dev.danae.gregorail.util.minecart.CodeUtils;
import dev.danae.gregorail.util.minecart.InvalidCodeException;
import dev.danae.gregorail.util.minecart.InvalidQueryException;
import dev.danae.gregorail.util.minecart.MinecartUtils;
import dev.danae.gregorail.util.minecart.QueryUtils;
import dev.danae.gregorail.util.webhooks.WebhookType;
import dev.danae.gregorail.util.webhooks.WebhookUtils;
import java.util.List;


public class CartSetCommand extends CartCommand
{
  // The execution type of the command
  private final CommandExecutionType executionType;
  
  
  // Constructor
  public CartSetCommand(CommandExecutionType executionType)
  {
    super("gregorail.cart.set");
    
    this.executionType = executionType;
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {
    try
    {      
      // Assert that the command sender has a location
      context.assertSenderHasLocation();
    
      // Parse the arguments
      if (!context.hasAtLeastArgumentsCount(this.executionType == CommandExecutionType.CONDITIONAL ? 2 : 1))
        throw new CommandUsageException();
      
      var argumentIndex = 0;
      
      var query = this.executionType == CommandExecutionType.CONDITIONAL ? QueryUtils.parseQuery(context.getArgument(argumentIndex++)) : null;
      
      var code = CodeUtils.createCode(context.getArgument(argumentIndex++));
      
      var cart = this.findMinecart(context, argumentIndex++);
      if (cart == null)
        throw new CommandException("No cart found");
      
      // Check if the minecart matches the query
      if (query == null || MinecartUtils.matchCode(cart, query))
      {
        // Assign the code to the cart
        MinecartUtils.setCode(cart, code);
      
        // Execute the appropriate webhooks
        RailPlugin.getInstance().executeWebhook(WebhookType.CART_CODE_SET, WebhookUtils.createCartCodeSetPayload(cart));
      
        // Send a message about the updated cart
        CommandMessages.sendCartCodeChangedMessage(context, cart, code);
      }
      else
      {
        // Send a message about the unchanged cart
        CommandMessages.sendCartCodeUnchangedMessage(context, cart);
      }
    }
    catch (InvalidLocationException | InvalidCodeException | InvalidQueryException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
  
  // Handle tab completion of the command
  @Override
  public List<String> handleTabCompletion(CommandContext context)
  {
    switch (this.executionType)
    {
      case ALWAYS:
        if (context.hasAtLeastArgumentsCount(2))
          return CommandUtils.handleLocationTabCompletion(context, 1);
        else if (context.hasArgumentsCount(1))
          return CommandUtils.handleCodeTabCompletion(context.getArgument(0));
        else
          return null;
        
      case CONDITIONAL:
        if (context.hasAtLeastArgumentsCount(3))
          return CommandUtils.handleLocationTabCompletion(context, 1);
        else if (context.hasArgumentsCount(2))
          return CommandUtils.handleCodeTabCompletion(context.getArgument(0));
        else if (context.hasArgumentsCount(1))
          return CommandUtils.handleCodesTabCompletion(context.getArgument(0));
        else
          return null;
        
      default:
        return null;
    }
  }
}
