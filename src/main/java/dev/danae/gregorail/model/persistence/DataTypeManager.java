package dev.danae.gregorail.model.persistence;

import org.bukkit.NamespacedKey;


public interface DataTypeManager 
{
  // Return the persistent minecart data type
  public MinecartDataType getMinecartDataType();
  
  // ReturnReturn the persistent code data type
  public CodeDataType getCodeDataType();

  // Return a namespaced key with the namespace of the manager
  public NamespacedKey createNamespacedKey(String key);
}
