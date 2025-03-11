package model.programState.MyStack;

import exceptions.ADTException;
import model.statements.ComptStmt;
import model.statements.IStmt;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class MyStack<T> implements MyIStack<T> {
    private Stack<T> stack;

    public MyStack() {
        this.stack = new Stack<>();
    }

    public Stack<T> getStack() {
        return stack;
    }

    public void setStack(Stack<T> stack) {
        this.stack = stack;
    }

    @Override
    public T pop() throws ADTException {
        if (stack.isEmpty())
            throw new ADTException("Stack is empty");
        return stack.pop();
    }

    @Override
    public void push(T value) {
        this.stack.push(value);
    }

    @Override
    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("ExeStack:\n");
        if (!this.stack.isEmpty()) {
            T st = stack.peek();
            IStmt stmt = (IStmt) st;
            while (stmt instanceof ComptStmt) {
                ComptStmt compStmt = (ComptStmt) stmt;
                s.append(" ").append(compStmt.getFirst().toString()).append("\n");
                stmt = compStmt.getSecond();
            }
            s.append(" ").append(stmt.toString()).append("\n");
        }
        return s.toString();
    }

    @Override
    public List<T> getReversed() {
        List<T> list = Arrays.asList((T[]) stack.toArray());
        Collections.reverse(list);
        return list;
    }
}
