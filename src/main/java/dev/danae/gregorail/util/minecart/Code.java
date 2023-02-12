package dev.danae.gregorail.util.minecart;

import java.util.Objects;


public class Code
{
  // The identifier of the code
  private final String id;
  
  
  // Constructor
  public Code(String id)
  {    
    this.id = id;
  }
  
  
  // Return the identifier of the code
  public String getId()
  {
    return this.id;
  }
  
  
  // Return if the code is equal to another object
  @Override
  public boolean equals(Object obj)
  {
    return obj instanceof Code other && Objects.equals(this.id, other.id);
  }
  
  // Return the hash code of the code
  @Override
  public int hashCode()
  {
    int hash = 7;
    hash = 59 * hash + Objects.hashCode(this.id);
    return hash;
  }
  
  // Return the string representation of the code
  @Override
  public String toString()
  {
    return this.id;
  }
}
