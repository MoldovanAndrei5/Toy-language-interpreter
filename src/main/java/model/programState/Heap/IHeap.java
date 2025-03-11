package model.programState.Heap;

import exceptions.ADTException;
import model.values.Value;

import java.util.Map;

public interface IHeap<Address, Content> {
    int getFreeLocation();
    boolean isDefined(Address address);
    Content lookup(Address address) throws ADTException;
    void put(Address address, Content content);
    void update(Address key, Content value) throws ADTException;
    void setContent(Map<Address, Content> newHeap);
    Map<Address, Content> getContent();
    void getNextFreeLocation();
}
