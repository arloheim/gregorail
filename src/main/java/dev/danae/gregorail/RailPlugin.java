package dev.danae.gregorail;

import dev.danae.gregorail.commands.CommandGroupHandler;
import dev.danae.gregorail.handlers.LocateCommand;
import org.bukkit.plugin.java.JavaPlugin;


public class RailPlugin extends JavaPlugin
{
  // Enable the plugin
  @Override
  public void onEnable()
  {
    // Register the command handlers
    this.getCommand("rail").setExecutor(new CommandGroupHandler(this)
      .registerSubcommand("locate", new LocateCommand(this)));
  }
  
  // Disable the plugin
  @Override
  public void onDisable()
  {
  }
}
