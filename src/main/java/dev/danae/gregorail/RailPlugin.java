package dev.danae.gregorail;

import dev.danae.gregorail.commands.CommandGroupHandler;
import dev.danae.gregorail.handlers.AssignCommand;
import dev.danae.gregorail.handlers.LocateCartCommand;
import dev.danae.gregorail.handlers.LocateCommand;
import dev.danae.gregorail.handlers.SwitchCommand;
import dev.danae.gregorail.handlers.SwitchIfCommand;
import dev.danae.gregorail.handlers.UnassignCommand;
import org.bukkit.plugin.java.JavaPlugin;


public final class RailPlugin extends JavaPlugin
{
  // The static plugin instance
  private static RailPlugin instance;
  
  
  // Return the static plugin instance
  public static RailPlugin getInstance()
  {
    return instance;
  }
  
  
  // Enable the plugin
  @Override
  public void onEnable()
  {
    // Set the static plugin instance
    instance = this;
    
    // Register the command handlers
    this.getCommand("rail").setExecutor(new CommandGroupHandler(this)
      .registerSubcommand("locate", new LocateCommand(this))
      .registerSubcommand("locatecart", new LocateCartCommand(this))
      .registerSubcommand("assign", new AssignCommand(this))
      .registerSubcommand("switch", new SwitchCommand(this))
      .registerSubcommand("switchif", new SwitchIfCommand(this))
      .registerSubcommand("unassign", new UnassignCommand(this)));
  }
  
  // Disable the plugin
  @Override
  public void onDisable()
  {
  }
}
