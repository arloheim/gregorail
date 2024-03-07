package dev.danae.gregorail.model;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;


public class CodeTag implements ConfigurationSerializable
{
  // The name of the tag
  private final String name;

  // The URL of the tag
  private final String url;


  // Constructor
  public CodeTag(String name, String url)
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
  public CodeTag withName(String name)
  {
    return new CodeTag(name, this.url);
  }

  // Return the URL of the tag
  public String getUrl()
  {
    return this.url;
  }

  // Return a new tag with the specified URL
  public CodeTag withUrl(String url)
  {
    return new CodeTag(this.name, url);
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
  public static CodeTag deserialize(Map<String, Object> map)
  {
    var name = (String)map.getOrDefault("name", null);
    var url = (String)map.getOrDefault("url", null);
    return new CodeTag(name, url);
  }

  // Return an empty code tag
  public static CodeTag empty()
  {
    return new CodeTag(null, null);
  }
}
