package dev.danae.gregorail.plugin.commands.rail;

import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.commands.CommandContext;
import dev.danae.gregorail.plugin.commands.CommandException;
import dev.danae.gregorail.plugin.commands.CommandUsageException;
import dev.danae.gregorail.plugin.commands.ManagerQueryCommand;
import dev.danae.gregorail.plugin.commands.QueryCommandType;
import dev.danae.gregorail.plugin.Formatter;
import dev.danae.gregorail.util.parser.ParserException;
import java.util.List;


public class RailBlockCommand extends ManagerQueryCommand
{  
  // Constructor
  public RailBlockCommand(Manager manager, QueryCommandType type)
  {
    super(manager, type, "gregorail.rail.block");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {
    try
    {      
      // Assert that the command sender has a location
      var senderLocation = context.assertSenderHasLocation();
            
      // Validate the number of arguments
      if (!context.hasAtLeastArgumentsCount(this.getType() == QueryCommandType.CONDITIONAL ? 3 : 2))
        throw new CommandUsageException();
      
      // Create a scanner for the arguments
      var scanner = context.getArgumentsScanner(this.getManager());
      
      // Parse the properties
      var properties = scanner.wrapInPropertyBag();
      var radius = properties.getUnsignedInt("radius", this.getManager().getBlockSearchRadius());
      var distance = properties.getUnsignedInt("distance", this.getManager().getCartSearchDistance());
      
      // Parse the arguments
      var result = this.matchQueryMatcher(scanner, () -> scanner.nextMaterial(true), () -> this.getManager().findNearestCart(senderLocation, distance));
      var block = scanner.nextLocation(senderLocation, radius).getBlock();
        
      // Execute the command
      var originalMaterial = block.getState().getType();
      if (this.getManager().updateBlockMaterial(block, result.getValue(), result.getCart()))
        context.sendMessage(Formatter.formatBlockMaterialChangedMessage(block, originalMaterial, result.getValue(), result.getCart()));
      else
        context.sendMessage(Formatter.formatBlockMaterialRetainedMessage(block, originalMaterial, result.getCart()));
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
        if (context.hasAtLeastArgumentsCount(2))
          return this.handleLocationTabCompletion(context, 1);
        else if (context.hasArgumentsCount(1))
          return this.handleMaterialTabCompletion(context.getArgument(0), true);
        else
          return null;
        
      case CONDITIONAL:
        if (context.hasAtLeastArgumentsCount(3))
          return this.handleLocationTabCompletion(context, 2);
        else if (context.hasArgumentsCount(2))
          return this.handleMaterialTabCompletion(context.getArgument(1), true);
        else if (context.hasArgumentsCount(1))
          return this.handleCodesTabCompletion(context.getArgument(0));
        else
          return null;
        
      default:
        return null;
    }
  }
}
