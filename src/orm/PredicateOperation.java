package orm;

public enum PredicateOperation {
    EQUAL("="), NOT_EQUAL("!="), ARRAY_CONTAINS(" IN "), NOT_ARRAY_CONTAINS(" NOT IN "),
    LIKE(" LIKE "), NOT_LIKE(" NOT LIKE "), IS_NULL(" IS NULL "), IS_NOT_NULL(" IS NOT NULL "),
    AND(" AND "), OR(" OR "), NOT(" NOT ");

    private String _value;
    PredicateOperation(String val) {
        this._value = val;
    }

    public String value() {
        return new String(this._value);
    }

}
