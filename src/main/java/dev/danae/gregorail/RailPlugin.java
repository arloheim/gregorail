package dev.danae.gregorail;

import dev.danae.gregorail.commands.CommandGroupHandler;
import dev.danae.gregorail.handlers.AssignCommand;
import dev.danae.gregorail.handlers.LocateCartCommand;
import dev.danae.gregorail.handlers.LocateCommand;
import dev.danae.gregorail.handlers.SwitchCommand;
import dev.danae.gregorail.handlers.SwitchIfCommand;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;


public final class RailPlugin extends JavaPlugin
{
  // Namespaced key for storing the code of a minecart
  private NamespacedKey minecartCodeKey;
  
  
  // Return the Namespaced key for storing the code of a minecart
  public NamespacedKey getMinecartCodeKey()
  {
    return this.minecartCodeKey;
  }
  
  
  // Enable the plugin
  @Override
  public void onEnable()
  {
    // Set the namespaced keys
    this.minecartCodeKey = new NamespacedKey(this, "minecart_code");
    
    // Register the command handlers
    this.getCommand("rail").setExecutor(new CommandGroupHandler(this)
      .registerSubcommand("locate", new LocateCommand(this))
      .registerSubcommand("locatecart", new LocateCartCommand(this))
      .registerSubcommand("assign", new AssignCommand(this))
      .registerSubcommand("switch", new SwitchCommand(this))
      .registerSubcommand("switchif", new SwitchIfCommand(this)));
  }
  
  // Disable the plugin
  @Override
  public void onDisable()
  {
  }
}
