package dev.danae.gregorail.plugin;

import com.google.gson.JsonObject;
import dev.danae.gregorail.model.events.BlockMaterialChangedEvent;
import dev.danae.gregorail.model.events.BlockShapeChangedEvent;
import dev.danae.gregorail.model.events.MinecartCodeChangedEvent;
import dev.danae.gregorail.model.events.MinecartDestroyedEvent;
import dev.danae.gregorail.model.events.MinecartSpeedMultiplierChangedEvent;
import dev.danae.gregorail.model.events.MinecartEnteredEvent;
import dev.danae.gregorail.model.events.MinecartExitedEvent;
import dev.danae.gregorail.model.events.MinecartMovedEvent;
import dev.danae.gregorail.model.events.SoundPlayedEvent;
import dev.danae.gregorail.plugin.webhooks.WebhookException;
import dev.danae.gregorail.plugin.webhooks.WebhookExecutor;
import dev.danae.gregorail.plugin.webhooks.WebhookPayloadFactory;
import dev.danae.gregorail.plugin.webhooks.WebhookType;
import java.util.logging.Level;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class GregoRailWebhookExecutor extends GregoRailPluginComponent implements WebhookExecutor, Listener
{
  // The options for the webhook executor
  private final GregoRailPluginOptions options;
  
  // The payload factory of the webhook executor
  private final WebhookPayloadFactory payloadFactory;
  
  
  // Constructor
  public GregoRailWebhookExecutor(GregoRailPlugin plugin, GregoRailPluginOptions options)
  {
    super(plugin);
    
    this.options = options;
    this.payloadFactory = new WebhookPayloadFactory(this.getManager());
  }
  
  
  // Execute a webhook with the specified type and payload
  @Override
  public void executeWebhook(WebhookType type, JsonObject payload)
  {
    if (!this.options.isWebhooksEnabled())
      return;
    
    // Create a new thread to execute webhooks
    new Thread(() ->
    {
      for (var webhook : this.options.getWebhooks())
      {
        try
        {
          webhook.execute(type, payload);
        }
        catch (WebhookException ex)
        {
          this.getPlugin().getLogger().log(Level.WARNING, "Could not execute webhook {0}", webhook.getName());
          this.getPlugin().getLogger().log(Level.WARNING, ex.getMessage());
        }
      }
    }).start();
  }
  
  
  // Event listener for when the code of a cart has been changed
  @EventHandler
  public void onMinecartCodeChanged(MinecartCodeChangedEvent e)
  {
    // Execute the appropriate webhook
    this.executeWebhook(WebhookType.CART_CODE_CHANGED, this.payloadFactory.createCartCodePayload(e.getCart(), e.getOriginalCode(), e.getCode()));
  }
  
  // Event listener for when the speed multiplier of a cart has been changed
  @EventHandler
  public void onMinecartSpeedMultiplierChanged(MinecartSpeedMultiplierChangedEvent e)
  {
    // Execute the appropriate webhook
    this.executeWebhook(WebhookType.CART_SPEED_MULTIPLIER_CHANGED, this.payloadFactory.createCartSpeedMultiplierPayload(e.getCart(), e.getOriginalSpeedMultiplier(), e.getSpeedMultiplier()));
  }
  
  // Event listener for when the shape of a block has been changed
  @EventHandler
  public void onBlockShapeChanged(BlockShapeChangedEvent e)
  {
    // Execute the appropriate webhook
    this.executeWebhook(WebhookType.BLOCK_SHAPE_CHANGED, this.payloadFactory.createBlockShapePayload(e.getBlock(), e.getOriginalShape(), e.getShape(), e.getCause()));
  }
  
  // Event listener for when the material of a block has been changed
  @EventHandler
  public void onBlockMaterialChanged(BlockMaterialChangedEvent e)
  {
    // Execute the appropriate webhook
    this.executeWebhook(WebhookType.BLOCK_MATERIAL_CHANGED, this.payloadFactory.createBlockMaterialPayload(e.getBlock(), e.getOriginalMaterial(), e.getMaterial(), e.getCause()));
  }
  
  // Event listener for when a sound is played
  @EventHandler
  public void onSoundPlayed(SoundPlayedEvent e)
  {
    // Execute the appropriate webhook
    this.executeWebhook(WebhookType.SOUND_PLAYED, this.payloadFactory.createSoundPayload(e.getLocation(), e.getSoundKey(), e.getCause()));
  }
  
  // Event listener for when a player has entered a minecart
  @EventHandler
  public void onMinecartEntered(MinecartEnteredEvent e)
  {
    // Execute the appropriate webhook
    this.executeWebhook(WebhookType.CART_ENTERED, this.payloadFactory.createCartPayload(e.getCart(), e.getPlayer()));
  }
  
  // Event listener for when a player exited a minecart
  @EventHandler
  public void onMinecartExited(MinecartExitedEvent e)
  {
    // Execute the appropriate webhook
    this.executeWebhook(WebhookType.CART_EXITED, this.payloadFactory.createCartPayload(e.getCart(), e.getPlayer()));
  }
  
  // Event listener for when a minecart moved
  @EventHandler
  public void onMinecartMoved(MinecartMovedEvent e)
  {
    // Execute the appropriate webhook
    this.executeWebhook(WebhookType.CART_MOVED, this.payloadFactory.createCartLocationPayload(e.getCart(), e.getFrom(), e.getTo()));
  }
  
  // Event listener for when a minecart is destroyed
  @EventHandler
  public void onMinecartDestroyed(MinecartDestroyedEvent e)
  {
    // Execute the appropriate webhook
    this.executeWebhook(WebhookType.CART_DESTROYED, this.payloadFactory.createCartPayload(e.getCart()));
  }
}
