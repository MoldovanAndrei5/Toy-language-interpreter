package model.expressions;

import exceptions.ADTException;
import exceptions.StatementException;
import model.programState.Heap.IHeap;
import model.types.Type;
import model.values.Value;
import exceptions.ExpressionException;
import model.programState.MyDictionary.MyIDictionary;

public class VarExp implements Exp {
    String id;

    public VarExp(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> table, IHeap<Integer, Value> heapTable) throws ExpressionException, ADTException {
        return table.lookup(id);
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws ExpressionException {
        return typeEnv.lookup(id);
    }
}
