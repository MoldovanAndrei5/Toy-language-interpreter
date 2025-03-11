package model.types;

import model.values.BoolValue;
import model.values.Value;

public class BoolType implements Type {
    public BoolType() {}

    @Override
    public boolean equals(Type another) {
        return another instanceof BoolType;
    }

    @Override
    public String toString() {
        return "bool";
    }

    @Override
    public Value defaultValue() {
        return new BoolValue(false);
    }
}
