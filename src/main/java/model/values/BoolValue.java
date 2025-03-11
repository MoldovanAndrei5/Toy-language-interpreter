package model.values;

import model.types.*;

public class BoolValue implements Value {
    boolean value;

    public BoolValue(boolean v) {
        value = v;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type getType() {
        return new BoolType();
    }

    @Override
    public String toString() {
        return value ? "true" : "false";
    }

    @Override
    public boolean equals(Value another) {
        return value == ((BoolValue)another).getValue();
    }

    @Override
    public Value deepCopy() {
        return new BoolValue(value);
    }
}
