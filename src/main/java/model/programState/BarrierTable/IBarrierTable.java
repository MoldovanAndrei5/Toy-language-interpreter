package model.programState.BarrierTable;

import exceptions.ADTException;
import exceptions.StatementException;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

public interface IBarrierTable {
    void update(int key, Pair<Integer, List<Integer>> value);
    boolean containsKey(int key);
    HashMap<Integer, Pair<Integer, List<Integer>>> getBarrierTable();
    void setBarrierTable(HashMap<Integer, Pair<Integer, List<Integer>>> barrierTable);
    void put(int key, Pair<Integer, List<Integer>> value) throws ADTException;
    int getFreeAddress();
    void setFreeAddress(int freeAddress);
    Pair<Integer, List<Integer>> get(int key) throws StatementException;
}
