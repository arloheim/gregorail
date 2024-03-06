package dev.danae.gregorail.plugin.commands;

import dev.danae.gregorail.plugin.GregoRailPlugin;
import dev.danae.gregorail.plugin.Formatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;


public class CommandUtils
{    
  // Clamp an integer value
  public static int clamp(int value, int min, int max) 
  {
    return Math.max(min, Math.min(max, value));
  }
  
  // Clamp a float value
  public static float clamp(float value, float min, float max) 
  {
    return Math.max(min, Math.min(max, value));
  }
  
  // Clamp a double value
  public static double clamp(double value, double min, double max) 
  {
    return Math.max(min, Math.min(max, value));
  }
  
  
  // Handle tab completion for a property
  public static List<String> handlePropertyTabCompletion(String arg, String... properties)
  {
    if (!arg.startsWith("#"))
      return null;
    
    var stream = Arrays.stream(properties).map(p -> String.format("#%s", p)).sorted();
    
    if (!arg.isEmpty())
      return stream.filter(s -> s.startsWith(arg)).toList();
    else
      return stream.toList();
  }
}
