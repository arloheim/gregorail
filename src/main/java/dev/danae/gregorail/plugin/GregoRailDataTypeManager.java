package dev.danae.gregorail.plugin;

import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.model.ManagerComponent;
import dev.danae.gregorail.model.persistence.CodeDataType;
import dev.danae.gregorail.model.persistence.DataTypeManager;
import dev.danae.gregorail.model.persistence.MinecartDataType;
import org.bukkit.NamespacedKey;


public class GregoRailDataTypeManager extends ManagerComponent implements DataTypeManager
{
  // The namespace for namespaced keys
  private static final String NAMESPACE = "gregorail";


  // Persistent data types
  private final MinecartDataType minecartDataType;
  private final CodeDataType codeDataType;


  // Constructor
  public GregoRailDataTypeManager(Manager manager)
  {
    super(manager);

    this.minecartDataType = new MinecartDataType(manager);
    this.codeDataType = new CodeDataType(manager);
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
    return new NamespacedKey(NAMESPACE, key);
  }
}
