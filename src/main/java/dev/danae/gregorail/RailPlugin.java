package dev.danae.gregorail;

import dev.danae.gregorail.commands.CommandGroupHandler;
import dev.danae.gregorail.handlers.admin.AdminReloadCommand;
import dev.danae.gregorail.handlers.admin.AdminVersionCommand;
import dev.danae.gregorail.handlers.cart.CartSetCommand;
import dev.danae.gregorail.handlers.cart.CartUnsetCommand;
import dev.danae.gregorail.handlers.locate.LocateCartCommand;
import dev.danae.gregorail.handlers.locate.LocateBlockCommand;
import dev.danae.gregorail.handlers.rail.RailSwitchCommand;
import dev.danae.gregorail.handlers.rail.RailSwitchIfCommand;
import dev.danae.gregorail.util.location.LocationUtils;
import java.util.logging.Level;
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
    
    // Load the configuration
    this.loadConfiguration();
    
    // Load the command handlers
    this.loadCommandHandlers();
  }
  
  
  // Reload the plugin
  public void reload()
  {
    // Load the configuration
    this.loadConfiguration();
  }
  
  
  // Load the configuration
  private void loadConfiguration()
  {
    this.saveDefaultConfig();
    this.reloadConfig();
    
    var generalConfig = this.getConfig().getConfigurationSection("general");
    if (generalConfig != null)
    {
      LocationUtils.blockSearchRadius = generalConfig.getInt("block-search-radius", 10);
      this.getLogger().log(Level.INFO, String.format("general.block-search-radius = %d", LocationUtils.blockSearchRadius));
      
      LocationUtils.entitySearchRadius = generalConfig.getInt("entity-search-radius", 10);
      this.getLogger().log(Level.INFO, String.format("general.entity-search-radius = %d", LocationUtils.entitySearchRadius));
    }
  }
  
  // Load the command handlers
  private void loadCommandHandlers()
  {
    this.getCommand("gregorail").setExecutor(new CommandGroupHandler(this)
      .registerSubcommand("reload", new AdminReloadCommand(this))
      .registerSubcommand("version", new AdminVersionCommand(this)));
    
    this.getCommand("gcart").setExecutor(new CommandGroupHandler(this)
      .registerSubcommand("set", new CartSetCommand(this))
      .registerSubcommand("unset", new CartUnsetCommand(this)));
    
    this.getCommand("grail").setExecutor(new CommandGroupHandler(this)
      .registerSubcommand("switch", new RailSwitchCommand(this))
      .registerSubcommand("switchif", new RailSwitchIfCommand(this)));
    
    this.getCommand("glocate").setExecutor(new CommandGroupHandler(this)
      .registerSubcommand("block", new LocateBlockCommand(this))
      .registerSubcommand("cart", new LocateCartCommand(this)));
  }
}
