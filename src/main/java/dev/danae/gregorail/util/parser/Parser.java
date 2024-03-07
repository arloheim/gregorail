package dev.danae.gregorail.util.parser;

import dev.danae.gregorail.model.Code;
import dev.danae.gregorail.model.Query;
import dev.danae.gregorail.util.Cuboid;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;


public class Parser
{ 
  // Pattern for parsing strings
  private static final Pattern INT_PATTERN = Pattern.compile("0|-?[1-9][0-9]*");
  private static final Pattern UNSIGNED_INT_PATTERN = Pattern.compile("0|[1-9][0-9]*");
  private static final Pattern FLOAT_PATTERN = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
  private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
  private static final Pattern NAMESPACED_KEY_PATTERN = Pattern.compile("[a-zA-Z_][a-zA-Z0-9]*:[a-zA-Z_][a-zA-Z0-9]*(?:\\.[a-zA-Z_][a-zA-Z0-9]*)*");
  private static final Pattern LOCATION_PATTERN = Pattern.compile("(?<cur>~)|(?<xyz>(?:(?<x>0|-?[1-9][0-9]*)|(?<rx>~(?<dx>-?[1-9][0-9]*)?))\\s+(?:(?<y>0|-?[1-9][0-9]*)|(?<ry>~(?<dy>-?[1-9][0-9]*)?))\\s+(?:(?<z>0|-?[1-9][0-9]*)|(?<rz>~(?<dz>-?[1-9][0-9]*)?)))|(?<block>(?<mode>[@^])(?<name>[a-z_][a-z0-9_]*))");
  private static final Pattern CODE_PATTERN = Pattern.compile("(?<code>[a-zA-Z0-9_]+)|(?<empty>\\*)");
  private static final Pattern QUERY_PATTERN = Pattern.compile("(?<query>(?<suffix>\\*)?(?<code>[a-zA-Z0-9_]+)(?<prefix>\\*)?)|(?<all>\\*)");
  
  
  // Parse an integer value from a string
  public static int parseInt(String string) throws ParserException
  {
    try
    {
      // Match the string against the pattern
      var m = INT_PATTERN.matcher(string);
      if (!m.matches())
        throw new ParserException(String.format("\"%s\" is an invalid integer value", string)); 
      
      // Parse the value
      return Integer.parseInt(m.group());
    }
    catch (NumberFormatException ex)
    {
      throw new ParserException(String.format("\"%s\" is an invalid integer value", string), ex);
    }
  }
  
  // Parse a long value from a string
  public static long parseLong(String string) throws ParserException
  {
    try
    {
      // Match the string against the pattern
      var m = INT_PATTERN.matcher(string);
      if (!m.matches())
        throw new ParserException(String.format("\"%s\" is an invalid integer value", string));
      
      // Parse the value
      return Long.parseLong(m.group());
        
    }
    catch (NumberFormatException ex)
    {
      throw new ParserException(String.format("\"%s\" is an invalid integer value", string), ex);
    }
  }
  
  // Parse an unsigned integer value from a string
  public static int parseUnsignedInt(String string) throws ParserException
  {
    try
    {
      // Match the string against the pattern
      var m = UNSIGNED_INT_PATTERN.matcher(string);
      if (!m.matches())
        throw new ParserException(String.format("\"%s\" is an invalid unsigned integer value", string));  
      
      // Parse the value
      return Integer.parseUnsignedInt(m.group());
    }
    catch (NumberFormatException ex)
    {
      throw new ParserException(String.format("\"%s\" is an invalid unsigned integer value", string), ex);
    }
  }
  
  // Parse an unsigned long value from a string
  public static long parseUnsignedLong(String string) throws ParserException
  {
    try
    {
      // Match the string against the pattern
      var m = UNSIGNED_INT_PATTERN.matcher(string);
      if (!m.matches())
        throw new ParserException(String.format("\"%s\" is an invalid unsigned integer value", string));
      
      // Parse the value
      return Long.parseUnsignedLong(m.group());
    }
    catch (NumberFormatException ex)
    {
      throw new ParserException(String.format("\"%s\" is an invalid unsigned integer value", string), ex);
    }
  }
  
  // Parse a float value from a string
  public static float parseFloat(String string) throws ParserException
  {
    try
    {
      // Match the string against the pattern
      var m = FLOAT_PATTERN.matcher(string);
      if (!m.matches())
        throw new ParserException(String.format("\"%s\" is an invalid float value", string));
      
      // Parse the value
      return Float.parseFloat(m.group());
    }
    catch (NumberFormatException ex)
    {
      throw new ParserException(String.format("\"%s\" is an invalid float value", string), ex);
    }
  }
  
  // Parse a double value from a string
  public static double parseDouble(String string) throws ParserException
  {
    try
    {
      // Match the string against the pattern
      var m = FLOAT_PATTERN.matcher(string);
      if (!m.matches())
        throw new ParserException(String.format("\"%s\" is an invalid float value", string));
      
      // Parse the value
      return Double.parseDouble(m.group());
    }
    catch (NumberFormatException ex)
    {
      throw new ParserException(String.format("\"%s\" is an invalid float value", string), ex);
    }
  }
  
  // Parse an identifier from a string
  public static String parseIdentifier(String string) throws ParserException
  {
    // Match the string against the pattern
    var m = IDENTIFIER_PATTERN.matcher(string);
    if (!m.matches())
      throw new ParserException(String.format("\"%s\" is an invalid identifier value", string.toLowerCase()));
    
    // Return the value
    return string;
  }
  
  // Parse a namespaced key from a string
  public static NamespacedKey parseKey(String string) throws ParserException
  {
    // Match the string against the pattern
    var m = NAMESPACED_KEY_PATTERN.matcher(string);
    if (!m.matches())
      throw new ParserException(String.format("\"%s\" is an invalid namespaced key value", string.toLowerCase()));
    
    // Parse the value
    var key = NamespacedKey.fromString(string);
    if (key == null)
      throw new ParserException(String.format("\"%s\" is an invalid namespaced key value", string));
    return key;
  }
  
  // Parse an enum value from a string using case-insensitive matching
  public static <T extends Enum<T>> T parseEnum(String string, Class<T> cls) throws ParserException
  {
    try
    {
      // Match the string against the pattern
      var m = IDENTIFIER_PATTERN.matcher(string);
      if (!m.matches())
        throw new ParserException(String.format("\"%s\" is an invalid value for enum %s", string, cls.getName()));
      
      // Parse the value
      return Enum.valueOf(cls, m.group().toUpperCase());  
    }
    catch (IllegalArgumentException ex)
    {
      throw new ParserException(String.format("\"%s\" is an invalid value for enum %s", string, cls.getName()), ex);
    }
  }
  
  // Parse an enum set value from an iterable of strings using case-insensitive matching
  public static<T extends Enum<T>> EnumSet<T> parseEnumSet(Iterable<String> strings, Class<T> cls) throws ParserException
  {
    var list = new LinkedList<T>();
    for (var string : strings)
      list.add(parseEnum(string, cls));
    return EnumSet.copyOf(list);
  }
  
  // Parse an enum set value from a string using case-insensitive matching
  public static<T extends Enum<T>> EnumSet<T> parseEnumSet(String string, String splitRegex, Class<T> cls) throws ParserException
  {
    var strings = Arrays.asList(string.split(splitRegex));
    return parseEnumSet(strings, cls);
  }
  
  // Parse a material from a string
  public static Material parseMaterial(String string, boolean requireBlock) throws ParserException
  {
    // Match the string against the pattern
    var m = IDENTIFIER_PATTERN.matcher(string);
    if (!m.matches())
      throw new ParserException(String.format("\"%s\" is an invalid material value", string.toLowerCase()));
    
    // Parse the value
    var material = Material.matchMaterial(m.group());
    if (material == null)
      throw new ParserException(String.format("\"%s\" is an invalid material value", string.toLowerCase()));
    if (requireBlock && !material.isBlock())
      throw new ParserException(String.format("\"%s\" is not a block material", string.toLowerCase()));
    return material;
  }
  
  // Parse a code from a string
  public static Code parseCode(String string) throws ParserException
  {
    // Match the string against the pattern
    var m = CODE_PATTERN.matcher(string);
    if (!m.matches())
      throw new ParserException(String.format("\"%s\" is an invalid code; codes may only contain alphanumeric characters and underscores", string));
    
    // Check for a code
    if (m.group("code") != null)
      return Code.of(m.group("code"));
    
    // Check for an empty code
    if (m.group("empty") != null)
      return Code.empty();
    
    // Invalid code format
    throw new ParserException(String.format("\"%s\" is an invalid code value", string));
  }
  
  // Parse a list of codes from a string
  public static List<Code> parseCodeList(String string) throws ParserException
  {
    var list = new LinkedList<Code>();
    for (var stringComponent : string.split("\\|"))
      list.add(parseCode(stringComponent));
    return list;
  }
  
  // Parse a query from a string
  public static Query parseQuery(String string) throws ParserException
  {
    var list = new LinkedList<Query>();
    
    // Split the string into components
    for (var stringComponent : string.split("\\|"))
    {
      // Match the component against the pattern
      var m = QUERY_PATTERN.matcher(stringComponent);
      if (!m.matches())
        throw new ParserException(String.format("\"%s\" is an invalid query value", stringComponent));
      
      // Check for a code query
      if (m.group("query") != null)
      {
        // Parse the pattern
        if (m.group("prefix") != null && m.group("suffix") != null)
          list.add(Query.codeContains(m.group("code")));
        else if (m.group("prefix") != null)
          list.add(Query.codeStartsWith(m.group("code")));
        else if (m.group("suffix") != null)
          list.add(Query.codeEndsWith(m.group("code")));
        else
          list.add(Query.codeEquals(m.group("code")));
      }
      
      // Check for an all query
      else if (m.group("all") != null)
        list.add(Query.tautology());
      
      // Invalid query format
      else
        throw new ParserException(String.format("\"%s\" is an invalid query value", string));
    }
    
    // Return the query
    if (list.size() == 1)
      return list.get(0);
    else
      return Query.anyMatch(list);
  }
  
  // Parse a location from a string
  public static Location parseLocation(String string, Location origin, int radius) throws ParserException
  {
    // Match the string against the pattern
    var m = LOCATION_PATTERN.matcher(string);
    if (!m.matches())
      throw new ParserException(String.format("\"%s\" is an invalid location value", string));
    
    // Check for a current location
    if (m.group("cur") != null)
      return origin;
    
    // Check for a numeric location
    if (m.group("xyz") != null)
    {
      var x = m.group("rx") != null ? origin.getBlockX() + (m.group("dx") != null ? parseInt(m.group("dx")) : 0) : parseInt(m.group("x"));
      var y = m.group("ry") != null ? origin.getBlockY() + (m.group("dy") != null ? parseInt(m.group("dy")) : 0) : parseInt(m.group("y"));
      var z = m.group("rz") != null ? origin.getBlockZ() + (m.group("dz") != null ? parseInt(m.group("dz")) : 0) : parseInt(m.group("z"));
      
      return new Location(origin.getWorld(), x, y, z);
    }
    
    // Check for a block location
    if (m.group("block") != null)
    {      
      var mode = m.group("mode");
      var material = parseMaterial(m.group("name"), true);
      
      // Find the block
      var block = Cuboid.of(origin, radius).findNearestBlockToCenter(material);
      if (block == null)
        throw new ParserException(String.format("No block of material %s could be found in a distance of %d blocks", material.toString().toLowerCase(), radius));
      
      // Return the appropriate position based on the mode
      return switch (mode)
      {
        case "@" -> block.getLocation();
        case "^" -> block.getLocation().add(0, 1, 0);
        default -> throw new ParserException(String.format("\"%s\" is an invalid location value", string));
      };
    }
    
    // Invalid location format
    throw new ParserException(String.format("\"%s\" is an invalid location value", string));
  }
}
