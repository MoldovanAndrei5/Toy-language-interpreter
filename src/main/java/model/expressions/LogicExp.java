package model.expressions;

import exceptions.StatementException;
import model.programState.Heap.IHeap;
import model.programState.MyDictionary.MyIDictionary;
import model.types.BoolType;
import model.types.IntType;
import model.types.Type;
import model.values.BoolValue;
import model.values.Value;
import exceptions.ExpressionException;

public class LogicExp implements Exp {
    Exp e1;
    Exp e2;
    String op;

    @Override
    public Value eval(MyIDictionary<String, Value> table, IHeap<Integer, Value> heapTable) throws ExpressionException, StatementException {
        Value v1 = e1.eval(table, heapTable);
        if (v1.getType().equals(new BoolType())) {
            Value v2 = (Value) e2.eval(table, heapTable);
            if (v2.getType().equals(new BoolType())) {
                BoolValue b1 = (BoolValue) v1;
                BoolValue b2 = (BoolValue) v2;
                boolean n1 = b1.getValue(), n2 = b2.getValue();
                if (op == "&&")
                    return new BoolValue(n1 && n2);
                else if (op == "||")
                    return new BoolValue(n2 || n1);
                else
                    throw new ExpressionException("Invalid operator");
            }
            else
                throw new ExpressionException("Second operand is not a bool");
        }
        else
            throw new ExpressionException("First operand is not a bool");
    }

    @Override
    public String toString() {
        return e1 + " " + op + " " + e2;
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws ExpressionException {
        Type typ1, typ2;
        typ1 = e1.typecheck(typeEnv);
        typ2 = e2.typecheck(typeEnv);
        if (typ1.equals(new BoolType())) {
            if (typ2.equals(new BoolType())) {
                return new BoolType();
            } else {
                throw new ExpressionException("Second operand is not an boolean");
            }
        }
        else {
            throw new ExpressionException("First operand is not an boolean");
        }
    }
}
