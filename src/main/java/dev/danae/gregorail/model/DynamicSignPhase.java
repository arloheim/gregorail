package dev.danae.gregorail.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.bukkit.DyeColor;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.configuration.serialization.ConfigurationSerializable;


public class DynamicSignPhase implements ConfigurationSerializable
{
  // The lines of the sign phase
  private List<String> lines = new LinkedList<>();
  
  // The color of the sign phase
  private DyeColor color = DyeColor.BLACK;
  
  // Does the sign phase have glowing text?
  private boolean glowingText = false;
  
  // The duration of the sign phase
  private int duration = 5;
  
  
  // Return the lines of the sign phase
  public Collection<String> getLines()
  {
    return this.lines;
  }

  // Return the line with the specified index from the lines of the sign phase
  public String getLine(int index)
  {
    return this.lines.get(index);
  }

  // Add a line into the lines of the sign phase
  public void addLine(String line)
  {
    this.lines.add(line);
  }

  // Set a line into the lines of the sign phase
  public void setLine(int index, String line)
  {
    this.lines.set(index, line);
  }

  // Remove the line with the specified index from the lines of the sign phase
  public void removeLine(int index)
  {
    this.lines.remove(index);
  }
  
  // Return the color of the sign phase
  public DyeColor getColor()
  {
    return this.color;
  }
  
  // Set the color of the sign phase
  public void setColor(DyeColor color)
  {
    this.color = color;
  }
  
  // Return if the sign phase has glowing text
  public boolean isGlowingText()
  {
    return this.glowingText;
  }
  
  // Set if the sign phase has glowing text
  public void setGlowingText(boolean glowingText)
  {
    this.glowingText = glowingText;
  }
  
  // Return the duration of the sign phase
  public int getDuration()
  {
    return this.duration;
  }
  
  // Set the duration of the sign phase
  public void setDuration(int duration)
  {
    this.duration = duration;
  }
  
  
  // Apply the sign phase to a sign side
  public void apply(SignSide side)
  {
    for (var i = 0; i < Math.min(4, this.lines.size()); i++)
      side.setLine(i, this.lines.get(i));
    side.setColor(this.color);
    side.setGlowingText(this.glowingText);
  }
  
  // Apply the sign phase to both sides of a sign
  public void apply(Sign sign)
  {
    this.apply(sign.getSide(Side.FRONT));
    this.apply(sign.getSide(Side.BACK));
  }


  // Serialize the dynamic sign phase to a map representation
  public Map<String, Object> serialize()
  {
    var map = new HashMap<String, Object>();
    map.put("lines", this.lines);
    map.put("color", this.color.toString());
    map.put("glowingText", this.glowingText);
    map.put("duration", this.duration);
    return map;
  }
  
  // Deserialize a map representation to a dynamic sign phase
  public static DynamicSignPhase deserialize(Map<String, Object> map)
  {
    var dynamicSignPhase = new DynamicSignPhase();
    dynamicSignPhase.lines = (List<String>)map.get("lines");
    dynamicSignPhase.color = Enum.valueOf(DyeColor.class, (String)map.get("color"));
    dynamicSignPhase.glowingText = (boolean)map.get("glowingText");
    dynamicSignPhase.duration = (int)map.get("duration");
    return dynamicSignPhase;
  }
}
