package dev.danae.gregorail.model.events;

import java.util.Arrays;
import dev.danae.gregorail.model.Minecart;
import net.kyori.adventure.audience.Audience;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class SoundPlayedEvent extends Event
{
  // List of handlers of the event
  private static final HandlerList HANDLERS = new HandlerList();
  
  
  // The audience of the sound
  private final Audience audience;
  
  // The key of the sound
  private final NamespacedKey soundKey;
  
  // The minecart that caused the sound to play
  private final Minecart cause;
  
  
  // Constructor
  public SoundPlayedEvent(Audience audience, NamespacedKey soundKey, Minecart cause)
  {    
    this.audience = audience;
    this.soundKey = soundKey;
    this.cause = cause;
  }
  
  
  // Return the audience of the sound
  public Audience getAudience()
  {
    return this.audience;
  }
  
  // Return the key of the sound
  public NamespacedKey getSoundKey()
  {
    return this.soundKey;
  }
  
  // Return the enum value of the sound
  public Sound getSound()
  {
    return Arrays.stream(Sound.values())
      .filter(sound -> sound.getKey().equals(this.soundKey))
      .findFirst()
      .orElse(null);
  }
  
  // Return the minecart that caused the sound to play
  public Minecart getCause()
  {
    return this.cause;
  }
  
  
  // Return the list of handlers of the event
  @Override
  public HandlerList getHandlers()
  {
    return HANDLERS;
  }
  
  // Statically return the list of handlers of the event
  public static HandlerList getHandlerList() 
  {
    return HANDLERS;
  }
}
