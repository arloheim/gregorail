package dev.danae.gregorail.model.arguments;

import java.util.stream.Stream;
import com.google.common.reflect.TypeToken;
import dev.danae.common.commands.CommandContext;
import dev.danae.common.commands.Suggestion;
import dev.danae.common.commands.arguments.ArgumentException;
import dev.danae.common.commands.arguments.ArgumentType;
import dev.danae.common.commands.arguments.Scanner;
import dev.danae.common.commands.arguments.StringArgumentType;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.model.ManagerComponent;
import dev.danae.gregorail.model.Query;
import dev.danae.gregorail.model.QueryMatcher;


public final class QueryMatcherArgumentType<T> extends ManagerComponent implements ArgumentType<QueryMatcher<T>>
{
  // The result type for the argument type
  private final StringArgumentType<T> resultArgumentType;

  // The argument types for the argument type
  private final StringArgumentType<Query> queryArgumentType;

  
  // Constructor
  public QueryMatcherArgumentType(Manager manager, StringArgumentType<T> resultArgumentType)
  {
    super(manager);

    this.resultArgumentType = resultArgumentType;
    this.queryArgumentType = manager.getQueryArgumentType();
  }


  // Return the result argument type for the argument type
  public StringArgumentType<T> getResultArgumentType()
  {
    return this.resultArgumentType;
  }
  
  // Return the query argument type for the argument type
  public StringArgumentType<Query> getQueryArgumentType()
  {
    return this.queryArgumentType;
  }


  // Return the type for the argument type
  @Override
  public Class<? super QueryMatcher<T>> getType()
  {
    return new TypeToken<QueryMatcher<T>>() {}.getRawType();
  }

  // Return the type name for the argument type
  @Override
  public String getTypeName()
  {
    return String.format("query matcher for %s", resultArgumentType.getType());
  }
  
  // Parse the query matcher from the specified string
  @Override
  public QueryMatcher<T> parse(Scanner scanner) throws ArgumentException
  {
    var matcher = new QueryMatcher<T>();
      
    while (!scanner.isAtEnd())
    {
      var query = this.queryArgumentType.parse(scanner);
      var result = resultArgumentType.parse(scanner);
        
      matcher.addBranch(query, result);
      
      if (!scanner.match(s -> s.equals("||"), "separator"))
        break;
    }
      
    return matcher;
  }

  // Return suggestions for the specified command context
  @Override
  public Stream<String> suggest(CommandContext context, int argumentIndex)
  {
    return Suggestion.find(context.getArgument(argumentIndex + 1), this.getManager().getDefinedCodeTags().keySet().stream()
      .map(code -> code.getId()));
  }
}
