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
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;


public class RailBlockCommand extends CommandHandler
{
  // Constructor
  public RailBlockCommand()
  {
    super("gregorail.rail.block");
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
      
      var material = CommandUtils.parseMaterial(context.getArgument(0), true);
    
      var block = LocationUtils.parseBlockAtLocation(senderLocation, context.getJoinedArguments(1));
      if (block == null)
        throw new CommandException("No block found");
      
      var blockState = block.getState();
      
      // Set the material of the block
      block.setType(material);
      
      // Execute the appropriate webhooks
      RailPlugin.getInstance().executeWebhook(WebhookType.BLOCK_CHANGED, WebhookUtils.createBlockChangedPayload(blockState, material, null));
        
      // Send information about the updated block
      context.sendMessage(new ComponentBuilder()
        .append(LocationUtils.formatBlockState(blockState), ComponentBuilder.FormatRetention.NONE)
        .append(" now has material ", ComponentBuilder.FormatRetention.NONE)
        .append(material.getKey().getKey(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
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
      return CommandUtils.handleMaterialTabCompletion(context.getArgument(0), true);
    else
      return null;
  }
}
