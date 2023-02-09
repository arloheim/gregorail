package dev.danae.gregorail;

import dev.danae.gregorail.commands.CommandGroupHandler;
import dev.danae.gregorail.handlers.LocateCartCommand;
import dev.danae.gregorail.handlers.LocateCommand;
import org.bukkit.plugin.java.JavaPlugin;


public class RailPlugin extends JavaPlugin
{
  // Enable the plugin
  @Override
  public void onEnable()
  {
    // Register the command handlers
    this.getCommand("rail").setExecutor(new CommandGroupHandler(this)
      .registerSubcommand("locate", new LocateCommand(this))
      .registerSubcommand("locatecart", new LocateCartCommand(this)));
  }
  
  // Disable the plugin
  @Override
  public void onDisable()
  {
  }
  
  
  // Run a delayed task
  public void runDelayed(Runnable runnable, long delayTicks)
  {
    this.getServer().getScheduler().scheduleSyncDelayedTask(this, runnable, delayTicks);
  }
  
  // Run a repeating task
  public void runRepeating(Runnable runnable, long delayTicks, long periodTicks)
  {
    this.getServer().getScheduler().scheduleSyncRepeatingTask(this, runnable, delayTicks, periodTicks);
  }
}
