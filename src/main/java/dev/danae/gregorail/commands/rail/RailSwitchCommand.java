package dev.danae.gregorail.commands.rail;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.commands.CommandExecutionType;
import dev.danae.gregorail.commands.CommandMessages;
import dev.danae.gregorail.commands.CommandUtils;
import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandHandler;
import dev.danae.gregorail.util.commands.CommandUsageException;
import dev.danae.gregorail.util.location.LocationParser;
import dev.danae.gregorail.util.location.LocationUtils;
import dev.danae.gregorail.util.minecart.MinecartUtils;
import dev.danae.gregorail.util.minecart.QueryParser;
import dev.danae.gregorail.util.parser.Parser;
import dev.danae.gregorail.util.parser.ParserException;
import dev.danae.gregorail.webhooks.WebhookType;
import dev.danae.gregorail.webhooks.WebhookUtils;
import java.util.EnumSet;
import java.util.List;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.block.data.Rail;
import org.bukkit.entity.minecart.RideableMinecart;


public class RailSwitchCommand extends CommandHandler
{
  // The execution type of the command
  private final CommandExecutionType executionType;
  
  
  // Constructor
  public RailSwitchCommand(CommandExecutionType executionType)
  {
    super("gregorail.rail.switch");
    
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
      
      var query = this.executionType == CommandExecutionType.CONDITIONAL ? QueryParser.parseQuery(context.getArgument(argumentIndex++)) : null;
      
      var shape = Parser.parseEnum(context.getArgument(argumentIndex++), Rail.Shape.class);
    
      var block = LocationParser.parseBlockAtLocation(context.getJoinedArguments(argumentIndex++), senderLocation, blockDistance);
      if (block == null)
        throw new CommandException("No block found");
      if (!EnumSet.of(Material.RAIL, Material.POWERED_RAIL, Material.DETECTOR_RAIL, Material.ACTIVATOR_RAIL).contains(block.getType()))
        throw new CommandException(String.format("%s is not a rail block", BaseComponent.toPlainText(LocationUtils.formatBlock(block))));
      
      var blockState = block.getState();
      
      var blockData = (Rail)block.getBlockData();
      if (!blockData.getShapes().contains(shape))
        throw new CommandException(String.format("%s cannot be set to shape %s", BaseComponent.toPlainText(LocationUtils.formatBlock(block)), shape.toString().toLowerCase()));
      
      // Check if the minecart matches the query
      var cart = LocationUtils.findNearestEntity(senderLocation, RideableMinecart.class, entityDistance);
      if (query == null || (cart != null && MinecartUtils.matchCode(cart, query)))
      {
        // Set the shape of the block
        blockData.setShape(shape);
        block.setBlockData(blockData);
      
        // Execute the appropriate webhooks
        RailPlugin.getInstance().executeWebhook(WebhookType.SWITCH_CHANGED, WebhookUtils.createSwitchChangedPayload(blockState, shape, null));
        
        // Send a message about the updated block
        CommandMessages.sendSwitchChangedMessage(context, block, shape, cart);
      }
      else
      {
        // Send information about the unchanged block
        CommandMessages.sendSwitchUnchangedMessage(context, block, shape, cart);
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
    if (context.getLastArgument().startsWith("#"))
      return CommandUtils.handlePropertyTabCompletion(context.getLastArgument(), "block-distance=", "distance=");
    
    switch (this.executionType)
    {
      case ALWAYS:
        if (context.hasAtLeastArgumentsCount(2))
          return CommandUtils.handleLocationTabCompletion(context, 1);
        else if (context.hasArgumentsCount(1))
          return CommandUtils.handleShapeTabCompletion(context.getArgument(0));
        else
          return null;
        
      case CONDITIONAL:
        if (context.hasAtLeastArgumentsCount(3))
          return CommandUtils.handleLocationTabCompletion(context, 2);
        else if (context.hasArgumentsCount(2))
          return CommandUtils.handleShapeTabCompletion(context.getArgument(1));
        else if (context.hasArgumentsCount(1))
          return CommandUtils.handleCodesTabCompletion(context.getArgument(0));
        else
          return null;
        
      default:
        return null;
    }
  }
}
