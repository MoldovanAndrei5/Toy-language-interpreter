package model.programState.Heap;

import exceptions.ADTException;
import model.values.Value;

import java.util.HashMap;
import java.util.Map;

public class Heap<Address, Content> implements IHeap<Address, Content> {
    private Map<Address, Content> heap;
    int freeLocation;

    public Heap() {
        heap = new HashMap<Address, Content>();
        freeLocation = 1;
    }

    public int getFreeLocation() {
        return freeLocation;
    }

    public void getNextFreeLocation() {
        int free = 1;
        while (heap.containsKey(free)) {
            free++;
        }
        freeLocation = free;
    }

    @Override
    public boolean isDefined(Address address) {
        return heap.containsKey(address);
    }

    @Override
    public Content lookup(Address address) throws ADTException {
        if (this.isDefined(address)) {
            return heap.get(address);
        }
        else
            throw new ADTException("Address not found");
    }

    @Override
    public void put(Address address, Content content) {
        heap.put(address, content);
        getNextFreeLocation();
    }

    @Override
    public void update(Address key, Content value) throws ADTException {
        if (this.isDefined(key)) {
            heap.put(key, value);
        }
        else
            throw new ADTException("Address not found");
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("HeapTable:\n");
        for (Address address : this.heap.keySet()) {
            s.append(address.toString()).append("->").append(heap.get(address).toString()).append("\n");
        }
        return s.toString();
    }

    @Override
    public void setContent(Map<Address, Content> newHeap) {
        heap = newHeap;
    }

    @Override
    public Map<Address, Content> getContent() {
        return heap;
    }
}
