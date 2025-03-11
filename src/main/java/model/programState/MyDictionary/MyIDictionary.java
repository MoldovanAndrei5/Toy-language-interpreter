package model.programState.MyDictionary;

import exceptions.ADTException;

import java.util.Map;

public interface MyIDictionary<Key, Value> {
    boolean isDefined(Key key);
    Value lookup(Key key) throws ADTException;
    void put(Key key, Value value);
    void update(Key key, Value value) throws ADTException;
    Map<Key, Value> getContent();
}
