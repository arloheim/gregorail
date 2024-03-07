package dev.danae.gregorail.model.persistence;

import dev.danae.gregorail.model.Code;
import dev.danae.gregorail.plugin.ConfigurationMapKeyType;


public class CodeKeyType implements ConfigurationMapKeyType<Code>
{
  // Convert the specified key to its string representation
  public String toString(Code key)
  {
    return key.getId();
  }

  // Convert the specified string to its key representation
  public Code toKey(String string)
  {
    return Code.of(string);
  }
}
