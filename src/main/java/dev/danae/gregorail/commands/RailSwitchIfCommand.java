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
import java.util.EnumSet;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Material;
import org.bukkit.block.data.Rail;
import org.bukkit.entity.minecart.RideableMinecart;


public class RailSwitchIfCommand extends CommandHandler
{
  // Constructor
  public RailSwitchIfCommand()
  {
    super("gregorail.rail.switchif");
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
      
      var shape = CommandUtils.parseShape(context.getArgument(1));
    
      var block = LocationUtils.parseBlockAtLocation(senderLocation, context.getJoinedArguments(2));
      if (block == null)
        throw new CommandException("No block found");
      if (!EnumSet.of(Material.RAIL, Material.POWERED_RAIL, Material.DETECTOR_RAIL, Material.ACTIVATOR_RAIL).contains(block.getType()))
        throw new CommandException(String.format("%s is not a rail block", BaseComponent.toPlainText(LocationUtils.formatBlock(block))));
      
      var blockState = block.getState();
      
      var blockData = (Rail)block.getBlockData();
      if (!blockData.getShapes().contains(shape))
        throw new CommandException(String.format("%s cannot be set to shape %s", BaseComponent.toPlainText(LocationUtils.formatBlock(block)), shape.toString().toLowerCase()));
      
      // Check for a minecart with the code at the sender location
      var cart = LocationUtils.findNearestEntity(senderLocation, RideableMinecart.class);
      if (cart != null && MinecartUtils.matchCode(cart, query))
      {
        // Set the shape of the block
        blockData.setShape(shape);
        block.setBlockData(blockData);
      
        // Execute the appropriate webhooks
        RailPlugin.getInstance().executeWebhook(WebhookType.SWITCH_CHANGED, WebhookUtils.createSwitchChangedPayload(blockState, shape, cart));
        
        // Send information about the updated block
        context.sendMessage(new ComponentBuilder()
          .append(LocationUtils.formatBlock(block), ComponentBuilder.FormatRetention.NONE)
          .append(" now has shape ", ComponentBuilder.FormatRetention.NONE)
          .append(shape.toString().toLowerCase(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
          .append(", detected ", ComponentBuilder.FormatRetention.NONE)
          .append(LocationUtils.formatEntity(cart), ComponentBuilder.FormatRetention.NONE)
          .create());
      }
      else
      {
        // Send information about the block
        context.sendMessage(new ComponentBuilder()
          .append(LocationUtils.formatBlock(block), ComponentBuilder.FormatRetention.NONE)
          .append(" still has original shape ", ComponentBuilder.FormatRetention.NONE)
          .append(shape.toString().toLowerCase(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
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
      return CommandUtils.handleShapeTabCompletion(context.getArgument(1));
    else if (context.hasArgumentsCount(1))
      return CommandUtils.handleCodesTabCompletion(context.getArgument(0));
    else
      return null;
  }
}
