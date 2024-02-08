package dev.danae.gregorail.commands.cart;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.commands.CommandMessages;
import dev.danae.gregorail.commands.CommandExecutionType;
import dev.danae.gregorail.commands.CommandUtils;
import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandUsageException;
import dev.danae.gregorail.util.location.InvalidLocationException;
import dev.danae.gregorail.util.minecart.InvalidQueryException;
import dev.danae.gregorail.util.minecart.MinecartUtils;
import dev.danae.gregorail.util.minecart.QueryUtils;
import dev.danae.gregorail.webhooks.WebhookType;
import dev.danae.gregorail.webhooks.WebhookUtils;
import java.util.List;


public class CartSpeedCommand extends CartCommand
{ 
  // The execution type of the command
  private final CommandExecutionType executionType;
  
  
  // Constructor
  public CartSpeedCommand(CommandExecutionType executionType)
  {
    super("gregorail.cart.speed");
    
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

      // Parse the properties
      var entityDistance = context.getPropertyAsUnsignedInt("distance", RailPlugin.getEntitySearchRadius());
    
      // Parse the arguments
      if (!context.hasAtLeastArgumentsCount(this.executionType == CommandExecutionType.CONDITIONAL ? 2 : 1))
        throw new CommandUsageException();
      
      var argumentIndex = 0;
      
      var query = this.executionType == CommandExecutionType.CONDITIONAL ? QueryUtils.parseQuery(context.getArgument(argumentIndex++)) : null;
     
      var speedMultiplier = CommandUtils.parseSpeedMultiplier(context.getArgument(argumentIndex++));
      
      var cart = this.findMinecart(context, argumentIndex++, entityDistance);
      if (cart == null)
        throw new CommandException("No cart found");
      
      // Check if the minecart matches the query
      if (query == null || MinecartUtils.matchCode(cart, query))
      {
        // Set the speed multiplier to the cart
        MinecartUtils.setSpeedMultiplier(cart, speedMultiplier);
      
        // Execute the appropriate webhooks if the cart has a code
        if (MinecartUtils.getCode(cart) != null)
          RailPlugin.getInstance().executeWebhook(WebhookType.CART_SPEED_CHANGED, WebhookUtils.createCartSpeedChangedPayload(cart));
      
        // Send a message about the changed cart
        CommandMessages.sendCartSpeedChangedMessage(context, cart, speedMultiplier);
      }
      else
      {
        // Send a message about the unchanged cart
        CommandMessages.sendCartSpeedUnchangedMessage(context, cart);
      }
    }
    catch (InvalidLocationException | InvalidQueryException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
  
  // Handle tab completion of the command
  @Override
  public List<String> handleTabCompletion(CommandContext context)
  {
    if (context.getLastArgument().startsWith("#"))
      return CommandUtils.handlePropertyTabCompletion(context.getLastArgument(), "distance=");
    
    switch (this.executionType)
    {
      case ALWAYS:
        if (context.hasAtLeastArgumentsCount(2))
          return CommandUtils.handleLocationTabCompletion(context, 1);
        else if (context.hasArgumentsCount(1))
          return CommandUtils.handleSpeedMultiplierTabCompletion(context.getArgument(0));
        else
          return null;
        
      case CONDITIONAL:
        if (context.hasAtLeastArgumentsCount(3))
          return CommandUtils.handleLocationTabCompletion(context, 2);
        else if (context.hasArgumentsCount(2))
          return CommandUtils.handleSpeedMultiplierTabCompletion(context.getArgument(1));
        else if (context.hasArgumentsCount(1))
          return CommandUtils.handleCodesTabCompletion(context.getArgument(0));
        else
          return null;
        
      default:
        return null;
    }
  }
}
