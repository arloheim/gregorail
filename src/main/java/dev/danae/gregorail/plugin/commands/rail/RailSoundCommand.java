package dev.danae.gregorail.plugin.commands.rail;

import java.util.Map;
import dev.danae.common.commands.CommandContext;
import dev.danae.common.commands.CommandException;
import dev.danae.common.commands.CommandUsageException;
import dev.danae.common.commands.CommandUtils;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.commands.QueryType;
import dev.danae.gregorail.plugin.commands.QueryMatcherCommand;
import org.bukkit.NamespacedKey;


public class RailSoundCommand extends QueryMatcherCommand<NamespacedKey>
{  
  // Constructor
  public RailSoundCommand(Manager manager, QueryType type)
  {
    super(manager, type, manager.getSoundQueryMatcherArgumentType(), "gregorail.rail.sound");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {    
    // Assert that the command sender has a location
    var senderLocation = context.assertSenderHasLocation();
    
    // Validate the number of arguments
    if (!context.hasAtLeastArgumentsCount(this.getType() == QueryType.CONDITIONAL ? 3 : 2))
      throw new CommandUsageException();
    
    // Create a scanner for the arguments
    var scanner = context.getArgumentsScanner();
    
    // Parse the properties
    var properties = this.getManager().getCartSoundPropertiesArgumentType("distance", "volume", "pitch").parse(scanner);
    var distance = this.getManager().getCartSearchDistanceProperty(properties, "distance");
    var volume = CommandUtils.clamp(this.getManager().getSoundVolumeProperty(properties, "volume"), 0.0f, 1.0f);
    var pitch = CommandUtils.clamp(this.getManager().getSoundPitchProperty(properties, "pitch"), 0.0f, 2.0f);
    
    // Parse the arguments
    var result = this.matchQueryMatcher(scanner, () -> this.getManager().findNearestCart(senderLocation, distance));
    
    // Execute the command
    if (this.getManager().playSound(context.getSender(), result.getValue(), result.getCart(), volume, pitch))
    {
      context.sendMessage(this.getManager().formatMessage("sound-played", Map.of(
        "sound", result.getValue())));
    }
  }
}
