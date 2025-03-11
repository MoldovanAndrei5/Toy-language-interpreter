package model.expressions;

import exceptions.StatementException;
import model.programState.Heap.IHeap;
import model.programState.MyDictionary.MyIDictionary;
import model.types.Type;
import model.values.Value;
import exceptions.ExpressionException;

public interface Exp {
    Value eval(MyIDictionary<String, Value> table, IHeap<Integer, Value> heapTable) throws ExpressionException, StatementException;
    Type typecheck(MyIDictionary<String, Type> typeEnv) throws ExpressionException;
}
