package dev.danae.gregorail.plugin.commands.rail;

import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.commands.ManagerQueryCommand;
import dev.danae.gregorail.plugin.commands.ManagerQueryCommandType;
import dev.danae.gregorail.plugin.Formatter;
import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandUsageException;
import dev.danae.gregorail.util.commands.CommandUtils;
import dev.danae.gregorail.util.parser.ParserException;
import java.util.List;


public class RailSoundCommand extends ManagerQueryCommand
{  
  // Constructor
  public RailSoundCommand(Manager manager, ManagerQueryCommandType type)
  {
    super(manager, type, "gregorail.rail.sound");
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
      if (!context.hasAtLeastArgumentsCount(this.getType() == ManagerQueryCommandType.CONDITIONAL ? 3 : 2))
        throw new CommandUsageException();
      
      // Create a scanner for the arguments
      var scanner = context.getArgumentsScanner();
      
      // Parse the properties
      var properties = scanner.wrapInPropertyBag();
      var distance = properties.getUnsignedInt("distance", this.getManager().getCartSearchDistance());
      var volume = CommandUtils.clamp(properties.getFloat("volume", 1.0f), 0.0f, 1.0f);
      var pitch = CommandUtils.clamp(properties.getFloat("pitch", 1.0f), 0.0f, 2.0f);
      
      // Parse the arguments
      var result = this.matchQueryMatcher(scanner, () -> scanner.nextNamespacedKey(), () -> this.getManager().findNearestCart(senderLocation, distance));
      
      // Execute the command
      if (this.getManager().playSound(senderLocation, result.getValue(), result.getCart(), volume, pitch))
        context.sendMessage(Formatter.formatPlaySoundMessage(senderLocation, result.getValue(), result.getCart()));
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
    switch (this.getType())
    {
      case ALWAYS:
      {
        if (context.hasAtLeastArgumentsCount(2))
          return this.handleLocationTabCompletion(context, 1, false);
        else if (context.hasArgumentsCount(1))
          return this.handleSoundTabCompletion(context.getArgument(0));
        else
          return null;
      }
        
      case CONDITIONAL:
      {
        var separatorIndex = context.findLastArgumentIndex("||");
        if (separatorIndex == 2)
          return this.handleSoundTabCompletion(context.getLastArgument(0));
        else if (separatorIndex == 1)
          return this.handleCodesTabCompletion(context.getLastArgument(0));
        else if (context.hasAtLeastArgumentsCount(3))
          return this.handleLocationTabCompletion(context, separatorIndex >= 0 ? context.getArgumentsCount() - separatorIndex + 2 : 2, true);
        else if (context.hasArgumentsCount(2))
          return this.handleSoundTabCompletion(context.getArgument(1));
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
