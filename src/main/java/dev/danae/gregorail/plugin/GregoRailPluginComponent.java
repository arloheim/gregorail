package dev.danae.gregorail.plugin;

import dev.danae.gregorail.model.Manager;

public abstract class GregoRailPluginComponent
{
  // The plugin of the component
  private final GregoRailPlugin plugin;
  
  
  // Constructor
  public GregoRailPluginComponent(GregoRailPlugin plugin)
  {
    this.plugin = plugin;
  }
  
  
  // Return the plugin of the component
  protected GregoRailPlugin getPlugin()
  {
    return this.plugin;
  }

  // Return the manager of the component
  protected Manager getManager()
  {
    return this.plugin.getManager();
  }
}
