package dev.danae.gregorail.plugin;

import java.util.List;
import java.util.stream.Stream;
import dev.danae.common.commands.arguments.ArgumentType;
import dev.danae.common.commands.arguments.LocationArgumentTypeBuilder;
import dev.danae.common.commands.arguments.LocationFormat;
import dev.danae.common.commands.arguments.MaterialFilter;
import dev.danae.common.commands.arguments.PropertyList;
import dev.danae.common.commands.arguments.StringArgumentType;
import dev.danae.gregorail.model.Code;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.model.ManagerComponent;
import dev.danae.gregorail.model.Query;
import dev.danae.gregorail.model.arguments.ArgumentTypeManager;
import dev.danae.gregorail.model.arguments.CodeArgumentType;
import dev.danae.gregorail.model.arguments.CodeListArgumentType;
import dev.danae.gregorail.model.arguments.QueryArgumentType;
import dev.danae.gregorail.model.arguments.QueryMatcherArgumentType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.data.Rail;


public class GregoRailArgumentTypeManager extends ManagerComponent implements ArgumentTypeManager
{
  // Predefined names for properties
  public static final String BLOCK_SEARCH_RADIUS_PROPERTY_NAME = "radius";
  public static final String CART_SEARCH_DISTANCE_PROPERTY_NAME = "distance";
  public static final String SOUND_VOLUME_PROPERTY_NAME = "volume";
  public static final String SOUND_PITCH_PROPERTY_NAME = "pitch";


  // Argument types for arguments
  private final StringArgumentType<Query> queryType;
  private final StringArgumentType<Code> codeType;
  private final QueryMatcherArgumentType<Code> codeQueryMatcherType;
  private final StringArgumentType<List<Code>> codeListType;
  private final QueryMatcherArgumentType<List<Code>> codeListQueryMatcherType;
  private final StringArgumentType<Double> speedMultiplierType;
  private final QueryMatcherArgumentType<Double> speedMultiplierQueryMatcherType;
  private final StringArgumentType<Rail.Shape> blockShapeType;
  private final QueryMatcherArgumentType<Rail.Shape> blockShapeQueryMatcherType;
  private final StringArgumentType<Material> blockMaterialType;
  private final QueryMatcherArgumentType<Material> blockMaterialQueryMatcherType;
  private final StringArgumentType<NamespacedKey> soundType;
  private final QueryMatcherArgumentType<NamespacedKey> soundQueryMatcherType;

  // Argument types for properties
  private final StringArgumentType<Integer> blockSearchRadiusPropertyType;
  private final StringArgumentType<Integer> cartSearchDistancePropertyType;
  private final StringArgumentType<Float> soundVolumePropertyType;
  private final StringArgumentType<Float> soundPitchPropertyType;

  // Argument type builders
  private final LocationArgumentTypeBuilder locationTypeBuilder;

  
  // Constructor
  public GregoRailArgumentTypeManager(Manager manager)
  {
    super(manager);

    this.queryType = new QueryArgumentType(manager);
    this.codeType = new CodeArgumentType(manager);
    this.codeQueryMatcherType = new QueryMatcherArgumentType<>(manager, this.queryType, this.codeType);
    this.codeListType = new CodeListArgumentType(manager);
    this.codeListQueryMatcherType = new QueryMatcherArgumentType<>(manager, this.queryType, this.codeListType);
    this.speedMultiplierType = ArgumentType.getDoubleArgumentType(Stream.of(1.0));
    this.speedMultiplierQueryMatcherType = new QueryMatcherArgumentType<>(manager, this.queryType, this.speedMultiplierType);
    this.blockShapeType = ArgumentType.getEnumArgumentType(Rail.Shape.class);
    this.blockShapeQueryMatcherType = new QueryMatcherArgumentType<>(manager, this.queryType, this.blockShapeType);
    this.blockMaterialType = ArgumentType.getMaterialArgumentType(MaterialFilter.BLOCKS);
    this.blockMaterialQueryMatcherType = new QueryMatcherArgumentType<>(manager, this.queryType, this.blockMaterialType);
    this.soundType = ArgumentType.getNamespacedKeyArgumentType(Registry.SOUNDS.keyStream());
    this.soundQueryMatcherType = new QueryMatcherArgumentType<>(manager, this.queryType, this.soundType);

    this.blockSearchRadiusPropertyType = ArgumentType.getUnsignedIntArgumentType(Stream.of(manager.getBlockSearchRadius()));
    this.cartSearchDistancePropertyType = ArgumentType.getUnsignedIntArgumentType(Stream.of(manager.getCartSearchDistance()));
    this.soundVolumePropertyType = ArgumentType.getFloatArgumentType(Stream.of(1.0f));
    this.soundPitchPropertyType = ArgumentType.getFloatArgumentType(Stream.of(1.0f));

    this.locationTypeBuilder = ArgumentType.getLocationArgumentTypeBuilder()
      .withAllowedFormats(LocationFormat.NUMERIC, LocationFormat.BLOCK, LocationFormat.ALIAS)
      .withBlockSearchRadius(manager.getBlockSearchRadius());
  }


  // Return the query argument type
  @Override
  public StringArgumentType<Query> getQueryArgumentType()
  {
    return this.queryType;
  }

  // Return the code argument type
  @Override
  public StringArgumentType<Code> getCodeArgumentType()
  {
    return this.codeType;
  }

  // Return the code query matcher argument type
  @Override
  public QueryMatcherArgumentType<Code> getCodeQueryMatcherArgumentType()
  {
    return this.codeQueryMatcherType;
  }

  // Return the code list argument type
  @Override
  public StringArgumentType<List<Code>> getCodeListArgumentType()
  {
    return this.codeListType;
  }

  // Return the code list query matcher argument type
  @Override
  public QueryMatcherArgumentType<List<Code>> getCodeListQueryMatcherArgumentType()
  {
    return this.codeListQueryMatcherType;
  }

  // Return the speed multiplier argument type
  @Override
  public StringArgumentType<Double> getSpeedMultiplierArgumentType()
  {
    return this.speedMultiplierType;
  }

  // Return the speed multiplier query matcher argument type
  @Override
  public QueryMatcherArgumentType<Double> getSpeedMultiplierQueryMatcherArgumentType()
  {
    return this.speedMultiplierQueryMatcherType;
  }

  // Return the block shape argument type
  @Override
  public StringArgumentType<Rail.Shape> getBlockShapeArgumentType()
  {
    return this.blockShapeType;
  }

  // Return the block shape query matcher argument type
  @Override
  public QueryMatcherArgumentType<Rail.Shape> getBlockShapeQueryMatcherArgumentType()
  {
    return this.blockShapeQueryMatcherType;
  }

  // Return the block material argument type
  @Override
  public StringArgumentType<Material> getBlockMaterialArgumentType()
  {
    return this.blockMaterialType;
  }

  // Return the block material query matcher argument type
  @Override
  public QueryMatcherArgumentType<Material> getBlockMaterialQueryMatcherArgumentType()
  {
    return this.blockMaterialQueryMatcherType;
  }

  // Return the sound argument type
  @Override
  public StringArgumentType<NamespacedKey> getSoundArgumentType()
  {
    return this.soundType;
  }

  // Return the sound query matcher argument type
  @Override
  public QueryMatcherArgumentType<NamespacedKey> getSoundQueryMatcherArgumentType()
  {
    return this.soundQueryMatcherType;
  }
  
  
  // Return a location argument type for the specified origin and block search radius
  @Override
  public ArgumentType<Location> getLocationArgumentType(Location origin, int blockSearchRadius)
  {
    return this.locationTypeBuilder
      .withBlockSearchRadius(blockSearchRadius)
      .build(origin);
  }

  // Return a location argument type for the specified origin
  @Override
  public ArgumentType<Location> getLocationArgumentType(Location origin)
  {
    return this.locationTypeBuilder
      .build(origin);
  }

  // Return a properties argument type for block commands
  @Override
  public ArgumentType<PropertyList> getBlockPropertiesArgumentType(String blockSearchRadiusName)
  {
    return ArgumentType.getPropertyListArgumentTypeBuilder()
      .withPropertyType(blockSearchRadiusName, this.blockSearchRadiusPropertyType)
      .build();
  }

  // Return a properties argument type for cart block commands
  @Override
  public ArgumentType<PropertyList> getCartBlockPropertiesArgumentType(String blockSearchRadiusName, String cartSearchRadiusName)
  {
    return ArgumentType.getPropertyListArgumentTypeBuilder()
      .withPropertyType(blockSearchRadiusName, this.blockSearchRadiusPropertyType)
      .withPropertyType(cartSearchRadiusName, this.cartSearchDistancePropertyType)
      .build();
  }

  // Return a properties argument type for cart sound commands
  @Override
  public ArgumentType<PropertyList> getCartSoundPropertiesArgumentType(String cartSearchRadiusName, String soundVolumeName, String soundPitchName)
  {
    return ArgumentType.getPropertyListArgumentTypeBuilder()
      .withPropertyType(cartSearchRadiusName, this.cartSearchDistancePropertyType)
      .withPropertyType(soundVolumeName, this.soundVolumePropertyType)
      .withPropertyType(soundPitchName, this.soundPitchPropertyType)
      .build();
  }


  // Return the property with the specified name from the properties as a block search radius type
  @Override
  public int getBlockSearchRadiusProperty(PropertyList properties, String name)
  {
    return properties.get(name, this.blockSearchRadiusPropertyType, this.getManager().getBlockSearchRadius());
  }

  // Return the property with the specified name from the properties as a cart search distance type
  @Override
  public int getCartSearchDistanceProperty(PropertyList properties, String name)
  {
    return properties.get(name, this.cartSearchDistancePropertyType, this.getManager().getCartSearchDistance());
  }

  // Return the property with the specified name from the properties as a sound volume type
  @Override
  public float getSoundVolumeProperty(PropertyList properties, String name)
  {
    return properties.get(name, this.soundVolumePropertyType, 1.0f);
  }

  // Return the property with the specified name from the properties as a sound pitch type
  @Override
  public float getSoundPitchProperty(PropertyList properties, String name)
  {
    return properties.get(name, this.soundPitchPropertyType, 1.0f);
  }
}
