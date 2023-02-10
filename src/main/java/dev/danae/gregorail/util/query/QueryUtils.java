package dev.danae.gregorail.util.query;

import java.util.ArrayList;
import java.util.regex.Pattern;


public class QueryUtils
{
  // Pattern for parsing queries
  private static final Pattern pattern = Pattern.compile("^(?<suffix>\\*)?(?<name>[a-z0-9_]+)(?<prefix>\\*)?$", Pattern.CASE_INSENSITIVE);
  
  
  // Parse a query from a string
  public static Query parseQuery(String string) throws InvalidQueryException
  {
    var queryList = new ArrayList<Query>();
    
    // Split the string into components
    var stringComponents = string.split(",");
    for (var stringComponent : stringComponents)
    {
      // Match the component against the pattern
      var m = pattern.matcher(stringComponent);
      if (!m.matches())
        throw new InvalidQueryException(String.format("String \"%s\" is not a valid query", stringComponent));
      
      // Parse the pattern
      if (m.group("prefix") != null && m.group("suffix") != null)
        queryList.add(input -> input != null && input.contains(m.group("name")));
      else if (m.group("prefix") != null)
        queryList.add(input -> input != null && input.startsWith(m.group("name")));
      else if (m.group("suffix") != null)
        queryList.add(input -> input != null && input.endsWith(m.group("name")));
      else
        queryList.add(input -> input != null && input.equals(m.group("name")));
    }
    
    // Return the query
    if (queryList.isEmpty())
      throw new InvalidQueryException(String.format("String \"%s\" is not a valid query", string));
    else if(queryList.size() == 1)
      return queryList.get(0);
    else
      return input -> queryList.stream().anyMatch(q -> q.matches(input));
  }
}
