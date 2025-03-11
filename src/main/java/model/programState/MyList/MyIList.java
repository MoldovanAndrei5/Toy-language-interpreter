package model.programState.MyList;

import java.util.List;

public interface MyIList<Value> {
    void add(Value value);
    List<Value> getList();
}
