package model.programState.MyStack;

import exceptions.ADTException;

import java.util.List;

public interface MyIStack<T> {
    T pop() throws ADTException;
    void push(T value);
    boolean isEmpty();
    String toString();
    List<T> getReversed();
}
