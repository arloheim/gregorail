package dev.danae.gregorail;

import dev.danae.gregorail.commands.CommandGroupHandler;
import dev.danae.gregorail.handlers.admin.AdminVersionCommand;
import dev.danae.gregorail.handlers.cart.CartSetCommand;
import dev.danae.gregorail.handlers.cart.CartUnsetCommand;
import dev.danae.gregorail.handlers.locate.LocateCartCommand;
import dev.danae.gregorail.handlers.locate.LocateBlockCommand;
import dev.danae.gregorail.handlers.rail.RailSwitchCommand;
import dev.danae.gregorail.handlers.rail.RailSwitchIfCommand;
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
    this.getCommand("gcart").setExecutor(new CommandGroupHandler(this)
      .registerSubcommand("set", new CartSetCommand(this))
      .registerSubcommand("unset", new CartUnsetCommand(this)));
    
    this.getCommand("grail").setExecutor(new CommandGroupHandler(this)
      .registerSubcommand("switch", new RailSwitchCommand(this))
      .registerSubcommand("switchif", new RailSwitchIfCommand(this)));
    
    this.getCommand("glocate").setExecutor(new CommandGroupHandler(this)
      .registerSubcommand("block", new LocateBlockCommand(this))
      .registerSubcommand("cart", new LocateCartCommand(this)));
    
    this.getCommand("gadmin").setExecutor(new CommandGroupHandler(this)
      .registerSubcommand("version", new AdminVersionCommand(this)));
  }
  
  // Disable the plugin
  @Override
  public void onDisable()
  {
  }
}
