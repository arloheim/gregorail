package dev.danae.gregorail.model.minecart;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;


public class MinecartCodeTag implements ConfigurationSerializable
{
  // The name of the tag
  private final String name;

  // The URL of the tag
  private final String url;


  // Constructor
  public MinecartCodeTag(String name, String url)
  {
    this.name = name;
    this.url = url;
  }


  // Return the name of the tag
  public String getName()
  {
    return this.name;
  }

  // Return a new tag with the specified name
  public MinecartCodeTag withName(String name)
  {
    return new MinecartCodeTag(name, this.url);
  }

  // Return the URL of the tag
  public String getUrl()
  {
    return this.url;
  }

  // Return a new tag with the specified URL
  public MinecartCodeTag withUrl(String url)
  {
    return new MinecartCodeTag(this.name, url);
  }


  // Serialize the tag to a map representation
  public Map<String, Object> serialize()
  {
    var map = new HashMap<String, Object>();
    map.put("name", this.name);
    map.put("url", this.url);
    return map;
  }
  
  // Deserialize a map representation to a tag
  public static MinecartCodeTag deserialize(Map<String, Object> map)
  {
    var name = (String)map.getOrDefault("name", null);
    var url = (String)map.getOrDefault("url", null);
    return new MinecartCodeTag(name, url);
  }
}
