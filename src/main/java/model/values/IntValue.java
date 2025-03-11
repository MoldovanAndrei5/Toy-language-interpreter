package model.values;

import model.types.*;

public class IntValue implements Value {
    int value;

    public IntValue(int v) {
        value = v;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public Type getType() {
        return new IntType();
    }

    @Override
    public boolean equals(Value another) {
        return another instanceof IntValue && ((IntValue) another).value == value;
    }

    @Override
    public Value deepCopy() {
        return new IntValue(value);
    }
}
