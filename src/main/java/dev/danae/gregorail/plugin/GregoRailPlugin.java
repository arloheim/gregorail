package dev.danae.gregorail.plugin;

import dev.danae.gregorail.plugin.butcher.ButcherOptions;
import dev.danae.gregorail.plugin.butcher.Butcher;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.commands.QueryCommandType;
import dev.danae.gregorail.plugin.commands.admin.AdminReloadCommand;
import dev.danae.gregorail.plugin.commands.admin.AdminVersionCommand;
import dev.danae.gregorail.plugin.commands.cart.CartClearCommand;
import dev.danae.gregorail.plugin.commands.cart.CartPromptOptions;
import dev.danae.gregorail.plugin.commands.cart.CartPromptSetCommand;
import dev.danae.gregorail.plugin.commands.cart.CartSetCommand;
import dev.danae.gregorail.plugin.commands.cart.CartSpeedCommand;
import dev.danae.gregorail.plugin.commands.locate.LocateBlockCommand;
import dev.danae.gregorail.plugin.commands.locate.LocateCartCommand;
import dev.danae.gregorail.plugin.commands.rail.RailBlockCommand;
import dev.danae.gregorail.plugin.commands.rail.RailSoundCommand;
import dev.danae.gregorail.plugin.commands.rail.RailSwitchCommand;
import dev.danae.gregorail.plugin.commands.tag.TagRemoveCommand;
import dev.danae.gregorail.plugin.commands.tag.TagSetCommand;
import dev.danae.gregorail.plugin.commands.tag.TagClearCommand;
import dev.danae.gregorail.plugin.commands.tag.TagListCommand;
import dev.danae.gregorail.plugin.commands.CommandGroup;
import dev.danae.gregorail.util.parser.Parser;
import dev.danae.gregorail.util.parser.ParserException;
import dev.danae.gregorail.plugin.webhooks.Webhook;
import dev.danae.gregorail.plugin.webhooks.WebhookType;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;


public final class GregoRailPlugin extends JavaPlugin
{  
  // The manager of the plugin
  private GregoRailManager manager;
  
  // The listener of the plugin
  private GregoRailListener listener;
  
  // The webhook executor of the plugin
  private GregoRailWebhookExecutor webhookExecutor;
  
  // The butcher of the plugin
  private Butcher butcher;
  
  
  // The options of the plugin
  private final GregoRailPluginOptions options = new GregoRailPluginOptions();
  
  // The options for the cart prompt
  private final CartPromptOptions cartPromptOptions = new CartPromptOptions();
  
  // The options for the butcher listener
  private final ButcherOptions butcherOptions = new ButcherOptions();
  
  
  // Return the manager of the plugin
  public Manager getManager()
  {
    return this.manager;
  }
  
  
  // Enable the plugin
  @Override
  public void onEnable()
  {    
    // Load the configuration
    this.loadConfiguration();
    
    // Create the componenets
    this.manager = new GregoRailManager(this, this.options);
    this.listener = new GregoRailListener(this);
    this.webhookExecutor = new GregoRailWebhookExecutor(this, this.options);
    this.butcher = new Butcher(this, this.butcherOptions);
    
    // Set the listeners
    Bukkit.getPluginManager().registerEvents(this.listener, this);
    Bukkit.getPluginManager().registerEvents(this.webhookExecutor, this);
    Bukkit.getPluginManager().registerEvents(this.butcher, this);
    
    // Set the command handlers    
    new CommandGroup()
      .registerSubcommand("reload", new AdminReloadCommand(this.manager, this))
      .registerSubcommand("version", new AdminVersionCommand(this.manager, this))
      .publishCommandHandler(this, this.getCommand("gregorail"));

    new CommandGroup()
      .registerSubcommand("clear", new TagClearCommand(this.manager))
      .registerSubcommand("list", new TagListCommand(this.manager))
      .registerSubcommand("remove", new TagRemoveCommand(this.manager))
      .registerSubcommand("set", new TagSetCommand(this.manager))
      .publishCommandHandler(this, this.getCommand("gtag"));
      
    new CommandGroup()
      .registerSubcommand("clear", new CartClearCommand(this.manager, QueryCommandType.ALWAYS))
      .registerSubcommand("clearif", new CartClearCommand(this.manager, QueryCommandType.CONDITIONAL))
      .registerSubcommand("promptset", new CartPromptSetCommand(this.manager, QueryCommandType.ALWAYS, this.cartPromptOptions, this))
      .registerSubcommand("promptsetif", new CartPromptSetCommand(this.manager, QueryCommandType.CONDITIONAL, this.cartPromptOptions, this))
      .registerSubcommand("set", new CartSetCommand(this.manager, QueryCommandType.ALWAYS))
      .registerSubcommand("setif", new CartSetCommand(this.manager, QueryCommandType.CONDITIONAL))
      .registerSubcommand("speed", new CartSpeedCommand(this.manager, QueryCommandType.ALWAYS))
      .registerSubcommand("speedif", new CartSpeedCommand(this.manager, QueryCommandType.CONDITIONAL))
      .publishCommandHandler(this, this.getCommand("gcart"));
    
    new CommandGroup()
      .registerSubcommand("block", new RailBlockCommand(this.manager, QueryCommandType.ALWAYS))
      .registerSubcommand("blockif", new RailBlockCommand(this.manager, QueryCommandType.CONDITIONAL))
      .registerSubcommand("sound", new RailSoundCommand(this.manager, QueryCommandType.ALWAYS))
      .registerSubcommand("soundif", new RailSoundCommand(this.manager, QueryCommandType.CONDITIONAL))
      .registerSubcommand("switch", new RailSwitchCommand(this.manager, QueryCommandType.ALWAYS))
      .registerSubcommand("switchif", new RailSwitchCommand(this.manager, QueryCommandType.CONDITIONAL))
      .publishCommandHandler(this, this.getCommand("grail"));
    
    new CommandGroup()
      .registerSubcommand("block", new LocateBlockCommand(this.manager))
      .registerSubcommand("cart", new LocateCartCommand(this.manager))
      .publishCommandHandler(this, this.getCommand("glocate"));
  }
  
  
  // Load the configuration
  public void loadConfiguration()
  {
    this.saveDefaultConfig();
    this.reloadConfig();
    
    // Load the general configuration
    var generalConfig = this.getConfig().getConfigurationSection("general");
    if (generalConfig != null)
    {
      this.options.setBlockSearchRadius(generalConfig.getInt("block-search-radius", 10));
      this.options.setCartSearchDistance(generalConfig.getInt("cart-search-distance", 10));
      this.options.setWebhooksEnabled(generalConfig.getBoolean("webhooks-enabled", true));
    }
    
    // Load the webhook configuration
    var webhooksConfig = this.getConfig().getConfigurationSection("webhooks");
    if (webhooksConfig != null && this.options.isWebhooksEnabled())
    {
      for (var webhookName : webhooksConfig.getKeys(false))
      {
        try
        {
          var webhookConfig = webhooksConfig.getConfigurationSection(webhookName);
          if (webhookConfig == null)
            continue;
        
          var webhookTypes = Parser.parseEnumSet(webhookConfig.getStringList("type"), WebhookType.class);
          var webhookUrl = new URL(webhookConfig.getString("url"));
          this.options.getWebhooks().add(new Webhook(this, webhookName, webhookTypes, webhookUrl));
        }
        catch (ParserException | MalformedURLException ex)
        {
          this.getLogger().log(Level.WARNING, String.format("Could not load the configuration for the webhook with name %s, so it will remain disabled", webhookName), ex);
        }
      }
    }
    
    // Load the cart prompt configuration
    var cartPromptConfig = this.getConfig().getConfigurationSection("cart-prompt");
    if (cartPromptConfig != null)
    {
      this.cartPromptOptions.setTitle(cartPromptConfig.getString("title", "Select a code"));
      this.cartPromptOptions.setItemMaterial(cartPromptConfig.contains("item-material") ? Material.matchMaterial(cartPromptConfig.getString("item-material")) : Material.MINECART);
      this.cartPromptOptions.setPlayerSearchRadius(cartPromptConfig.getInt("player-search-radius", 5));
    }
    
    // Load the butcher configuration
    var butcherConfig = this.getConfig().getConfigurationSection("butcher");
    if (butcherConfig != null)
    {
      try
      {
        this.butcherOptions.setEnabled(butcherConfig.getBoolean("enabled", true));
        this.butcherOptions.setRadius(butcherConfig.getInt("radius", 5)); 
        this.butcherOptions.setIgnoreEntitiesOfType(Parser.parseEnumSet(butcherConfig.getStringList("ignore-entities-of-type"), EntityType.class));
        this.butcherOptions.setIgnoreNamedEntities(butcherConfig.getBoolean("ignore-named-entities", true));
        this.butcherOptions.setLightningBoltEffect(butcherConfig.getBoolean("lightning-bolt-effect", true));
        this.butcherOptions.setDisableItemDrops(butcherConfig.getBoolean("disable-item-drops", true));
      }
      catch (ParserException ex)
      {
        this.getLogger().log(Level.WARNING, "Could not load the configuration for the butcher, so it will remain disabled", ex);
        this.butcherOptions.setEnabled(false);
      }
    }
  }
}
