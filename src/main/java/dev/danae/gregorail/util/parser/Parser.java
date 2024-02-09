package dev.danae.gregorail.util.parser;

import java.util.EnumSet;
import java.util.LinkedList;
import org.bukkit.Material;


public class Parser
{ 
  // Parse an integer value from a string
  public static int parseInt(String string) throws ParserException
  {
    try
    {
      return Integer.parseInt(string);
    }
    catch (NumberFormatException ex)
    {
      throw new ParserException(String.format("\"%s\" is not a valid integer format", string), ex);
    }
  }
  
  // Parse a long value from a string
  public static long parseLong(String string) throws ParserException
  {
    try
    {
      return Long.parseLong(string);
    }
    catch (NumberFormatException ex)
    {
      throw new ParserException(String.format("\"%s\" is an invalid integer format", string), ex);
    }
  }
  
  // Parse an unsigned integer value from a string
  public static int parseUnsignedInt(String string) throws ParserException
  {
    try
    {
      return Integer.parseUnsignedInt(string);
    }
    catch (NumberFormatException ex)
    {
      throw new ParserException(String.format("\"%s\" is an invalid unsigned integer format", string), ex);
    }
  }
  
  // Parse an unsigned long value from a string
  public static long parseUnsignedLong(String string) throws ParserException
  {
    try
    {
      return Long.parseUnsignedLong(string);
    }
    catch (NumberFormatException ex)
    {
      throw new ParserException(String.format("\"%s\" is an invalid unsigned integer format", string), ex);
    }
  }
  
  // Parse a float value from a string
  public static float parseFloat(String string) throws ParserException
  {
    try
    {
      return Float.parseFloat(string);
    }
    catch (NumberFormatException ex)
    {
      throw new ParserException(String.format("\"%s\" is an invalid float format", string), ex);
    }
  }
  
  // Parse a double value from a string
  public static double parseDouble(String string) throws ParserException
  {
    try
    {
      return Double.parseDouble(string);
    }
    catch (NumberFormatException ex)
    {
      throw new ParserException(String.format("\"%s\" is an invalid float format", string), ex);
    }
  }
  
  // Parse an enum value from a string using case-insensitive matching
  public static <T extends Enum<T>> T parseEnum(String string, Class<T> cls) throws ParserException
  {
    try
    {
      return Enum.valueOf(cls, string.toUpperCase());
    }
    catch (IllegalArgumentException ex)
    {
      throw new ParserException(String.format("\"%s\" is an invalid value for enum %s", string, cls.getName()), ex);
    }
  }
  
  // Parse an enum set value from an iterable of strings using case-insensitive matching
  public static <T extends Enum<T>> EnumSet<T> parseEnumSet(Iterable<String> strings, Class<T> cls) throws ParserException
  {
    var list = new LinkedList<T>();
    for (var string : strings)
      list.add(parseEnum(string, cls));
    return EnumSet.copyOf(list);
  }
  
  // Parse a material from a string
  public static Material parseMaterial(String string, boolean requireBlock) throws ParserException
  {
    var material = Material.matchMaterial(string);
    if (material == null)
      throw new ParserException(String.format("\"%s\" is an invalid material", string.toLowerCase()));
    if (requireBlock && !material.isBlock())
      throw new ParserException(String.format("\"%s\" is not a block material", string.toLowerCase()));
      
    return material;
  }
}
