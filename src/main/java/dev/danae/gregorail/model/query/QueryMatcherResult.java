package dev.danae.gregorail.model.query;

import dev.danae.gregorail.model.minecart.Minecart;


public class QueryMatcherResult<T>
{
  // The value of the result
  private final T value;
    
  // The detected cart of the result
  private final Minecart cart;
    
    
  // Constructor
  public QueryMatcherResult(T value, Minecart cart)
  {
    this.value = value;
    this.cart = cart;
  }
    
    
  // Return the value of the result
  public final T getValue()
  {
    return this.value;
  }
    
  // Return the detected cart of the result
  public final Minecart getCart()
  {
    return this.cart;
  }
}
