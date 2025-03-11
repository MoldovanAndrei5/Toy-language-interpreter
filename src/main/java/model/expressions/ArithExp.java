package model.expressions;

import exceptions.StatementException;
import model.programState.Heap.IHeap;
import model.programState.MyDictionary.MyIDictionary;
import model.types.IntType;
import model.types.Type;
import model.values.IntValue;
import model.values.Value;
import exceptions.ExpressionException;

public class ArithExp implements Exp {
    Exp e1;
    Exp e2;
    char op;

    public ArithExp(char op, Exp e1, Exp e2) {
        this.op = op;
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> table, IHeap<Integer, Value> heapTable) throws ExpressionException, StatementException {
        Value v1, v2;
        v1 = e1.eval(table, heapTable);
        if (v1.getType().equals(new IntType())) {
            v2 = e2.eval(table, heapTable);
            if (v2.getType().equals(new IntType())) {
                IntValue i1 = (IntValue) v1;
                IntValue i2 = (IntValue) v2;
                int n1, n2;
                n1 = i1.getValue();
                n2 = i2.getValue();
                if (op == '+')
                    return new IntValue(n1 + n2);
                else if (op == '-')
                    return new IntValue(n1 - n2);
                else if (op == '*')
                    return new IntValue(n1 * n2);
                else if (op == '/') {
                    if (n2 == 0)
                        throw new ExpressionException("Division by zero");
                    else
                        return new IntValue(n1 / n2);
                }
                else
                    throw new ExpressionException("Invalid operator");
            }
            else
                throw new ExpressionException("Second operand is not an integer");
        }
        else
            throw new ExpressionException("First operand is not an integer");
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
        if (typ1.equals(new IntType())) {
            if (typ2.equals(new IntType())) {
                return new IntType();
            } else {
                throw new ExpressionException("Second operand is not an integer");
            }
        }
        else {
            throw new ExpressionException("First operand is not an integer");
        }
    }
}
