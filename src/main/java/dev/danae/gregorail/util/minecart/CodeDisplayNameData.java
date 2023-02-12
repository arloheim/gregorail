package dev.danae.gregorail.util.minecart;

import dev.danae.gregorail.RailPlugin;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Stream;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;


public class CodeDisplayNameData
{
  // The file that backs the data
  private final File dataFile;
  
  // Map that maps codes to strings specifying a display name for the code
  private final Map<Code, String> displayNames = new HashMap();
  
  
  // Constructor
  public CodeDisplayNameData(File dataFile)
  {
    this.dataFile = dataFile;
    this.load();
  }
  
  
  
  // Get all codes for which a display name is defined as a stream
  public Stream<Code> codesAsStream()
  {
    return this.displayNames.keySet().stream()
      .sorted(Comparator.comparing(code -> code.getId(), String.CASE_INSENSITIVE_ORDER));
  }
  
  // Get all codes for which a display name is defined
  public Collection<Code> codes()
  {
    return this.codesAsStream().toList();
  }
  
  // Get all entries for codes for which a display name is defined as a stream
  public Stream<Map.Entry<Code, String>> codeEntriesAsStream()
  {
    return this.displayNames.entrySet().stream()
      .sorted(Comparator.comparing(e -> e.getKey().getId(), String.CASE_INSENSITIVE_ORDER));
  }
  
  // Get all entries for codes for which a display name is defined
  public Collection<Map.Entry<Code, String>> codeEntries()
  {
    return this.codeEntriesAsStream().toList();
  }
  
  
  // Get the display name of a code
  public String getDisplayName(Code code)
  {
    return this.displayNames.getOrDefault(code, null);
  }
  
  // Set the display name of a code
  public void setDisplayName(Code code, String displayName)
  {
    this.displayNames.put(code, displayName);
    this.save();
  }
  
  // Remove the display name of a code
  public void removeDisplayName(Code code)
  {
    this.displayNames.remove(code);
    this.save();
  }
  
  
  // Load the display names from the configuration file
  public void load()
  {
    try
    {
      if (!this.dataFile.exists())
        this.dataFile.createNewFile();
      
      var config = new YamlConfiguration();
      config.load(this.dataFile);
      
      this.displayNames.clear();
      for (String key : config.getKeys(false))
        this.displayNames.put(new Code(key), config.getString(key));
    }
    catch (IOException | InvalidConfigurationException ex)
    {
      RailPlugin.getInstance().getLogger().log(Level.WARNING, "Could not load the code display name data", ex);
    }
  }
  
  // Save the display names to the configuration file
  public void save()
  {
    try
    {
      var config = new YamlConfiguration();
      for (var e : this.displayNames.entrySet())
        config.set(e.getKey().getId(), e.getValue());
      
      config.save(this.dataFile);
    }
    catch (IOException ex)
    {
      RailPlugin.getInstance().getLogger().log(Level.WARNING, "Could not save the code display name data", ex);
    }
  }
}
