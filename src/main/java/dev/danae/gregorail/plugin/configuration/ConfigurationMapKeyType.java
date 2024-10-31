package dev.danae.gregorail.plugin.configuration;

import dev.danae.gregorail.model.Code;
import org.bukkit.NamespacedKey;


public interface ConfigurationMapKeyType<K>
{
  // Static key type instances
  public static ConfigurationMapKeyType<String> STRING = new StringKeyType();
  public static ConfigurationMapKeyType<NamespacedKey> NAMESPACED_KEY = new NamespacedKeyKeyType();
  public static ConfigurationMapKeyType<Code> CODE = new CodeKeyType();


  // Convert the specified key to its string representation
  public String toString(K key);

  // Convert the specified string to its key representation
  public K toKey(String string);


  
  // Class that defines a configuration map key type for a code tag
  public static class StringKeyType implements ConfigurationMapKeyType<String>
  {
    // Convert the specified key to its string representation
    public String toString(String key)
    {
      return key;
    }

    // Convert the specified string to its key representation
    public String toKey(String string)
    {
      return string;
    }
  }


  // Class that defines a configuration map key type for a namespaced key
  public static class NamespacedKeyKeyType implements ConfigurationMapKeyType<NamespacedKey>
  {
    // Convert the specified key to its string representation
    public String toString(NamespacedKey key)
    {
      return key.toString();
    }

    // Convert the specified string to its key representation
    public NamespacedKey toKey(String string)
    {
      return NamespacedKey.fromString(string);
    }
  }


  // Class that defines a configuration map key type for a code tag
  public static class CodeKeyType implements ConfigurationMapKeyType<Code>
  {
    // Convert the specified key to its string representation
    public String toString(Code key)
    {
      return key.getId();
    }

    // Convert the specified string to its key representation
    public Code toKey(String string)
    {
      return Code.of(string);
    }
  }
}
