package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.expressions.Exp;
import model.programState.Heap.IHeap;
import model.programState.MyDictionary.MyIDictionary;
import model.programState.PrgState;
import model.types.BoolType;
import model.types.RefType;
import model.types.Type;
import model.values.RefValue;
import model.values.Value;

import java.util.List;
import java.util.stream.Collectors;

public class NewStmt implements IStmt {
    String name;
    Exp exp;

    public NewStmt(String name, Exp exp) {
        this.name = name;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        MyIDictionary<String, Value> symTable = state.getSymTable();
        IHeap<Integer, Value> heapTable = state.getHeapTable();

        if (symTable.isDefined(name)) {
            Type varType = symTable.lookup(name).getType();
            if (varType instanceof RefType) {
                Value val = exp.eval(symTable, heapTable);
                if (val.getType().equals((((RefType) varType).getInnerType()))) {
                    int address = heapTable.getFreeLocation();
                    heapTable.put(address, val);
                    ((RefValue)symTable.lookup(name)).setAddr(address);
                }
                else
                    throw new StatementException("The types of the variable " + name + " locationType and the expression are not the same");
            }
            else
                throw new StatementException("Variable " + name + " is not a reference type");
        }
        else
            throw new StatementException("The used variable " + name + " was not declared before");
        return null;
    }

    @Override
    public String toString() {
        return "new(" + name + ", " + exp + ")";
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws StatementException, ExpressionException, ADTException {
        Type typevar = typeEnv.lookup(name);
        Type typexp = exp.typecheck(typeEnv);
        if (typevar.equals(new RefType(typexp))) {
            return typeEnv;
        }
        else
            throw new StatementException("NewStmt: right hand side and left hand side have different types");
    }
}
