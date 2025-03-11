package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.expressions.Exp;
import model.programState.FileTable.IFileTable;
import model.programState.Heap.IHeap;
import model.programState.MyDictionary.MyIDictionary;
import model.programState.PrgState;
import model.types.BoolType;
import model.types.StringType;
import model.types.Type;
import model.values.Value;

import java.io.*;

public class OpenRFileStmt implements IStmt {
    Exp exp;

    public OpenRFileStmt(Exp exp) {
        this.exp = exp;
    }

    public String toString() {
        return "openRFile(" + exp.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionException, ADTException {
        IFileTable<String, BufferedReader> fileTable = state.getFileTable();
        MyIDictionary<String, Value> symTable = state.getSymTable();
        IHeap<Integer, Value> heapTable = state.getHeapTable();

        Value val = exp.eval(symTable, heapTable);
        if (val.getType().equals(new StringType())) {
            if (!fileTable.isDefined(val.toString())) {
                FileReader in;
                try {
                    in = new FileReader(val.toString());
                }
                catch (IOException e) {
                    throw new StatementException("Error opening file: " + val.toString());
                }
                BufferedReader br = new BufferedReader(in);
                fileTable.put(val.toString(), br);
            }
            else
                throw new StatementException("The expression \"" + exp.toString() + "\" has already been evaluated.");
        }
        else
            throw new StatementException("The expression must be a string");
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws StatementException, ExpressionException, ADTException {
        exp.typecheck(typeEnv);
        return typeEnv;
    }
}
