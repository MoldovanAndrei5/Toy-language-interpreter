package model.programState.MyDictionary;

import exceptions.ADTException;

import java.util.HashMap;
import java.util.Map;

public class MyDictionary<Key, Value> implements MyIDictionary<Key, Value> {
    private Map<Key, Value> dict;

    public MyDictionary() {
        this.dict = new HashMap<Key, Value>();
    }

    @Override
    public boolean isDefined(Key key) {
        return this.dict.containsKey(key);
    }

    @Override
    public Value lookup(Key key) throws ADTException {
        if (this.isDefined(key))
            return this.dict.get(key);
        else
            throw new ADTException("Key not found");
    }

    @Override
    public void put(Key key, Value value) {
        this.dict.put(key, value);
    }

    @Override
    public void update(Key key, Value value) throws ADTException {
        if (this.isDefined(key))
            this.dict.put(key, value);
        else
            throw new ADTException("Key not found");
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("SymTable:\n");
        for (Key key : this.dict.keySet()) {
            s.append(key.toString()).append("->").append(this.dict.get(key).toString()).append("\n");
        }
        return s.toString();
    }

    @Override
    public Map<Key, Value> getContent() {
        return this.dict;
    }
}
