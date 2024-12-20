package dev.danae.gregorail.plugin.commands.rail;

import java.util.Map;
import java.util.stream.Stream;
import dev.danae.common.commands.CommandContext;
import dev.danae.common.commands.CommandException;
import dev.danae.common.commands.CommandUsageException;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.commands.QueryType;
import dev.danae.gregorail.plugin.commands.QueryMatcherCommand;
import org.bukkit.Material;


public class RailBlockCommand extends QueryMatcherCommand<Material>
{
  // Constructor
  public RailBlockCommand(Manager manager, QueryType type)
  {
    super(manager, type, manager.getBlockMaterialQueryMatcherArgumentType(), "gregorail.rail.block");
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
      if (!context.hasAtLeastArgumentsCount(this.getType() == QueryType.CONDITIONAL ? 3 : 2))
        throw new CommandUsageException();
      
      // Create a scanner for the arguments
      var scanner = context.getArgumentsScanner();
      
      // Parse the properties
      var properties = this.getManager().getCartBlockPropertiesArgumentType("radius", "distance").parse(scanner);
      var radius = this.getManager().getBlockSearchRadiusProperty(properties, "radius");
      var distance = this.getManager().getCartSearchDistanceProperty(properties, "distance");
      
      // Parse the arguments
      var result = this.matchQueryMatcher(scanner, () -> this.getManager().findNearestCart(senderLocation, distance));
      var block = this.getManager().getLocationArgumentType(senderLocation, radius).parse(scanner).getBlock();
        
      // Execute the command
      var originalMaterial = block.getState().getType();
      if (this.getManager().updateBlockMaterial(block, result.getValue(), result.getCart()))
      {
        context.sendMessage(this.getManager().formatMessage("block-material-changed", Map.of(
          "block", block,
          "original-material", originalMaterial,
          "material", result.getValue())));
      }
      else
      {
        context.sendMessage(this.getManager().formatMessage("block-material-retained", Map.of(
          "block", block,
          "original-material", originalMaterial)));
      }
    }
    catch (IllegalArgumentException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }

  // Return suggestions for the specified command context and argument after the query matcher arguments
  @Override
  public Stream<String> suggestAfterQueryMatcher(CommandContext context)
  {
    return Stream.concat(
      this.getManager().getLocationArgumentType(null).suggest(context, 0),
      super.suggestAfterQueryMatcher(context));
  }
}
