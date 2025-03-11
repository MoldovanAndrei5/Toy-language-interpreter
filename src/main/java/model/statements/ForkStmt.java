package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.programState.MyDictionary.MyDictionary;
import model.programState.MyDictionary.MyIDictionary;
import model.programState.MyStack.MyStack;
import model.programState.PrgState;
import model.types.BoolType;
import model.types.Type;
import model.values.Value;

import java.util.Map;

public class ForkStmt implements IStmt {
    IStmt stmt;

    public ForkStmt(IStmt stmt) {
        this.stmt = stmt;
    }

    public String toString() {
        return "fork(" + stmt.toString() + ")";
    }

    public PrgState execute(PrgState state) throws StatementException {
        MyStack<IStmt> newStack = new MyStack<IStmt>();
        MyIDictionary<String, Value> symTable = state.getSymTable();
        MyIDictionary<String, Value> symTableClone = new MyDictionary<String, Value>();
        for (Map.Entry<String, Value> i : symTable.getContent().entrySet()) {
            symTableClone.put(i.getKey(), i.getValue().deepCopy());
        }
        PrgState newState = new PrgState(newStack, symTableClone, state.getOut(), state.getFileTable(), state.getHeapTable(), state.getBarrierTable(), stmt);
        return newState;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws StatementException, ExpressionException, ADTException {
        stmt.typecheck(typeEnv);
        return typeEnv;
    }
}
