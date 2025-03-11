package model.statements;

import model.programState.MyDictionary.MyIDictionary;
import model.programState.PrgState;
import exceptions.*;
import model.types.Type;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface IStmt {
    PrgState execute(PrgState state) throws StatementException;
    MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws StatementException, ExpressionException;
}
