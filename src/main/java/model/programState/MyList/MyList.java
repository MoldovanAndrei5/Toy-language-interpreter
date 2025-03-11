package model.programState.MyList;

import java.util.ArrayList;
import java.util.List;

public class MyList<Value> implements MyIList<Value> {
    private List<Value> list;

    public MyList() {
        this.list = new ArrayList<Value>();
    }

    @Override
    public void add(Value value) {
        this.list.add(value);
    }

    @Override
    public List<Value> getList() {
        return this.list;
    }

    public void setList(List<Value> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Out:\n");
        for (Value v : this.list) {
            s.append(v.toString()).append("\n");
        }
        return s.toString();
    }
}
