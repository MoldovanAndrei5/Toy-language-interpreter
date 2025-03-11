package model.statements;

import exceptions.ADTException;
import exceptions.ControllerException;
import exceptions.ExpressionException;
import model.expressions.Exp;
import model.programState.Heap.IHeap;
import model.programState.PrgState;
import model.programState.MyDictionary.MyIDictionary;
import model.programState.MyStack.MyIStack;
import model.types.Type;
import model.values.Value;
import exceptions.StatementException;

public class AssignStmt implements IStmt {
    String id;
    Exp exp;

    public AssignStmt(String id, Exp exp) {
        this.id = id;
        this.exp = exp;
    }

    public String toString() {
        return id + " = " + exp.toString();
    }

    @Override
    public PrgState execute(PrgState state) throws ExpressionException, StatementException, ADTException {
        MyIDictionary<String, Value> symTable = state.getSymTable();
        IHeap<Integer, Value> heapTable = state.getHeapTable();

        if (symTable.isDefined(id)) {
            Value val = exp.eval(symTable, heapTable);
            Type typeId = (symTable.lookup(id)).getType();
            if (val.getType().equals(typeId))
                symTable.update(id, val);
            else
                throw new StatementException("Declared type of variable " + id + " and type of the assigned expression do not match");
        }
        else
            throw new StatementException("The used variable " + id + " was not declared before");
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws StatementException, ExpressionException, ADTException {
        Type typevar = typeEnv.lookup(id);
        Type typexp = exp.typecheck(typeEnv);
        if (typevar.equals(typexp))
            return typeEnv;
        else
            throw new StatementException("Assignment: right hand side and left hand side have different types");
    }
}
