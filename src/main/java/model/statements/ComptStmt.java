package model.statements;

import exceptions.ExpressionException;
import model.programState.MyDictionary.MyIDictionary;
import model.programState.PrgState;
import exceptions.StatementException;
import model.programState.MyStack.MyIStack;
import model.types.Type;

public class ComptStmt implements IStmt {
    IStmt first;
    IStmt second;

    public ComptStmt(IStmt first, IStmt second) {
        this.first = first;
        this.second = second;
    }

    public String toString() {
        return "(" + first.toString() + ";" + second.toString() + ")";
    }

    public IStmt getFirst() {
        return first;
    }

    public IStmt getSecond() {
        return second;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        MyIStack<IStmt> stack = state.getStack();
        stack.push(second);
        stack.push(first);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws StatementException, ExpressionException {
        return second.typecheck(first.typecheck(typeEnv));
    }
}
