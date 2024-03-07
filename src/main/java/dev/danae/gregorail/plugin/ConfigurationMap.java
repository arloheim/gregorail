package dev.danae.gregorail.plugin;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;


public class ConfigurationMap<K, V extends ConfigurationSerializable> extends GregoRailPluginComponent implements Map<K, V>
{
  // The file where the map is stored
  private final File file;

  // The type of the elements in the map
  private final Class<V> clazz;

  // The key type of the map
  private final ConfigurationMapKeyType<K> keyType;
  
  // The backing map
  private final Map<K, V> map = new HashMap<>();
  
  
  // Constructor
  public ConfigurationMap(GregoRailPlugin plugin, File file, Class<V> clazz, ConfigurationMapKeyType<K> keyType)
  {
    super(plugin);
    
    this.file = file;
    this.clazz = clazz;
    this.keyType = keyType;

    this.load();
  }
  
  
  // Return the keys in the map
  @Override
  public Set<K> keySet()
  {
    return this.map.keySet();
  }
  
  // Return the values in the map
  @Override
  public Collection<V> values()
  {
    return this.map.values();
  }
  
  // Return the entries in the map
  @Override
  public Set<Map.Entry<K, V>> entrySet()
  {
    return this.map.entrySet();
  }
  
  // Return the size of the map
  @Override
  public int size()
  {
    return this.map.size();
  }

  // Return if the map is empty
  @Override
  public boolean isEmpty()
  {
    return this.map.isEmpty();
  }

  // Return if the map contains the specified key
  @Override
  public boolean containsKey(Object key)
  {
    return this.map.containsKey(key);
  }

  // Return if the map contains the specified value
  @Override
  public boolean containsValue(Object value)
  {
    return this.map.containsValue(value);
  }
  
  // Get an item from the map
  @Override
  public V get(Object key)
  {
    return this.map.get(key);
  }
  
  // Put an item into the map
  @Override
  public V put(K key, V value)
  {
    V original = this.map.put(key, value);
    this.save();
    return original;
  }
  
  // Put all items in the specified map into the map
  @Override
  public void putAll(Map<? extends K, ? extends V> m)
  {
    this.map.putAll(m);
    this.save();
  }
  
  // Remove an item from the map
  @Override
  public V remove(Object key)
  {
    V original = this.map.remove(key);
    this.save();
    return original;
  }
  
  // Clear the map
  @Override
  public void clear()
  {
    this.map.clear();
    this.save();
  }
  
  
  // Load the map from the file
  protected void load()
  {
    try
    {
      if (!this.file.exists())
        this.file.createNewFile();
      
      var config = new YamlConfiguration();
      config.load(this.file);
      
      this.map.clear();
      for (String key : config.getKeys(false))
        this.map.put(this.keyType.toKey(key), config.getSerializable(key, this.clazz));
    }
    catch (IOException | InvalidConfigurationException ex)
    {
      this.getPlugin().getLogger().log(Level.WARNING, "Could not load the defined display names", ex);
    }
  }
  
  // Save the map to the file
  protected void save()
  {
    try
    {
      var config = new YamlConfiguration();
      for (var e : this.map.entrySet())
        config.set(this.keyType.toString(e.getKey()), e.getValue());
      
      config.save(this.file);
    }
    catch (IOException ex)
    {
      this.getPlugin().getLogger().log(Level.WARNING, "Could not save the defined display names", ex);
    }
  }
}
