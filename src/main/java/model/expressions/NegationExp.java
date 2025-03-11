package model.expressions;

import exceptions.ExpressionException;
import exceptions.StatementException;
import model.expressions.Exp;
import model.programState.Heap.IHeap;
import model.programState.MyDictionary.MyIDictionary;
import model.types.BoolType;
import model.types.Type;
import model.values.BoolValue;
import model.values.Value;

public class NegationExp implements Exp {
    private Exp exp;

    public NegationExp(Exp exp) {
        this.exp = exp;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> table, IHeap<Integer, Value> heapTable) throws ExpressionException, StatementException {
        BoolValue bool = (BoolValue) exp.eval(table, heapTable);
        return new BoolValue(!bool.getValue());
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws ExpressionException {
        Type type = exp.typecheck(typeEnv);
        if (type instanceof BoolType)
            return new BoolType();
        else
            throw new ExpressionException("The expression must be boolean");
    }

    @Override
    public String toString() {
        return "!" + exp.toString();
    }
}
