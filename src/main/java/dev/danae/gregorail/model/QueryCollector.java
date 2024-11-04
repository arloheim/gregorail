package dev.danae.gregorail.model;

import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;


final class QueryCollector implements Collector<Query, List<Query>, Query>
{
  // The merger function for the collector
  private final Function<Collection<Query>, Query> merger;


  // Constructor
  public QueryCollector(Function<Collection<Query>, Query> merger)
  {
    this.merger = merger;
  }


  // Return the supplier of the collector
  @Override
  public Supplier<List<Query>> supplier()
  {
    return () -> new LinkedList<Query>();
  }

  // Return the accumulator of the collector
  @Override
  public BiConsumer<List<Query>, Query> accumulator()
  {
    return (list, query) -> list.add(query);
  }

  // Return the combiner of the collector
  @Override
  public BinaryOperator<List<Query>> combiner()
  {
    return (list, otherList) -> { 
      list.addAll(otherList); 
      return list; 
    };
  }

  // Return the finisher of the collector
  @Override
  public Function<List<Query>, Query> finisher()
  {
    return (list) -> this.merger.apply(list);
  }

  // Return the characteristics of the collector
  @Override
  public Set<Characteristics> characteristics()
  {
    return EnumSet.noneOf(Characteristics.class);
  }
}
