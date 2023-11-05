package dev.danae.gregorail.commands;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandHandler;
import dev.danae.gregorail.util.commands.CommandUsageException;
import dev.danae.gregorail.util.location.InvalidLocationException;
import dev.danae.gregorail.util.location.LocationUtils;
import dev.danae.gregorail.util.webhooks.WebhookType;
import dev.danae.gregorail.util.webhooks.WebhookUtils;
import java.util.EnumSet;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Material;
import org.bukkit.block.data.Rail;


public class RailSwitchCommand extends CommandHandler
{
  // Constructor
  public RailSwitchCommand()
  {
    super("gregorail.rail.switch");
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
      if (!context.hasAtLeastArgumentsCount(2))
        throw new CommandUsageException();
      
      var shape = CommandUtils.parseShape(context.getArgument(0));
    
      var block = LocationUtils.parseBlockAtLocation(senderLocation, context.getJoinedArguments(1));
      if (block == null)
        throw new CommandException("No block found");
      if (!EnumSet.of(Material.RAIL, Material.POWERED_RAIL, Material.DETECTOR_RAIL, Material.ACTIVATOR_RAIL).contains(block.getType()))
        throw new CommandException(String.format("%s is not a rail block", BaseComponent.toPlainText(LocationUtils.formatBlock(block))));
      
      var blockState = block.getState();
      
      var blockData = (Rail)block.getBlockData();
      if (!blockData.getShapes().contains(shape))
        throw new CommandException(String.format("%s cannot be set to shape %s", BaseComponent.toPlainText(LocationUtils.formatBlock(block)), shape.toString().toLowerCase()));
      
      // Set the shape of the block
      blockData.setShape(shape);
      block.setBlockData(blockData);
      
      // Execute the appropriate webhooks
      RailPlugin.getInstance().executeWebhook(WebhookType.SWITCH_CHANGED, WebhookUtils.createSwitchChangedPayload(blockState, shape, null));
        
      // Send information about the updated block
      context.sendMessage(new ComponentBuilder()
          .append(LocationUtils.formatBlock(block), ComponentBuilder.FormatRetention.NONE)
          .append(" now has shape ", ComponentBuilder.FormatRetention.NONE)
          .append(shape.toString().toLowerCase(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
          .create());
    }
    catch (InvalidLocationException ex)
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
      return CommandUtils.handleShapeTabCompletion(context.getArgument(0));
    else
      return null;
  }
}
