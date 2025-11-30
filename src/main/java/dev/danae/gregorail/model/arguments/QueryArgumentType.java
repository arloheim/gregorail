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
  private static final Pattern QUERY_PATTERN = Pattern.compile("(?<neg>!)?(?:(?<q>(?<sw>\\*)?(?<code>[a-zA-Z0-9_]+)(?<ew>\\*)?)|(?<any>#)|(?<true>\\*))");


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

    // Parse the pattern
    if (m.group("q") != null && m.group("sw") != null && m.group("ew") != null)
      query = Query.codeContains(m.group("code"));
    else if (m.group("q") != null && m.group("ew") != null)
      query = Query.codeStartsWith(m.group("code"));
    else if (m.group("q") != null && m.group("sw") != null)
      query = Query.codeEndsWith(m.group("code"));
    else if (m.group("q") != null)
      query = Query.codeEquals(m.group("code"));
    else if (m.group("any") != null)
      query = Query.codeExists();
    else if (m.group("true") != null)
      query = Query.alwaysTrue();
    else
      throw new ArgumentTypeMismatchException(this, m.group());

    // Check if the query should be negated
    if (m.group("neg") != null)
      query = Query.negate(query);

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
