package dev.danae.gregorail.plugin.commands.admin;

import java.util.stream.Stream;
import dev.danae.common.commands.CommandContext;
import dev.danae.common.commands.CommandException;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.GregoRailPlugin;
import dev.danae.gregorail.plugin.commands.ManagerCommand;


public class AdminVersionCommand extends ManagerCommand
{
  // The plugin of the command
  private final GregoRailPlugin plugin;
  
  
  // Constructor
  public AdminVersionCommand(Manager manager, GregoRailPlugin plugin)
  {
    super(manager, "gregorail.admin");
    
    this.plugin = plugin;
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException
  {     
    // Send information about the version
    context.sendPlainMessage(String.format("%s %s", this.plugin.getPluginMeta().getName(), this.plugin.getPluginMeta().getVersion()));
  }

  // Return suggestions for the specified command context
  @Override
  public Stream<String> suggest(CommandContext context)
  {
    return Stream.empty();
  }
}
