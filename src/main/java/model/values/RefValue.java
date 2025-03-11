package model.values;

import model.types.RefType;
import model.types.Type;

public class RefValue implements Value {
    int heapAddress;
    Type locationType;

    public RefValue(int heapAddress, Type locationType) {
        this.heapAddress = heapAddress;
        this.locationType = locationType;
    }

    public int getHeapAddress() {
        return heapAddress;
    }

    @Override
    public Type getType() {
        return new RefType(locationType);
    }

    @Override
    public String toString() {
        return "(" + heapAddress + ", " + locationType + ")";
    }

    @Override
    public boolean equals(Value another) {
        return another instanceof RefValue && locationType == ((RefValue) another).locationType;
    }

    public int getAddr() {
        return heapAddress;
    }

    public void setAddr(int addr) {
        heapAddress = addr;
    }

    @Override
    public Value deepCopy() {
        return new RefValue(heapAddress, locationType);
    }
}
