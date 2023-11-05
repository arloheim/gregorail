package dev.danae.gregorail.commands;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandUsageException;
import dev.danae.gregorail.util.location.InvalidLocationException;
import dev.danae.gregorail.util.location.LocationUtils;
import dev.danae.gregorail.util.minecart.CodeUtils;
import dev.danae.gregorail.util.minecart.InvalidCodeException;
import dev.danae.gregorail.util.minecart.MinecartUtils;
import dev.danae.gregorail.util.webhooks.WebhookType;
import dev.danae.gregorail.util.webhooks.WebhookUtils;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;


public class CartSetCommand extends AbstractCartCommand
{
  // Constructor
  public CartSetCommand()
  {
    super("gregorail.cart.set");
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
      if (!context.hasAtLeastArgumentsCount(1))
        throw new CommandUsageException();
      
      var code = CodeUtils.createCode(context.getArgument(0));
      
      var cart = this.findMinecart(context, 1);
      if (cart == null)
        throw new CommandException("No cart found");
      
      // Assign the code to the cart
      MinecartUtils.setCode(cart, code);
      
      // Execute the appropriate webhooks
      RailPlugin.getInstance().executeWebhook(WebhookType.CART_CODE_SET, WebhookUtils.createCartCodeSetPayload(cart));
      
      // Send information about the updated cart
      context.sendMessage(new ComponentBuilder()
        .append(LocationUtils.formatEntity(cart), ComponentBuilder.FormatRetention.NONE)
        .append(" now has code ", ComponentBuilder.FormatRetention.NONE)
        .append(code.toString(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
        .create());
    }
    catch (InvalidLocationException | InvalidCodeException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
  
  // Handle tab completion of the command
  @Override
  public List<String> handleTabCompletion(CommandContext context)
  {
    if (context.hasAtLeastArgumentsCount(2))
      return CommandUtils.handleLocationTabCompletion(context, 1);
    else if (context.hasArgumentsCount(1))
      return CommandUtils.handleCodeTabCompletion(context.getArgument(0));
    else
      return null;
  }
}
