package orm;

public class Predicate {
    public String[] columns;
    public Object[] values;
    public PredicateOperation[] operations;
    public PredicateOperation interPredicateOperation;
}
