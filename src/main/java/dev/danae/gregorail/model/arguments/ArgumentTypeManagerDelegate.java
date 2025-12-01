package dev.danae.gregorail.model.arguments;

import java.util.List;
import dev.danae.common.commands.arguments.ArgumentType;
import dev.danae.common.commands.arguments.PropertyList;
import dev.danae.common.commands.arguments.StringArgumentType;
import dev.danae.gregorail.model.Code;
import dev.danae.gregorail.model.Query;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.data.Rail;


public interface ArgumentTypeManagerDelegate extends ArgumentTypeManager
{
  // Return the argument type manager
  public ArgumentTypeManager getArgumentTypeManager();


  // Return the query argument type
  public default StringArgumentType<Query> getQueryArgumentType()
  {
    return this.getArgumentTypeManager().getQueryArgumentType();
  }

  // Return the code argument type
  public default StringArgumentType<Code> getCodeArgumentType()
  {
    return this.getArgumentTypeManager().getCodeArgumentType();
  }

  // Return the code query matcher argument type
  public default QueryMatcherArgumentType<Code> getCodeQueryMatcherArgumentType()
  {
    return this.getArgumentTypeManager().getCodeQueryMatcherArgumentType();
  }

  // Return the code list argument type
  public default StringArgumentType<List<Code>> getCodeListArgumentType()
  {
    return this.getArgumentTypeManager().getCodeListArgumentType();
  }

  // Return the code list query matcher argument type
  public default QueryMatcherArgumentType<List<Code>> getCodeListQueryMatcherArgumentType()
  {
    return this.getArgumentTypeManager().getCodeListQueryMatcherArgumentType();
  }

  // Return the speed multiplier argument type
  public default StringArgumentType<Double> getSpeedMultiplierArgumentType()
  {
    return this.getArgumentTypeManager().getSpeedMultiplierArgumentType();
  }

  // Return the speed multiplier query matcher argument type
  public default QueryMatcherArgumentType<Double> getSpeedMultiplierQueryMatcherArgumentType()
  {
    return this.getArgumentTypeManager().getSpeedMultiplierQueryMatcherArgumentType();
  }

  // Return the block shape argument type
  public default StringArgumentType<Rail.Shape> getBlockShapeArgumentType()
  {
    return this.getArgumentTypeManager().getBlockShapeArgumentType();
  }

  // Return the block shape query matcher argument type
  public default QueryMatcherArgumentType<Rail.Shape> getBlockShapeQueryMatcherArgumentType()
  {
    return this.getArgumentTypeManager().getBlockShapeQueryMatcherArgumentType();
  }

  // Return the block material argument type
  public default StringArgumentType<Material> getBlockMaterialArgumentType()
  {
    return this.getArgumentTypeManager().getBlockMaterialArgumentType();
  }

  // Return the block material query matcher argument type
  public default QueryMatcherArgumentType<Material> getBlockMaterialQueryMatcherArgumentType()
  {
    return this.getArgumentTypeManager().getBlockMaterialQueryMatcherArgumentType();
  }

  // Return the sound argument type
  public default StringArgumentType<NamespacedKey> getSoundArgumentType()
  {
    return this.getArgumentTypeManager().getSoundArgumentType();
  }

  // Return the sound query matcher argument type
  public default QueryMatcherArgumentType<NamespacedKey> getSoundQueryMatcherArgumentType()
  {
    return this.getArgumentTypeManager().getSoundQueryMatcherArgumentType();
  }

  
  // Return a location argument type for the specified origin and block search radius
  public default ArgumentType<Location> getLocationArgumentType(Location origin, int blockSearchRadius)
  {
    return this.getArgumentTypeManager().getLocationArgumentType(origin, blockSearchRadius);
  }

  // Return a location argument type for the specified origin
  public default ArgumentType<Location> getLocationArgumentType(Location origin)
  {
    return this.getArgumentTypeManager().getLocationArgumentType(origin);
  }

  // Return a properties argument type for block commands
  public default ArgumentType<PropertyList> getBlockPropertiesArgumentType(String blockSearchRadiusName)
  {
    return this.getArgumentTypeManager().getBlockPropertiesArgumentType(blockSearchRadiusName);
  }

  // Return a properties argument type for cart block commands
  public default ArgumentType<PropertyList> getCartBlockPropertiesArgumentType(String blockSearchRadiusName, String cartSearchRadiusName)
  {
    return this.getArgumentTypeManager().getCartBlockPropertiesArgumentType(blockSearchRadiusName, cartSearchRadiusName);
  }

  // Return a properties argument type for cart sound commands
  public default ArgumentType<PropertyList> getCartSoundPropertiesArgumentType(String cartSearchRadiusName, String soundVolumeName, String soundPitchName)
  {
    return this.getArgumentTypeManager().getCartSoundPropertiesArgumentType(cartSearchRadiusName, soundVolumeName, soundPitchName);
  }


  // Return the property with the specified name from the properties as a block search radius type
  public default int getBlockSearchRadiusProperty(PropertyList properties, String name)
  {
    return this.getArgumentTypeManager().getBlockSearchRadiusProperty(properties, name);
  }

  // Return the property with the specified name from the properties as a cart search distance type
  public default int getCartSearchDistanceProperty(PropertyList properties, String name)
  {
    return this.getArgumentTypeManager().getCartSearchDistanceProperty(properties, name);
  }

  // Return the property with the specified name from the properties as a sound volume type
  public default float getSoundVolumeProperty(PropertyList properties, String name)
  {
    return this.getArgumentTypeManager().getSoundVolumeProperty(properties, name);
  }

  // Return the property with the specified name from the properties as a sound pitch type
  public default float getSoundPitchProperty(PropertyList properties, String name)
  {
    return this.getArgumentTypeManager().getSoundPitchProperty(properties, name);
  }
}
