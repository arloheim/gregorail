package dev.danae.gregorail;

import dev.danae.gregorail.commands.AdminReloadCommand;
import dev.danae.gregorail.commands.AdminVersionCommand;
import dev.danae.gregorail.commands.CartSetCommand;
import dev.danae.gregorail.commands.CartUnsetCommand;
import dev.danae.gregorail.commands.LocateBlockCommand;
import dev.danae.gregorail.commands.LocateCartCommand;
import dev.danae.gregorail.commands.RailBlockCommand;
import dev.danae.gregorail.commands.RailBlockIfCommand;
import dev.danae.gregorail.commands.RailSwitchCommand;
import dev.danae.gregorail.commands.RailSwitchIfCommand;
import dev.danae.gregorail.listeners.ButcherListener;
import dev.danae.gregorail.listeners.ButcherOptions;
import dev.danae.gregorail.util.EnumUtils;
import dev.danae.gregorail.util.commands.CommandGroupHandler;
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
        
        this.butcherOptions.setDisableItemDrops(butcherConfig.getBoolean("disable-item-drops", true));
        this.getLogger().log(Level.INFO, String.format("butcher.disable-item-drops = %s", this.butcherOptions.isDisableItemDrops()? "true" : "false"));
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
    this.getCommand("gregorail").setExecutor(new CommandGroupHandler()
      .registerSubcommand("reload", new AdminReloadCommand())
      .registerSubcommand("version", new AdminVersionCommand()));
    
    this.getCommand("gcart").setExecutor(new CommandGroupHandler()
      .registerSubcommand("set", new CartSetCommand())
      .registerSubcommand("unset", new CartUnsetCommand()));
    
    this.getCommand("grail").setExecutor(new CommandGroupHandler()
      .registerSubcommand("block", new RailBlockCommand())
      .registerSubcommand("blockif", new RailBlockIfCommand())
      .registerSubcommand("switch", new RailSwitchCommand())
      .registerSubcommand("switchif", new RailSwitchIfCommand()));
    
    this.getCommand("glocate").setExecutor(new CommandGroupHandler()
      .registerSubcommand("block", new LocateBlockCommand())
      .registerSubcommand("cart", new LocateCartCommand()));
  }
  
  // Load the listeners
  private void loadListeners()
  {
    this.getServer().getPluginManager().registerEvents(new ButcherListener(this.butcherOptions), this);
  }
}
