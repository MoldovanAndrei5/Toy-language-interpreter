package model.expressions;

import exceptions.ExpressionException;
import exceptions.StatementException;
import model.programState.Heap.IHeap;
import model.programState.MyDictionary.MyIDictionary;
import model.types.BoolType;
import model.types.RefType;
import model.types.Type;
import model.values.RefValue;
import model.values.Value;

public class ReadHeapExp implements Exp {
    Exp exp;

    public ReadHeapExp(Exp exp) {
        this.exp = exp;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> table, IHeap<Integer, Value> heapTable) throws ExpressionException, StatementException {
        Value val = exp.eval(table, heapTable);
        if (val instanceof RefValue) {
            int address = ((RefValue) val).getHeapAddress();
            if (heapTable.isDefined(address)) {
                return heapTable.lookup(((RefValue) val).getHeapAddress());
            }
            else
                throw new ExpressionException("The address is not a key in the heap table");
        }
        else
            throw new ExpressionException("rH expects a ref value");
    }

    @Override
    public String toString() {
        return "rH(" + exp.toString() + ")";
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws ExpressionException {
        Type typ = exp.typecheck(typeEnv);
        if (typ instanceof RefType) {
            RefType refType = (RefType) typ;
            return refType.getInnerType();
        }
        else {
            throw new ExpressionException("The rH argument is not a Ref Type");
        }
    }
}
