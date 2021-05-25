package model;

public enum Gender {
    M("M"), F("F"), Other("Other");

    private final String _value;

    Gender(String value) {
        this._value = value;
    }

    public String value() {
        return new String(_value);
    }
}
