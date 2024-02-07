package dev.danae.gregorail;

import com.google.gson.JsonObject;
import dev.danae.gregorail.butcher.ButcherListener;
import dev.danae.gregorail.butcher.ButcherOptions;
import dev.danae.gregorail.commands.CommandExecutionType;
import dev.danae.gregorail.commands.admin.AdminCodeListCommand;
import dev.danae.gregorail.commands.admin.AdminCodeRemoveCommand;
import dev.danae.gregorail.commands.admin.AdminCodeSetCommand;
import dev.danae.gregorail.commands.admin.AdminReloadCommand;
import dev.danae.gregorail.commands.admin.AdminVersionCommand;
import dev.danae.gregorail.commands.cart.CartClearCommand;
import dev.danae.gregorail.commands.cart.CartPromptOptions;
import dev.danae.gregorail.commands.cart.CartPromptSetCommand;
import dev.danae.gregorail.commands.cart.CartSetCommand;
import dev.danae.gregorail.commands.cart.CartSpeedCommand;
import dev.danae.gregorail.commands.locate.LocateBlockCommand;
import dev.danae.gregorail.commands.locate.LocateCartCommand;
import dev.danae.gregorail.commands.rail.RailBlockCommand;
import dev.danae.gregorail.commands.rail.RailSwitchCommand;
import dev.danae.gregorail.util.EnumUtils;
import dev.danae.gregorail.util.commands.CommandGroupHandler;
import dev.danae.gregorail.util.commands.CommandHandler;
import dev.danae.gregorail.util.webhooks.Webhook;
import dev.danae.gregorail.util.webhooks.WebhookExecutor;
import dev.danae.gregorail.util.webhooks.WebhookType;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;


public final class RailPlugin extends JavaPlugin implements WebhookExecutor
{
  // The static plugin instance
  private static RailPlugin instance;
  
  
  // The distance in blocks to search for blocks while parsing a location
  private int blockSearchRadius = 10;
  
  // The distance in blocks to search for entities while setting or querying codes
  private int entitySearchRadius = 10;
  
  // The options for the prompt
  private final CartPromptOptions cartPromptOptions = new CartPromptOptions();
  
  // The options for the butcher listener
  private final ButcherOptions butcherOptions = new ButcherOptions();
  
  // The registered webhooks
  private final List<Webhook> webhooks = new LinkedList<>();
  
  
  // Return the static plugin instance
  public static RailPlugin getInstance()
  {
    return instance;
  }
  
  // Return the distance in blocks to search for blocks while parsing a location
  public static int getBlockSearchRadius()
  {
    return instance.blockSearchRadius;
  }
  
  // Return the distance in blocks to search for entities while setting or querying codes
  public static int getEntitySearchRadius()
  {
    return instance.entitySearchRadius;
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
      this.blockSearchRadius = generalConfig.getInt("block-search-radius", 10);      
      this.entitySearchRadius = generalConfig.getInt("entity-search-radius", 10);
    }
    
    var cartPromptConfig = this.getConfig().getConfigurationSection("cart-prompt");
    if (cartPromptConfig != null)
    {
      this.cartPromptOptions.setTitle(cartPromptConfig.getString("title", "Select a code"));
      this.cartPromptOptions.setItemMaterial(Material.matchMaterial(cartPromptConfig.getString("item-material", Material.MINECART.name())));
      this.cartPromptOptions.setPlayerSearchRadius(cartPromptConfig.getInt("player-search-radius", 5));
   }
    
    var butcherConfig = this.getConfig().getConfigurationSection("butcher");
    if (butcherConfig != null)
    {
      try
      {
        this.butcherOptions.setEnabled(butcherConfig.getBoolean("enabled", true));
        this.butcherOptions.setRadius(butcherConfig.getInt("radius", 5)); 
        this.butcherOptions.setIgnoreEntitiesOfType(EnumUtils.parseEnumSet(butcherConfig.getStringList("ignore-entities-of-type"), EntityType.class));
        this.butcherOptions.setIgnoreNamedEntities(butcherConfig.getBoolean("ignore-named-entities", true));
        this.butcherOptions.setLightningBoltEffect(butcherConfig.getBoolean("lightning-bolt-effect", true));
        this.butcherOptions.setDisableItemDrops(butcherConfig.getBoolean("disable-item-drops", true));
      }
      catch (IllegalArgumentException | NullPointerException ex)
      {
        this.getLogger().log(Level.WARNING, "Could not load the configuration for the butcher listener, so it will remain disabled", ex);
        this.butcherOptions.setEnabled(false);
      }
    }
    
    var webhooksConfig = this.getConfig().getConfigurationSection("webhooks");
    if (webhooksConfig != null)
    {
      for (var webhookName : webhooksConfig.getKeys(false))
      {
        try
        {
          var webhookConfig = webhooksConfig.getConfigurationSection(webhookName);
        
          var webhookTypes = EnumUtils.parseEnumSet(webhookConfig.getStringList("type"), WebhookType.class);
          var webhookUrl = new URL(webhookConfig.getString("url"));
          this.webhooks.add(new Webhook(webhookName, webhookTypes, webhookUrl));
        }
        catch (IllegalArgumentException | NullPointerException | MalformedURLException ex)
        {
          this.getLogger().log(Level.WARNING, String.format("Could not load the configuration for webhook %s, so it will remain disabled", webhookName), ex);
        }
      }
    }
  }
  
  // Load the command handlers
  private void loadCommandHandlers()
  {
    var adminCommandHandler = new CommandGroupHandler()
      .registerSubcommand("code", new CommandGroupHandler()
        .registerSubcommand("list", new AdminCodeListCommand())
        .registerSubcommand("remove", new AdminCodeRemoveCommand())
        .registerSubcommand("set", new AdminCodeSetCommand()))
      .registerSubcommand("reload", new AdminReloadCommand())
      .registerSubcommand("version", new AdminVersionCommand());
    
    var cartCommandHandler = new CommandGroupHandler()
      .registerSubcommand("clear", new CartClearCommand(CommandExecutionType.ALWAYS))
      .registerSubcommand("clearif", new CartClearCommand(CommandExecutionType.CONDITIONAL))
      .registerSubcommand("promptset", new CartPromptSetCommand(this.cartPromptOptions))
      .registerSubcommand("set", new CartSetCommand(CommandExecutionType.ALWAYS))
      .registerSubcommand("setif", new CartSetCommand(CommandExecutionType.CONDITIONAL))
      .registerSubcommand("speed", new CartSpeedCommand(CommandExecutionType.ALWAYS))
      .registerSubcommand("speedif", new CartSpeedCommand(CommandExecutionType.CONDITIONAL));
    
    var railCommandHandler = new CommandGroupHandler()
      .registerSubcommand("block", new RailBlockCommand(CommandExecutionType.ALWAYS))
      .registerSubcommand("blockif", new RailBlockCommand(CommandExecutionType.CONDITIONAL))
      .registerSubcommand("switch", new RailSwitchCommand(CommandExecutionType.ALWAYS))
      .registerSubcommand("switchif", new RailSwitchCommand(CommandExecutionType.CONDITIONAL));
    
    var locateCommandHandler = new CommandGroupHandler()
      .registerSubcommand("block", new LocateBlockCommand())
      .registerSubcommand("cart", new LocateCartCommand());
    
    this.setCommandHandler("gregorail", adminCommandHandler);
    this.setCommandHandler("gcart", cartCommandHandler);
    this.setCommandHandler("grail", railCommandHandler);
    this.setCommandHandler("glocate", locateCommandHandler);
  }
  
  // Load the listeners
  private void loadListeners()
  {
    this.getServer().getPluginManager().registerEvents(new RailListener(), this);
    this.getServer().getPluginManager().registerEvents(new ButcherListener(this.butcherOptions), this);
  }
  
  
  // Set the handler of a command to the specified command handler
  private void setCommandHandler(String name, CommandHandler handler)
  {
    this.getCommand(name).setExecutor(handler);
    this.getCommand(name).setTabCompleter(handler);
    
    handler.registerListener();
  }

  
  // Execute a webhook with the specified type and payload
  @Override
  public void executeWebhook(WebhookType type, JsonObject payload)
  {
    for (var webhook : this.webhooks)
    {
      if (webhook.getType().contains(type))
        webhook.execute(payload);
    }
  }
}
