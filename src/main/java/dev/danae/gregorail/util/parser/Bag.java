package dev.danae.gregorail.util.parser;

import dev.danae.gregorail.model.minecart.MinecartCode;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import dev.danae.gregorail.model.query.Query;


public class Bag
{  
  // The properties of the property bag
  private final Map<String, String> properties;
  
  
  // Constructor
  public Bag()
  {
    this.properties = new HashMap<>();
  }
  
  // Constructor with existing properties
  public Bag(Map<String, String> properties)
  {    
    this.properties = properties;
  }
  
  
  // Add a property
  public void add(String key, String value)
  {
    this.properties.put(key, value);
  }
  
  // Remove a property
  public void remove(String key)
  {
    this.properties.remove(key);
  }
  
   // Return if the bag contains a property with the specified key
  public boolean has(String key)
  {
    return this.properties.containsKey(key);
  }
  
  // Return a property parsed with the specified function
  private <T> T get(String key, ParserFunction<String, T> parser, T defaultValue) throws ParserException
  {
    var value = this.properties.get(key);
    return value != null ? parser.apply(value) : defaultValue;
  }
  
  
  // Return a property
  public String getString(String key)
  {
    return this.properties.get(key);
  }
  
  // Return a property with a default value if it doesn't exist
  public String getString(String key, String defaultValue)
  {
    return this.properties.getOrDefault(key, defaultValue);
  }
  
  // Return a property as an integer, or the default value if no such element exists
  public int getInt(String key, int defaultValue) throws ParserException
  {
    return this.get(key, Parser::parseInt, defaultValue);
  }
  
  // Return a property as a long, or the default value if no such element exists
  public long getLong(String key, long defaultValue) throws ParserException
  {
    return this.get(key, Parser::parseLong, defaultValue);
  }
  
  // Return a property as an unsigned integer, or the default value if no such element exists
  public int getUnsignedInt(String key, int defaultValue) throws ParserException
  {
    return this.get(key, Parser::parseUnsignedInt, defaultValue);
  }
  
  // Return a property as an unsigned long, or the default value if no such element exists
  public long getUnsignedLong(String key, long defaultValue) throws ParserException
  {
    return this.get(key, Parser::parseUnsignedLong, defaultValue);
  }
  
  // Return a property as a float, or the default value if no such element exists
  public float getFloat(String key, float defaultValue) throws ParserException
  {
    return this.get(key, Parser::parseFloat, defaultValue);
  }
  
  // Return a property as a double, or the default value if no such element exists
  public double getDouble(String key, double defaultValue) throws ParserException
  {
    return this.get(key, Parser::parseDouble, defaultValue);
  }
  
  // Return a property as an enum of the specified class, or the default value if no such element exists
  public <T extends Enum<T>> T getEnum(String key, Class<T> cls, T defaultValue) throws ParserException
  {
    return this.get(key, s -> Parser.parseEnum(s, cls), defaultValue);
  }
  
  // Return a property as an enum set of the specified class, or the default value if no such element exists
  public <T extends Enum<T>> EnumSet<T> getEnumSet(String key, Class<T> cls, EnumSet<T> defaultValue) throws ParserException
  {
    return this.get(key, s -> Parser.parseEnumSet(Arrays.asList(s.split(",")), cls), defaultValue);
  }
  
  // Return a property as a material, or the default value if no such element exists
  public Material getMaterial(String key, boolean requireBlock, Material defaultValue) throws ParserException
  {
    return this.get(key, s -> Parser.parseMaterial(s, requireBlock), defaultValue);
  }
  
  // Return a property as a code, or the default value if no such element exists
  public MinecartCode getCode(String key, MinecartCode defaultValue) throws ParserException
  {
    return this.get(key, Parser::parseCode, defaultValue);
  }
  
  // Return a property as a list of codes, or the default value if no such element exists
  public List<MinecartCode> getCodeList(String key, List<MinecartCode> defaultValue) throws ParserException
  {
    return this.get(key, Parser::parseCodeList, defaultValue);
  }
  
  // Return a property as a query, or the default value if no such element exists
  public Query getQuery(String key, Query defaultValue) throws ParserException
  {
    return this.get(key, Parser::parseQuery, defaultValue);
  }
  
  // Return a property as a location, or the default value if no such element exists
  public Location getLocation(String key, Location origin, int radius, Location defaultValue) throws ParserException
  {
    return this.get(key, s -> Parser.parseLocation(s, origin, radius), defaultValue);
  }
}
