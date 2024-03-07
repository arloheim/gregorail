package dev.danae.gregorail.plugin.migrations.v1_1_0;

import com.google.common.io.Files;
import dev.danae.gregorail.model.CodeTag;
import dev.danae.gregorail.plugin.GregoRailPlugin;
import dev.danae.gregorail.plugin.migrations.Migration;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;


public class CodeDisplayNamesMigration extends Migration
{
  // The old file where the code display names are stored
  private final File codeDisplayNamesFile;

  // The backup file to move the old file to
  private final File codeDisplayNamesBackupFile;

  // The new file where the code tags are stored
  private final File codeTagsFile;


  // Constructor
  public CodeDisplayNamesMigration(GregoRailPlugin plugin) 
  {
    super(plugin);

    this.codeDisplayNamesFile = new File(plugin.getDataFolder(), "code_display_names.yml");
    this.codeDisplayNamesBackupFile = new File(plugin.getDataFolder(), "code_display_names.yml.bak");
    this.codeTagsFile = new File(plugin.getDataFolder(), "code_tags.yml");
  }


  // Return the version the migration applies to
  @Override
  protected String getVersion()
  {
    return "1.1.0";
  }

  // Return if the prerequisites for the migrations are met
  @Override
  protected boolean canMigrate()
  {
    // Return if the code display names file exists and the code tags file does not exist
    return this.codeDisplayNamesFile.exists() && !codeTagsFile.exists();
  }

  // Actually execute the migration
  @Override
  protected void doMigrate()
  {
    try
    {
      this.getPlugin().getLogger().log(Level.INFO, "[v{0}: {1}] Migrating code display names to code tags...", new Object[] { this.getVersion(), CodeDisplayNamesMigration.class.getSimpleName()});

      // Load the code display names configuration
      var codeDisplayNamesConfig = new YamlConfiguration();
      codeDisplayNamesConfig.load(codeDisplayNamesFile);

      // Save the code tags configuration
      var codeTagsConfig = new YamlConfiguration();
      for (var key : codeDisplayNamesConfig.getKeys(false))
        codeTagsConfig.set(key, new CodeTag(codeDisplayNamesConfig.getString(key), null));
      codeTagsConfig.save(this.codeTagsFile);

      // Move the old file to the backup file
      Files.move(this.codeDisplayNamesFile, this.codeDisplayNamesBackupFile);
    }
    catch (IOException | InvalidConfigurationException ex)
    {
      this.getPlugin().getLogger().log(Level.SEVERE, "[v{0}: {1}] {2}", new Object[] { this.getVersion(), CodeDisplayNamesMigration.class.getSimpleName(), ex.getMessage()});
    }
  }
}
