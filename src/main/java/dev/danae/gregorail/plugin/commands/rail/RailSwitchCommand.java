package dev.danae.gregorail.plugin.commands.rail;

import java.util.List;
import dev.danae.common.commands.CommandContext;
import dev.danae.common.commands.CommandException;
import dev.danae.common.commands.CommandUsageException;
import dev.danae.common.commands.arguments.ArgumentType;
import dev.danae.common.commands.arguments.MaterialFilter;
import dev.danae.common.commands.arguments.StringArgumentType;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.commands.ManagerQueryCommand;
import dev.danae.gregorail.plugin.commands.ManagerQueryCommandType;
import dev.danae.gregorail.plugin.Formatter;
import org.bukkit.Material;
import org.bukkit.block.data.Rail;


public class RailSwitchCommand extends ManagerQueryCommand
{  
  // Constructor
  public RailSwitchCommand(Manager manager, ManagerQueryCommandType type)
  {
    super(manager, type, "gregorail.rail.switch");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException
  {
    try
    {      
      // Assert that the command sender has a location
      var senderLocation = context.assertSenderHasLocation();
      
      // Validate the number of arguments
      if (!context.hasAtLeastArgumentsCount(this.getType() == ManagerQueryCommandType.CONDITIONAL ? 3 : 2))
        throw new CommandUsageException();
      
      // Create a scanner for the arguments
      var scanner = context.getArgumentsScanner();
      
      // Parse the properties        
      var properties = scanner.wrapInPropertyBag();
      var radius = properties.getUnsignedInt("radius", this.getManager().getBlockSearchRadius());
      var distance = properties.getUnsignedInt("distance", this.getManager().getCartSearchDistance());
      
      // Parse the arguments
      var result = this.matchQueryMatcher(scanner, () -> scanner.nextEnum(Rail.Shape.class, "shape"), () -> this.getManager().findNearestCart(senderLocation, distance));
      var block = scanner.nextLocation(senderLocation, radius).getBlock();
      
      // Execute the command
      var originalShape = ((Rail)block.getBlockData()).getShape();
      if (this.getManager().updateBlockShape(block, result.getValue(), result.getCart()))
        context.sendMessage(Formatter.formatBlockShapeChangedMessage(block, originalShape, result.getValue(), result.getCart()));
      else
        context.sendMessage(Formatter.formatBlockShapeRetainedMessage(block, originalShape, result.getCart()));
    }
    catch (ParserException | IllegalArgumentException ex)
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
          return this.handleShapeTabCompletion(context.getArgument(0));
        else
          return List.of();
      }
        
      case CONDITIONAL:
      {
        var separatorIndex = context.findLastArgumentIndex("||");
        if (separatorIndex == 2)
          return this.handleShapeTabCompletion(context.getLastArgument(0));
        else if (separatorIndex == 1)
          return this.handleCodesTabCompletion(context.getLastArgument(0));
        else if (context.hasAtLeastArgumentsCount(3))
          return this.handleLocationTabCompletion(context, separatorIndex >= 0 ? context.getArgumentsCount() - separatorIndex + 2 : 2, true);
        else if (context.hasArgumentsCount(2))
          return this.handleShapeTabCompletion(context.getArgument(1));
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
