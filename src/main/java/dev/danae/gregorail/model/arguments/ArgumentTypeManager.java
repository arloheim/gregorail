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


public interface ArgumentTypeManager
{
  // Return the query argument type
  public StringArgumentType<Query> getQueryArgumentType();

  // Return the code argument type
  public StringArgumentType<Code> getCodeArgumentType();

  // Return the code query matcher argument type
  public QueryMatcherArgumentType<Code> getCodeQueryMatcherArgumentType();

  // Return the code list argument type
  public StringArgumentType<List<Code>> getCodeListArgumentType();

  // Return the code list query matcher argument type
  public QueryMatcherArgumentType<List<Code>> getCodeListQueryMatcherArgumentType();

  // Return the speed multiplier argument type
  public StringArgumentType<Double> getSpeedMultiplierArgumentType();

  // Return the speed multiplier query matcher argument type
  public QueryMatcherArgumentType<Double> getSpeedMultiplierQueryMatcherArgumentType();

  // Return the block shape argument type
  public StringArgumentType<Rail.Shape> getBlockShapeArgumentType();

  // Return the block shape query matcher argument type
  public QueryMatcherArgumentType<Rail.Shape> getBlockShapeQueryMatcherArgumentType();

  // Return the block material argument type
  public StringArgumentType<Material> getBlockMaterialArgumentType();

  // Return the block material query matcher argument type
  public QueryMatcherArgumentType<Material> getBlockMaterialQueryMatcherArgumentType();

  // Return the sound argument type
  public StringArgumentType<NamespacedKey> getSoundArgumentType();

  // Return the sound query matcher argument type
  public QueryMatcherArgumentType<NamespacedKey> getSoundQueryMatcherArgumentType();
  
  
  // Return a location argument type for the specified origin and block search radius
  public ArgumentType<Location> getLocationArgumentType(Location origin, int blockSearchRadius);

  // Return a location argument type for the specified origin
  public ArgumentType<Location> getLocationArgumentType(Location origin);

  // Return a properties argument type for block commands
  public ArgumentType<PropertyList> getBlockPropertiesArgumentType(String blockSearchRadiusName);

  // Return a properties argument type for cart block commands
  public ArgumentType<PropertyList> getCartBlockPropertiesArgumentType(String blockSearchRadiusName, String cartSearchRadiusName);

  // Return a properties argument type for cart sound commands
  public ArgumentType<PropertyList> getCartSoundPropertiesArgumentType(String cartSearchRadiusName, String soundVolumeName, String soundPitchName);


  // Return the property with the specified name from the properties as a block search radius type
  public int getBlockSearchRadiusProperty(PropertyList properties, String name);

  // Return the property with the specified name from the properties as a cart search distance type
  public int getCartSearchDistanceProperty(PropertyList properties, String name);

  // Return the property with the specified name from the properties as a sound volume type
  public float getSoundVolumeProperty(PropertyList properties, String name);

  // Return the property with the specified name from the properties as a sound pitch type
  public float getSoundPitchProperty(PropertyList properties, String name);
}
