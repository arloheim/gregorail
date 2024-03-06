package dev.danae.gregorail.plugin;

import dev.danae.gregorail.model.ManagerComponent;


public abstract class GregoRailPluginComponent extends ManagerComponent
{
  // The plugin of the component
  private final GregoRailPlugin plugin;
  
  
  // Constructor
  public GregoRailPluginComponent(GregoRailPlugin plugin)
  {
    super(plugin.getManager());
    
    this.plugin = plugin;
  }
  
  
  // Return the plugin of the component
  protected GregoRailPlugin getPlugin()
  {
    return this.plugin;
  }
}
