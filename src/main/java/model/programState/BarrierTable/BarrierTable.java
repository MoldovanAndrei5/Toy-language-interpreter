package model.programState.BarrierTable;

import exceptions.ADTException;
import exceptions.StatementException;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

public class BarrierTable implements IBarrierTable {
    HashMap<Integer, Pair<Integer, List<Integer>>> barrierTable;
    private int freeLocation = 0;

    public BarrierTable() {
        barrierTable = new HashMap<>();
    }

    public HashMap<Integer, Pair<Integer, List<Integer>>> getBarrierTable() {
        return barrierTable;
    }

    public void setBarrierTable(HashMap<Integer, Pair<Integer, List<Integer>>> barrierTable) {
        this.barrierTable = barrierTable;
    }

    @Override
    public Pair<Integer, List<Integer>> get(int key) throws StatementException {
        synchronized (this) {
            if (barrierTable.containsKey(key)) {
                return barrierTable.get(key);
            } else {
                throw new StatementException(String.format("Barrier table doesn't contain the key %d!", key));
            }
        }
    }

    @Override
    public int getFreeAddress() {
        synchronized (this) {
            freeLocation++;
            return freeLocation;
        }
    }

    @Override
    public void setFreeAddress(int freeAddress) {
        synchronized (this) {
            this.freeLocation = freeAddress;
        }
    }

    @Override
    public void put(int key, Pair<Integer, List<Integer>> value) throws ADTException {
        synchronized (this) {
            if (!barrierTable.containsKey(key)) {
                barrierTable.put(key, value);
            } else {
                throw new ADTException(String.format("Barrier table already contains the key %d!", key));
            }
        }
    }

    @Override
    public boolean containsKey(int key) {
        synchronized (this) {
            return barrierTable.containsKey(key);
        }
    }

    @Override
    public void update(int key, Pair<Integer, List<Integer>> value) {
        synchronized (this) {
            if (barrierTable.containsKey(key)) {
                barrierTable.replace(key, value);
            }
            else
                throw new ADTException("BarrierTable does not contain key " + key);
        }
    }

    @Override
    public String toString() {
        return barrierTable.toString();
    }
}
