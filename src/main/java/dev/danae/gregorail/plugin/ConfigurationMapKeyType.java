package dev.danae.gregorail.plugin;

public interface ConfigurationMapKeyType<K>
{
  // Convert the specified key to its string representation
  public String toString(K key);

  // Convert the specified string to its key representation
  public K toKey(String string);
}
