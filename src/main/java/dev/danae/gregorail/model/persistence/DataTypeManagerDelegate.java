package dev.danae.gregorail.model.persistence;

import org.bukkit.NamespacedKey;


public interface DataTypeManagerDelegate extends DataTypeManager 
{
  // Return the data type manager
  public DataTypeManager getDataTypeManager();


  // Return the persistent minecart data type
  public default MinecartDataType getMinecartDataType()
  {
    return this.getDataTypeManager().getMinecartDataType();
  }
  
  // Return the persistent code data type
  public default CodeDataType getCodeDataType()
  {
    return this.getDataTypeManager().getCodeDataType();
  }

  // Return a namespaced key with the namespace of the manager
  public default NamespacedKey createNamespacedKey(String key)
  {
    return this.getDataTypeManager().createNamespacedKey(key);
  }
}
