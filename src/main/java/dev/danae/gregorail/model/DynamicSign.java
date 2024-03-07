package dev.danae.gregorail.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;


public class DynamicSign implements ConfigurationSerializable
{
  // The phases of the sign
  private List<DynamicSignPhase> phases = new LinkedList<>();
  
  
  // Return the phases of the sign
  public Collection<DynamicSignPhase> getPhases()
  {
    return this.phases;
  }

  // Return the phase with the specified index from the phases of the sign
  public DynamicSignPhase getPhase(int index)
  {
    return this.phases.get(index);
  }

  // Add a phase into the phases of the sign
  public void addPhase(DynamicSignPhase phase)
  {
    this.phases.add(phase);
  }

  // Set a phase into the phases of the sign
  public void setPhase(int index, DynamicSignPhase phase)
  {
    this.phases.set(index, phase);
  }

  // Remove the phase with the specified index from the phases of the sign
  public void removePhase(int index)
  {
    this.phases.remove(index);
  }


  // Serialize the properties to a map representation
  public Map<String, Object> serialize()
  {
    var map = new HashMap<String, Object>();
    map.put("phases", this.phases);
    return map;
  }
  
  // Deserialize a map representation to a properties
  public static DynamicSign deserialize(Map<String, Object> map)
  {
    var dynamicSign = new DynamicSign();
    dynamicSign.phases = (List<DynamicSignPhase>)map.get("phases");
    return dynamicSign;
  }
}
