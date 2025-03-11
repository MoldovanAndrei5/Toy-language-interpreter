package model.types;

import model.values.Value;
import model.values.StringValue;

public class StringType implements Type {
    public StringType() {}

    @Override
    public boolean equals(Type another) {
        return another instanceof StringType;
    }

    @Override
    public String toString() {
        return "string";
    }

    @Override
    public Value defaultValue() {
        return new StringValue("");
    }
}
