package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.expressions.Exp;
import model.programState.Heap.IHeap;
import model.programState.MyDictionary.MyIDictionary;
import model.programState.PrgState;
import model.types.IntType;
import model.types.RefType;
import model.types.Type;
import model.values.RefValue;
import model.values.Value;

public class WriteHeapStmt implements IStmt {
    String name;
    Exp exp;

    public WriteHeapStmt(String name, Exp exp) {
        this.name = name;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        MyIDictionary<String, Value> symTable = state.getSymTable();
        IHeap<Integer, Value> heapTable = state.getHeapTable();

        if (symTable.isDefined(name)) {
            Value val = symTable.lookup(name);
            if (val.getType() instanceof RefType) {
                if (heapTable.isDefined(((RefValue)val).getHeapAddress())) {
                    Value expVal = exp.eval(symTable, heapTable);
                    if (expVal.getType().equals(((RefType) val.getType()).getInnerType())) {
                        heapTable.put(((RefValue) val).getHeapAddress(), expVal);
                    }
                    else
                        throw new ExpressionException("The type of the expression is not the same as the inner type of the variable " + name);
                }
                else
                    throw new ExpressionException("The variable " + name + " is not defined");
            }
            else
                throw new ExpressionException("Variable '" + name + "' is not of type RefType");
        }
        else
            throw new ExpressionException("Variable '" + name + "' not found");
        return null;
    }

    @Override
    public String toString() {
        return "wH(" + name + ", " + exp + ")";
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws StatementException, ExpressionException, ADTException {
        Type typevar = typeEnv.lookup(name);
        Type typexp = exp.typecheck(typeEnv);
        if (typevar.equals(new RefType(typexp))) {
            return typeEnv;
        }
        else
            throw new StatementException("WriteHeapStmt: The type of the variable does not match the type of the expression");
    }
}
