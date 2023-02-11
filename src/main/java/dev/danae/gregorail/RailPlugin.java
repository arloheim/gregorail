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
import dev.danae.gregorail.listeners.butcher.ButcherListener;
import dev.danae.gregorail.listeners.butcher.ButcherOptions;
import dev.danae.gregorail.util.EnumUtils;
import dev.danae.gregorail.util.location.LocationUtils;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;


public final class RailPlugin extends JavaPlugin
{
  // The static plugin instance
  private static RailPlugin instance;
  
  
  // The options for the butcher listener
  private final ButcherOptions butcherOptions = new ButcherOptions();
  
  
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
    
    // Load the listeners
    this.loadListeners();
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
    
    var butcherConfig = this.getConfig().getConfigurationSection("butcher");
    if (butcherConfig != null)
    {
      try
      {
        this.butcherOptions.setEnabled(butcherConfig.getBoolean("enabled", true));
        this.getLogger().log(Level.INFO, String.format("butcher.enabled = %s", this.butcherOptions.isEnabled() ? "true" : "false"));
        
        this.butcherOptions.setRadius(butcherConfig.getInt("radius", 5));
        this.getLogger().log(Level.INFO, String.format("butcher.radius = %d", this.butcherOptions.getRadius()));
        
        this.butcherOptions.setIgnoreEntitiesOfType(EnumUtils.parseEnumSet(butcherConfig.getStringList("ignore-entities-of-type"), EntityType.class));
        this.getLogger().log(Level.INFO, String.format("butcher.ignore-entities-of-type = %s", this.butcherOptions.getIgnoreEntitiesOfType().stream().map(e -> e.toString()).collect(Collectors.joining(", ", "[", "]"))));
        
        this.butcherOptions.setIgnoreNamedEntities(butcherConfig.getBoolean("ignore-named-entities", true));
        this.getLogger().log(Level.INFO, String.format("butcher.ignore-named-entities = %s", this.butcherOptions.isIgnoreNamedEntities() ? "true" : "false"));
        
        this.butcherOptions.setLightningBoltEffect(butcherConfig.getBoolean("lightning-bolt-effect", true));
        this.getLogger().log(Level.INFO, String.format("butcher.lightning-bolt-effect = %s", this.butcherOptions.isLightningBoltEffect() ? "true" : "false"));
      }
      catch (IllegalArgumentException | NullPointerException ex)
      {
        this.getLogger().log(Level.WARNING, ex, () -> "Could not load the configuration for the butcher listener, so it will remain disabled");
        
        this.butcherOptions.setEnabled(false);
      }
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
  
  // Load the listeners
  private void loadListeners()
  {
    this.getServer().getPluginManager().registerEvents(new ButcherListener(this, this.butcherOptions), this);
  }
}
