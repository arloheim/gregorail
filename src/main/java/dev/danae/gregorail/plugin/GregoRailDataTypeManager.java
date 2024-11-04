package dev.danae.gregorail.plugin;

import dev.danae.gregorail.model.persistence.CodeDataType;
import dev.danae.gregorail.model.persistence.DataTypeManager;
import dev.danae.gregorail.model.persistence.MinecartDataType;
import org.bukkit.NamespacedKey;


public class GregoRailDataTypeManager extends GregoRailPluginComponent implements DataTypeManager
{
  // Persistent data types
  private final MinecartDataType minecartDataType;
  private final CodeDataType codeDataType;


  // Constructor
  public GregoRailDataTypeManager(GregoRailPlugin plugin)
  {
    super(plugin);

    this.minecartDataType = new MinecartDataType(this.getManager());
    this.codeDataType = new CodeDataType(this.getManager());
  }


  // Return the persistent minecart data type
  @Override
  public MinecartDataType getMinecartDataType()
  {
    return this.minecartDataType;
  }

  // Return the persistent code data type
  @Override
  public CodeDataType getCodeDataType()
  {
    return this.codeDataType;
  }

  // Return a namespaced key with the namespace of the manager
  @Override
  public NamespacedKey createNamespacedKey(String key)
  {
    return new NamespacedKey(this.getPlugin(), key);
  }
}
