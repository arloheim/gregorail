package dev.danae.gregorail.model;

import java.util.LinkedList;
import java.util.List;


public class QueryMatcher<T>
{
  // Class that defines a branch in the matcher
  public class Branch
  {
    // The query of the branch
    private final Query query;
    
    // The value of the branch
    private final T value;

    
    // Constructor
    public Branch(Query query, T value)
    {
      this.query = query;
      this.value = value;
    }
    
    
    // Return the query of the branch
    public Query getQuery()
    {
      return this.query;
    }
    
    // Return the value of the branch
    public T getValue()
    {
      return this.value;
    }
  }

  
  // The list of branches for the matcher
  private final List<Branch> branches = new LinkedList<>();
  
  
  // Add a branch to the matcher
  public void addBranch(Branch branch)
  {
    this.branches.add(branch);
  }
  
  // Add a branch to the matcher with the specified query and result
  public void addBranch(Query query, T result)
  {
    this.branches.add(new Branch(query, result));
  }
  
  // Remove a branch from the matcher
  public void removeBranch(Branch branch)
  {
    this.branches.remove(branch);
  }
  
  // Evaluate the queries of the matcher
  public QueryMatcherResult<T> match(Minecart cart)
  {
    // Iterate over the branches
    for (var branch : this.branches)
    {
      // If the query of the branch matcher, then return its result
      if (branch.getQuery().match(cart))
        return new QueryMatcherResult<>(branch.getValue(), cart);
    }
    
    // No query matched, so return a null result
    return new QueryMatcherResult<>(null, cart);
  }
}
