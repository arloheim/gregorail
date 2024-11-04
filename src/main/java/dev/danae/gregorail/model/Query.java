package dev.danae.gregorail.model;

import java.util.Collection;
import java.util.stream.Collector;


@FunctionalInterface
public interface Query
{
  // Evaluate the query
  public boolean match(Minecart cart);
  

  // Return a query that always returns false
  public static Query alwayFalse()
  {
    return cart -> false;
  }
  
  // Return a query that always returns true
  public static Query alwaysTrue()
  {
    return cart -> true;
  }

  // Return a query that negates the specified query
  public static Query negate(Query query)
  {
    return cart -> !query.match(cart);
  }
  
  // Return a query that checks if any of the specified queries evaluate to true
  public static Query anyMatch(Collection<Query> queries)
  {
    return cart -> queries.stream().anyMatch(q -> q.match(cart));
  }

  // Return a collector that checks if any of the specified queries evaluate to true
  public static Collector<Query, ?, Query> anyMatch()
  {
    return new QueryCollector(queries -> anyMatch(queries));
  }
  
  // Return a query that checks if all of the specified queries evaluate to true
  public static Query allMatch(Collection<Query> queries)
  {
    return cart -> queries.stream().allMatch(q -> q.match(cart));
  }

  // Return a collector that checks if all of the specified queries evaluate to true
  public static Collector<Query, ?, Query> allMatch()
  {
    return new QueryCollector(queries -> allMatch(queries));
  }


  // Return a query that checks if the code of a minecart equals a string
  public static Query codeEquals(String string)
  {
    return cart -> cart != null && !cart.getCode().isEmpty() && cart.getCode().getId().equals(string);
  }
  
  // Return a query that checks if the code of a minecart starts with a string
  public static Query codeStartsWith(String string)
  {
    return cart -> cart != null && !cart.getCode().isEmpty() && cart.getCode().getId().startsWith(string);
  }
  
  // Return a query that checks if the code of a minecart ends with a string
  public static Query codeEndsWith(String string)
  {
    return cart -> cart != null && !cart.getCode().isEmpty() && cart.getCode().getId().endsWith(string);
  }
  
  // Return a query that checks if the code of a minecart contains a string
  public static Query codeContains(String string)
  {
    return cart -> cart != null && !cart.getCode().isEmpty() && cart.getCode().getId().contains(string);
  }
}
