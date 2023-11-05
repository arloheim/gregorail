package dev.danae.gregorail.commands;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandHandler;
import dev.danae.gregorail.util.commands.CommandUsageException;
import dev.danae.gregorail.util.location.InvalidLocationException;
import dev.danae.gregorail.util.location.LocationUtils;
import dev.danae.gregorail.util.minecart.InvalidQueryException;
import dev.danae.gregorail.util.minecart.MinecartUtils;
import dev.danae.gregorail.util.minecart.QueryUtils;
import dev.danae.gregorail.util.webhooks.WebhookType;
import dev.danae.gregorail.util.webhooks.WebhookUtils;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.minecart.RideableMinecart;


public class RailBlockIfCommand extends CommandHandler
{
  // Constructor
  public RailBlockIfCommand()
  {
    super("gregorail.rail.blockif");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {
    try
    {      
      // Assert that the command sender has a location
      var senderLocation = context.assertSenderHasLocation();
      
      // Parse the arguments
      if (!context.hasAtLeastArgumentsCount(3))
        throw new CommandUsageException();
      
      var query = QueryUtils.parseQuery(context.getArgument(0));
      
      var material = CommandUtils.parseMaterial(context.getArgument(1), true);
    
      var block = LocationUtils.parseBlockAtLocation(senderLocation, context.getJoinedArguments(2));
      if (block == null)
        throw new CommandException("No block found");
      
      var blockState = block.getState();
      
      // Check for a minecart with the code at the sender location
      var cart = LocationUtils.findNearestEntity(senderLocation, RideableMinecart.class);
      if (cart != null && MinecartUtils.matchCode(cart, query))
      {
        // Set the material of the block
        block.setType(material);
      
        // Execute the appropriate webhooks
        RailPlugin.getInstance().executeWebhook(WebhookType.BLOCK_CHANGED, WebhookUtils.createBlockChangedPayload(blockState, material, cart));
        
        // Send information about the updated block
        context.sendMessage(new ComponentBuilder()
          .append(LocationUtils.formatBlockState(blockState), ComponentBuilder.FormatRetention.NONE)
          .append(" now has material ", ComponentBuilder.FormatRetention.NONE)
          .append(material.getKey().getKey(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
          .append(", detected ", ComponentBuilder.FormatRetention.NONE)
          .append(LocationUtils.formatEntity(cart), ComponentBuilder.FormatRetention.NONE)
          .create());
      }
      else
      {
        // Send information about the block
        context.sendMessage(new ComponentBuilder()
          .append(LocationUtils.formatBlockState(blockState), ComponentBuilder.FormatRetention.NONE)
          .append(" still has original material ", ComponentBuilder.FormatRetention.NONE)
          .append(material.getKey().getKey(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
          .append(", detected ", ComponentBuilder.FormatRetention.NONE)
          .append(LocationUtils.formatEntity(cart), ComponentBuilder.FormatRetention.NONE)
          .create());
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
    if (context.hasAtLeastArgumentsCount(3))
      return CommandUtils.handleLocationTabCompletion(context, 2);
    else if (context.hasArgumentsCount(2))
      return CommandUtils.handleMaterialTabCompletion(context.getArgument(1), true);
    else if (context.hasArgumentsCount(1))
      return CommandUtils.handleCodesTabCompletion(context.getArgument(0));
    else
      return null;
  }
}
