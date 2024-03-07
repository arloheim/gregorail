package dev.danae.gregorail.plugin.migrations;

import dev.danae.gregorail.plugin.GregoRailPlugin;
import dev.danae.gregorail.plugin.GregoRailPluginComponent;


public abstract class Migration extends GregoRailPluginComponent
{
  // Constructor
  public Migration(GregoRailPlugin plugin)
  {
    super(plugin);
  }

  
  // Execute the migration
  public void migrate()
  {
    if (this.canMigrate())
      this.doMigrate();
  }


  // Return the version the migration applies to
  protected abstract String getVersion();
  
  // Return if the prerequisites for the migrations are met
  protected abstract boolean canMigrate();
  
  // Actually execute the migration
  protected abstract void doMigrate();
}
