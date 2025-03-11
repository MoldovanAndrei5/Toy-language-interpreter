package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.programState.MyDictionary.MyIDictionary;
import model.programState.PrgState;
import model.types.BoolType;
import model.types.Type;

public class NopStmt implements IStmt {

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        return null;
    }

    @Override
    public String toString() {
        return "{}";
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws StatementException, ExpressionException, ADTException {
        return typeEnv;
    }
}
