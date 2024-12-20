package dev.danae.gregorail.plugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import dev.danae.gregorail.plugin.butcher.ButcherOptions;
import dev.danae.gregorail.plugin.butcher.Butcher;
import dev.danae.common.commands.CommandGroup;
import dev.danae.common.commands.arguments.ArgumentException;
import dev.danae.common.commands.arguments.ArgumentType;
import dev.danae.gregorail.model.CodeTag;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.commands.QueryType;
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
import dev.danae.gregorail.plugin.configuration.ConfigurationMap;
import dev.danae.gregorail.plugin.configuration.ConfigurationMapKeyType;
import dev.danae.gregorail.plugin.migrations.v1_1_0.CodeDisplayNamesMigration;
import dev.danae.gregorail.plugin.migrations.v2_1_0.CodeTagNamesToMiniMessageMigration;
import dev.danae.gregorail.plugin.commands.tag.TagClearCommand;
import dev.danae.gregorail.plugin.commands.tag.TagListCommand;
import dev.danae.gregorail.plugin.webhooks.Webhook;
import dev.danae.gregorail.plugin.webhooks.WebhookType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
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
  

  // Load the plugin
  @Override
  public void onLoad()
  {
    // Create the manager
    this.manager = new GregoRailManager(this, this.options);

    // Register serializable classes for the configuration API
    ConfigurationSerialization.registerClass(CodeTag.class);

    // Load the plugin
    this.loadPlugin();
  }
  
  // Enable the plugin
  @Override
  public void onEnable()
  {    
    // Create the componenets
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
      .publishCommandHandler(this, "gregorail");

    new CommandGroup()
      .registerSubcommand("clear", new TagClearCommand(this.manager))
      .registerSubcommand("list", new TagListCommand(this.manager))
      .registerSubcommand("remove", new TagRemoveCommand(this.manager))
      .registerSubcommand("set", new TagSetCommand(this.manager))
      .publishCommandHandler(this, "gtag");
      
    new CommandGroup()
      .registerSubcommand("clear", new CartClearCommand(this.manager, QueryType.ALWAYS))
      .registerSubcommand("clearif", new CartClearCommand(this.manager, QueryType.CONDITIONAL))
      .registerSubcommand("promptset", new CartPromptSetCommand(this.manager, QueryType.ALWAYS, this.cartPromptOptions))
      .registerSubcommand("promptsetif", new CartPromptSetCommand(this.manager, QueryType.CONDITIONAL, this.cartPromptOptions))
      .registerSubcommand("set", new CartSetCommand(this.manager, QueryType.ALWAYS))
      .registerSubcommand("setif", new CartSetCommand(this.manager, QueryType.CONDITIONAL))
      .registerSubcommand("speed", new CartSpeedCommand(this.manager, QueryType.ALWAYS))
      .registerSubcommand("speedif", new CartSpeedCommand(this.manager, QueryType.CONDITIONAL))
      .publishCommandHandler(this, "gcart");
    
    new CommandGroup()
      .registerSubcommand("block", new RailBlockCommand(this.manager, QueryType.ALWAYS))
      .registerSubcommand("blockif", new RailBlockCommand(this.manager, QueryType.CONDITIONAL))
      .registerSubcommand("sound", new RailSoundCommand(this.manager, QueryType.ALWAYS))
      .registerSubcommand("soundif", new RailSoundCommand(this.manager, QueryType.CONDITIONAL))
      .registerSubcommand("switch", new RailSwitchCommand(this.manager, QueryType.ALWAYS))
      .registerSubcommand("switchif", new RailSwitchCommand(this.manager, QueryType.CONDITIONAL))
      .publishCommandHandler(this, "grail");
    
    new CommandGroup()
      .registerSubcommand("block", new LocateBlockCommand(this.manager))
      .registerSubcommand("cart", new LocateCartCommand(this.manager))
      .publishCommandHandler(this, "glocate");
  }


  // Load the plugin
  public void loadPlugin()
  {
    // Execute migrations
    this.executeMigrations();

    // Load the configuration
    this.loadConfiguration();

    // Load the messages
    this.loadMessages();
  }

  // Execute migrations
  public void executeMigrations()
  {
    this.getLogger().log(Level.INFO, "Checking for migrations to execute...");

    // Execute v1.1.0 migrations
    new CodeDisplayNamesMigration(this).migrate();

    // Execute v2.1.0 migrations
    new CodeTagNamesToMiniMessageMigration(this).migrate();
  }
  
  // Load the configuration
  public void loadConfiguration()
  {
    this.getLogger().log(Level.INFO, "Loading configuration...");

    // Save the default configuration
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
        
          var webhookTypes = ArgumentType.getEnumSetArgumentType(WebhookType.class, "").parseFromStringList(webhookConfig.getStringList("type"));
          var webhookUrl = new URL(webhookConfig.getString("url"));
          this.options.getWebhooks().add(new Webhook(this, webhookName, webhookTypes, webhookUrl));
        }
        catch (ArgumentException | MalformedURLException ex)
        {
          this.getLogger().log(Level.WARNING, String.format("Could not load the configuration for the webhook with name %s, so it will remain disabled", webhookName), ex);
        }
      }
    }
    
    // Load the cart prompt configuration
    var cartPromptConfig = this.getConfig().getConfigurationSection("cart-prompt");
    if (cartPromptConfig != null)
    {
      this.cartPromptOptions.setTitle(this.manager.getMessageFormatter().format(cartPromptConfig.getString("title", "Select a code")));
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
        this.butcherOptions.setIgnoreEntitiesOfType(ArgumentType.getEnumSetArgumentType(EntityType.class, "").parseFromStringList(butcherConfig.getStringList("ignore-entities-of-type")));
        this.butcherOptions.setIgnoreNamedEntities(butcherConfig.getBoolean("ignore-named-entities", true));
        this.butcherOptions.setLightningBoltEffect(butcherConfig.getBoolean("lightning-bolt-effect", true));
        this.butcherOptions.setDisableItemDrops(butcherConfig.getBoolean("disable-item-drops", true));
      }
      catch (ArgumentException ex)
      {
        this.getLogger().log(Level.WARNING, "Could not load the configuration for the butcher, so it will remain disabled", ex);
        this.butcherOptions.setEnabled(false);
      }
    }
  }

  // Load the messages
  public void loadMessages()
  {
    this.getLogger().log(Level.INFO, "Loading messages...");

    // Load the messages
    this.manager.loadMessagesFromConfiguration(this, "messages.yml", Map.ofEntries(
      // General messages
      Map.entry("plugin-reloaded", "<name> has been reloaded"),
      Map.entry("copy-to-clipboard", "Click to copy \"<text>\" to clipboard"),
      Map.entry("location-format", "<blue><location></blue>"),
      Map.entry("block-format", "block <material> at <location>"),
      Map.entry("block-found", "Found <block>"),
      Map.entry("block-not-found", "<red>No block found</red>"),
      Map.entry("cart-format", "minecart with code <code> at <location>"),
      Map.entry("cart-found", "Found <cart>"),
      Map.entry("cart-not-found", "<red>No minecart found</red>"),
      
      // Messages for the /gtag command
      Map.entry("tag-list", "<count> code tags are defined"),
      Map.entry("tag-list-item", "- <code>: name = <name:'empty'>, url = <url:'empty'>"),
      Map.entry("tag-name-changed", "The name of code tag <green><code></green> has been changed to <name>"),
      Map.entry("tag-url-changed", "The URL of code tag <green><code></green> has been changed to <url>"),
      Map.entry("tag-name-cleared", "The name of code tag <green><code></green> has been cleared"),
      Map.entry("tag-url-cleared", "The URL of code tag <green><code></green> has been cleared"),
      Map.entry("tag-removed", "The code tag <green><code></green> has been removed"),
      
      // Messages for the /gcart command
      Map.entry("cart-code-changed", "The code of <cart> was changed from <green><original-code></green>"),
      Map.entry("cart-code-cleared", "The code of <cart> was cleared from <green><original-code></green>"),
      Map.entry("cart-code-retained", "The code of <cart> was unchanged from <green><original-code></green>"),
      Map.entry("cart-code-speed-multiplier-changed", "The speed multiplier of <cart> was changed from <green><original-speed-multiplier:'#.0'> to <green><speed-multiplier:'#.0'></green>"),
      Map.entry("cart-code-speed-multiplier-retained", "The speed multiplier of <cart> was unchanged from <green><original-speed-multiplier:'#.0'></green>"),
      
      // Messages for the /grail command
      Map.entry("block-shape-changed", "The shape of <block> was changed from <green><original-shape></green> to <green><shape></green>"),
      Map.entry("block-shape-retained", "The shape of <block> was unchanged from <green><original-shape></green>"),
      Map.entry("block-material-changed", "The material of <block> was changed from <green><original-material> to <green><material></green>"),
      Map.entry("block-material-retained", "The material of <block> was unchanged from <green><original-material></green>"),
      Map.entry("sound-played", "Sound <sound> was played")
    ));
  }


  // Create a new configuration map
  public <K, V extends ConfigurationSerializable> ConfigurationMap<K, V> createConfigurationMap(String file, Class<V> clazz, ConfigurationMapKeyType<K> keyType)
  {
    return new ConfigurationMap<>(this, new File(this.getDataFolder(), file), clazz, keyType);
  }
}
