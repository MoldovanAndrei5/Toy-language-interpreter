package model.values;

import model.types.*;

public interface Value {
    Type getType();
    String toString();
    boolean equals(Value another);
    Value deepCopy();
}
