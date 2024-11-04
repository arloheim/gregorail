package dev.danae.gregorail.model.arguments;

import java.util.regex.Pattern;
import java.util.regex.MatchResult;
import java.util.stream.Collector;
import java.util.stream.Stream;
import dev.danae.common.commands.Suggestion;
import dev.danae.common.commands.arguments.ArgumentException;
import dev.danae.common.commands.arguments.ArgumentTypeMismatchException;
import dev.danae.common.commands.arguments.PatternListArgumentType;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.model.ManagerComponent;
import dev.danae.gregorail.model.Query;


public final class QueryArgumentType extends ManagerComponent implements PatternListArgumentType<Query, Query>
{
  // The pattern for parsing a code
  private static final Pattern QUERY_PATTERN = Pattern.compile("((!)?(\\*)?([a-zA-Z0-9_]+)(\\*)?)|(\\*)");


  // Constructor
  public QueryArgumentType(Manager manager)
  {
    super(manager);
  }


  // Return the type for the argument type
  @Override
  public Class<Query> getType()
  {
    return Query.class;
  }

  // Return the type name for the argument type
  @Override
  public String getTypeName()
  {
    return "query";
  }

  // Return the pattern for the argument type
  @Override
  public Pattern getPattern()
  {
    return QUERY_PATTERN;
  }

  // Return the delimiter for the argument type
  @Override
  public String getDelimiter()
  {
    return "|";
  }

  // Parse a query from the specified match result
  @Override
  public Query parseFromMatchResult(MatchResult m) throws ArgumentException 
  {
    Query query;

    // Check for a code query
    if (m.group(1) != null)
    {
      // Parse the pattern
      if (m.group(3) != null && m.group(5) != null)
        query = Query.codeContains(m.group(4));
      else if (m.group(3) != null)
        query = Query.codeStartsWith(m.group(4));
      else if (m.group(5) != null)
        query = Query.codeEndsWith(m.group(4));
      else
        query = Query.codeEquals(m.group(4));
    }
    
    // Check for an all query
    else if (m.group(6) != null)
      query = Query.alwaysTrue();
    
    // Invalid query format
    else
      throw new ArgumentTypeMismatchException(this, m.group());

    // Check if the query should be negated
    if (m.group(1) != null)
      query = Query.negate(query);

    // Return the query
    return query;
  }

  // Return the collector for the argument type
  @Override
  public Collector<Query, ?, Query> getCollector()
  {
    return Query.anyMatch();
  }

  // Return suggestions for the specified string list item
  @Override
  public Stream<String> suggestFromStringList(String input)
  {
    return Suggestion.find(input, this.getManager().getDefinedCodeTags().keySet().stream()
      .map(code -> code.getId()));
  }
}
