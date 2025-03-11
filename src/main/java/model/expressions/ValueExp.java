package model.expressions;

import model.programState.Heap.IHeap;
import model.programState.MyDictionary.MyIDictionary;
import model.types.Type;
import model.values.Value;
import exceptions.ExpressionException;

public class ValueExp implements Exp {
    Value value;

    public ValueExp(Value value) {
        this.value = value;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> table, IHeap<Integer, Value> heapTable) throws ExpressionException {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws ExpressionException {
        return value.getType();
    }
}
