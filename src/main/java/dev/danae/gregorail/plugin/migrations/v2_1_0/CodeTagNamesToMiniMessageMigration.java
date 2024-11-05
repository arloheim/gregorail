package dev.danae.gregorail.plugin.migrations.v2_1_0;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import com.google.common.io.Files;
import dev.danae.gregorail.model.CodeTag;
import dev.danae.gregorail.plugin.GregoRailPlugin;
import dev.danae.gregorail.plugin.migrations.Migration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;


public class CodeTagNamesToMiniMessageMigration extends Migration
{
  // The new file where the code tags are stored
  private final File codeTagsFile;

  // The backup file to move the old file to
  private final File codeTagsBackupFile;


  // Constructor
  public CodeTagNamesToMiniMessageMigration(GregoRailPlugin plugin) 
  {
    super(plugin);

    this.codeTagsFile = new File(plugin.getDataFolder(), "code_tags.yml");
    this.codeTagsBackupFile = new File(plugin.getDataFolder(), "code_tags_v2_0_0.yml.bak");
  }


  // Return the version the migration applies to
  @Override
  protected String getVersion()
  {
    return "2.1.0";
  }

  // Return if the prerequisites for the migrations are met
  @Override
  protected boolean canMigrate()
  {
    // Return if the code tags backup file does not exist
    return !this.codeTagsBackupFile.exists();
  }

  // Actually execute the migration
  @Override
  protected void doMigrate()
  {
    try
    {
      this.getPlugin().getLogger().log(Level.INFO, "[v{0}: {1}] Migrating code code tags to the MiniMessage format...", new Object[] { this.getVersion(), CodeTagNamesToMiniMessageMigration.class.getSimpleName()});

      // Create the component (de)serializers
      var legacySerializer = LegacyComponentSerializer.legacy('§');
      var miniMessageSerializer = MiniMessage.miniMessage();

      // Copy the file to the backup file
      Files.copy(this.codeTagsFile, this.codeTagsBackupFile);

      // Load the code tags configuration and migrate the name to the MiniMessage format
      var codeTagsConfig = new YamlConfiguration();
      codeTagsConfig.load(this.codeTagsFile);
      for (var key : codeTagsConfig.getKeys(false))
      {
        var codeTag = codeTagsConfig.getObject(key, CodeTag.class);
        var nameComponent = legacySerializer.deserialize(codeTag.getName());
        var name = miniMessageSerializer.serialize(nameComponent);
        codeTagsConfig.set(key, new CodeTag(name, codeTag.getUrl()));
      }
      codeTagsConfig.save(this.codeTagsFile);
    }
    catch (IOException | InvalidConfigurationException ex)
    {
      this.getPlugin().getLogger().log(Level.SEVERE, "[v{0}: {1}] {2}", new Object[] { this.getVersion(), CodeTagNamesToMiniMessageMigration.class.getSimpleName(), ex.getMessage()});
    }
  }
}
