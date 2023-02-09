package dev.danae.gregorail;

import dev.danae.gregorail.commands.CommandGroupHandler;
import dev.danae.gregorail.handlers.LocateCartCommand;
import dev.danae.gregorail.handlers.LocateCommand;
import dev.danae.gregorail.handlers.SwitchCommand;
import org.bukkit.plugin.java.JavaPlugin;


public final class RailPlugin extends JavaPlugin
{
  // Enable the plugin
  @Override
  public void onEnable()
  {
    // Register the command handlers
    this.getCommand("rail").setExecutor(new CommandGroupHandler(this)
      .registerSubcommand("locate", new LocateCommand(this))
      .registerSubcommand("locatecart", new LocateCartCommand(this))
      .registerSubcommand("switch", new SwitchCommand(this)));
  }
  
  // Disable the plugin
  @Override
  public void onDisable()
  {
  }
}
