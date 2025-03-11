package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.expressions.Exp;
import model.programState.Heap.IHeap;
import model.programState.MyDictionary.MyDictionary;
import model.programState.PrgState;
import model.programState.MyDictionary.MyIDictionary;
import model.types.BoolType;
import model.types.Type;
import model.values.BoolValue;
import model.values.Value;

public class IfStmt implements IStmt {
    Exp exp;
    IStmt thenS;
    IStmt elseS;

    public IfStmt(Exp e, IStmt t, IStmt el) {
        exp = e;
        thenS = t;
        elseS = el;
    }

    @Override
    public String toString() {
        return "(IF(" + exp.toString() + ") THEN (" + thenS.toString() + ") ELSE (" + elseS.toString() + "))";
    }

    @Override
    public PrgState execute(PrgState state) throws ExpressionException, StatementException {
        MyIDictionary<String, Value> dict = state.getSymTable();
        IHeap<Integer, Value> heapTable = state.getHeapTable();
        Value value = exp.eval(dict, heapTable);

        if (value.getType().equals(new BoolType())) {
            BoolValue b = (BoolValue) value;
            if (b.getValue()) {
                state.getStack().push(thenS);
            }
            else
                state.getStack().push(elseS);
        }
        else
            throw new StatementException("The expression must be boolean");
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws StatementException, ExpressionException, ADTException {
        Type typexp = exp.typecheck(typeEnv);
        if (typexp.equals(new BoolType())) {
            thenS.typecheck(typeEnv);
            elseS.typecheck(typeEnv);
            return typeEnv;
        }
        else
            throw new StatementException("The condition of IF doesn't have the type bool");
    }
}
