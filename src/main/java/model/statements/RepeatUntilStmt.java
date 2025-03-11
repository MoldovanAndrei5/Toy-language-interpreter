package model.statements;

import exceptions.ExpressionException;
import exceptions.StatementException;
import model.expressions.Exp;
import model.expressions.NegationExp;
import model.programState.MyDictionary.MyIDictionary;
import model.programState.PrgState;
import model.types.BoolType;
import model.types.Type;

public class RepeatUntilStmt implements IStmt{
    IStmt stmt;
    Exp exp;

    public RepeatUntilStmt(IStmt stmt, Exp exp) {
        this.stmt = stmt;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        IStmt comptstmt = new ComptStmt(stmt, (new WhileStmt(new NegationExp(exp), stmt)));
        state.getStack().push(comptstmt);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws StatementException, ExpressionException {
        Type typexp = exp.typecheck(typeEnv);
        if (typexp.equals(new BoolType())) {
            stmt.typecheck(typeEnv);
            return typeEnv;
        }
        else
            throw new StatementException("The condition of until doesn't have the type bool");
    }

    @Override
    public String toString() {
        return "repeat " + stmt.toString() + " until " + exp.toString();
    }
}
