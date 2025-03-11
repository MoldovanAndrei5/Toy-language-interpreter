package model.programState.FileTable;

import exceptions.ADTException;

import java.util.Map;

public interface IFileTable<Key, Value> {
    boolean isDefined(Key key);
    Value lookup(Key key) throws ADTException;
    void put(Key key, Value value);
    void update(Key key, Value value) throws ADTException;
    void delete(Key key) throws ADTException;
    Map<Key, Value> getContent();
}
