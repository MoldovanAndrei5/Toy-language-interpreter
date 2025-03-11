package model.types;

import model.values.Value;

public interface Type {
    String toString();
    boolean equals(Type another);
    Value defaultValue();
}
