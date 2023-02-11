package dev.danae.gregorail.util;

import java.util.EnumSet;
import java.util.List;


public class EnumUtils
{
  // Parse an enum from a string using case-insensitive matching
  public static <T extends Enum<T>> T parseEnum(String string, Class<T> cls)
  {
    return Enum.valueOf(cls, string.toUpperCase());
  }
  
  // Parse an enum set from a collection of strings using case-insensitive matching
  public static <T extends Enum<T>> EnumSet<T> parseEnumSet(List<String> strings, Class<T> cls)
  {
    return EnumSet.copyOf(strings.stream()
      .map(s -> parseEnum(s, cls))
      .toList());
  }
}
