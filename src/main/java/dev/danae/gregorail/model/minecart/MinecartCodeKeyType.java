package dev.danae.gregorail.model.minecart;

import dev.danae.gregorail.plugin.ConfigurationMapKeyType;


public class MinecartCodeKeyType implements ConfigurationMapKeyType<MinecartCode>
{
  // Convert the specified key to its string representation
  public String toString(MinecartCode key)
  {
    return key.getId();
  }

  // Convert the specified string to its key representation
  public MinecartCode toKey(String string)
  {
    return MinecartCode.of(string);
  }
}
