package dev.danae.gregorail.model;

import java.util.Objects;


public class Code implements Comparable<Code>
{  
  // The identifier of the code
  private final String id;
  
  
  // Constructor
  private Code(String id)
  {    
    this.id = id;
  }
  
  
  // Return the identifier of the code
  public String getId()
  {
    return this.id;
  }
  
  // Return if the code is empty
  public boolean isEmpty()
  {
    return this.id == null;
  }

  // Compare the code the another code
  @Override
  public int compareTo(Code other) 
  {
    return this.id.compareToIgnoreCase(other.id);
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
    if (!this.isEmpty())
      return this.id;
    else
      return "∅";
  }
  
  
  // Return a code from an identifier
  public static Code of(String id)
  {
    return new Code(id);
  }
  
  // Return an empty code
  public static Code empty()
  {
    return new Code(null);
  }
}
