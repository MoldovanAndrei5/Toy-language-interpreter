package model.types;

import model.values.RefValue;
import model.values.Value;

public class RefType implements Type {
    Type innerType;

    public RefType(Type innerType) {
        this.innerType = innerType;
    }

    public Type getInnerType() {
        return innerType;
    }

    @Override
    public boolean equals(Type another) {
        if (another instanceof RefType) {
            return innerType.equals(((RefType) another).innerType);
        }
        else
            return false;
    }

    @Override
    public String toString() {
        return "Ref " + innerType.toString();
    }

    @Override
    public Value defaultValue() {
        return new RefValue(0, innerType);
    }
}
