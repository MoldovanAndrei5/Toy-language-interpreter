package model.statements;

import exceptions.ExpressionException;
import model.expressions.Exp;
import model.programState.Heap.IHeap;
import model.programState.PrgState;
import exceptions.StatementException;
import model.programState.MyDictionary.MyIDictionary;
import model.programState.MyList.MyIList;
import model.types.Type;
import model.values.Value;

public class PrintStmt implements IStmt {
    Exp exp;

    public PrintStmt(Exp exp) {
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "print(" + exp.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws ExpressionException, StatementException {
        MyIList<Value> out = state.getOut();
        MyIDictionary<String, Value> symTable = state.getSymTable();
        IHeap<Integer, Value> heapTable = state.getHeapTable();
        out.add(exp.eval(symTable, heapTable));
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws StatementException, ExpressionException {
        exp.typecheck(typeEnv);
        return typeEnv;
    }
}
