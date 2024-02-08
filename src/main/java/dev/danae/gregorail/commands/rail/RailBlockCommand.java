package dev.danae.gregorail.commands.rail;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.commands.CommandMessages;
import dev.danae.gregorail.commands.CommandExecutionType;
import dev.danae.gregorail.commands.CommandUtils;
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
import org.bukkit.entity.minecart.RideableMinecart;


public class RailBlockCommand extends CommandHandler
{
  // The execution type of the command
  private final CommandExecutionType executionType;
  
  
  // Constructor
  public RailBlockCommand(CommandExecutionType executionType)
  {
    super("gregorail.rail.block");
    
    this.executionType = executionType;
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {
    try
    {      
      // Assert that the command sender has a location
      var senderLocation = context.assertSenderHasLocation();
      
      // Parse the properties
      var blockDistance = context.getPropertyAsUnsignedInt("block-distance", RailPlugin.getBlockSearchRadius());
      var entityDistance = context.getPropertyAsUnsignedInt("distance", RailPlugin.getEntitySearchRadius());
      
      // Parse the arguments
      if (!context.hasAtLeastArgumentsCount(this.executionType == CommandExecutionType.CONDITIONAL ? 3 : 2))
        throw new CommandUsageException();
      
      var argumentIndex = 0;
      
      var query = this.executionType == CommandExecutionType.CONDITIONAL ? QueryUtils.parseQuery(context.getArgument(argumentIndex++)) : null;
      
      var material = CommandUtils.parseMaterial(context.getArgument(argumentIndex++), true);
    
      var block = LocationUtils.parseBlockAtLocation(senderLocation, context.getJoinedArguments(argumentIndex++), blockDistance);
      if (block == null)
        throw new CommandException("No block found");
      
      var blockState = block.getState();
      
      // Check if the minecart matches the query
      var cart = LocationUtils.findNearestEntity(senderLocation, RideableMinecart.class, entityDistance);
      if (query == null || (cart != null && MinecartUtils.matchCode(cart, query)))
      {
        // Set the material of the block
        block.setType(material);
      
        // Execute the appropriate webhooks
        RailPlugin.getInstance().executeWebhook(WebhookType.BLOCK_CHANGED, WebhookUtils.createBlockChangedPayload(blockState, material, null));
        
        // Send a message about the updated block
        CommandMessages.sendBlockChangedMessage(context, blockState, material, cart);
      }
      else
      {
        // Send a message about the unchanged block
        CommandMessages.sendBlockUnchangedMessage(context, blockState, material, cart);
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
      return CommandUtils.handlePropertyTabCompletion(context.getLastArgument(), "block-distance=", "distance=");
    
    switch (this.executionType)
    {
      case ALWAYS:
        if (context.hasAtLeastArgumentsCount(2))
          return CommandUtils.handleLocationTabCompletion(context, 1);
        else if (context.hasArgumentsCount(1))
          return CommandUtils.handleMaterialTabCompletion(context.getArgument(0), true);
        else
          return null;
        
      case CONDITIONAL:
        if (context.hasAtLeastArgumentsCount(3))
          return CommandUtils.handleLocationTabCompletion(context, 2);
        else if (context.hasArgumentsCount(2))
          return CommandUtils.handleMaterialTabCompletion(context.getArgument(1), true);
        else if (context.hasArgumentsCount(1))
          return CommandUtils.handleCodesTabCompletion(context.getArgument(0));
        else
          return null;
        
      default:
        return null;
    }
  }
}
