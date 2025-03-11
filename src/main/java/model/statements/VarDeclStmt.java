package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.programState.PrgState;
import model.programState.MyDictionary.MyIDictionary;
import model.types.Type;
import model.values.Value;

public class VarDeclStmt implements IStmt {
    String name;
    Type type;

    public VarDeclStmt(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        MyIDictionary<String, Value> symTable = state.getSymTable();
        if (symTable.isDefined(name))
            throw new StatementException("Variable " + name + " already exists");
        else
            symTable.put(name, type.defaultValue());
        return null;
    }

    @Override
    public String toString() {
        return name + " = " + type;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws StatementException, ExpressionException, ADTException {
        typeEnv.put(name, type);
        return typeEnv;
    }
}
