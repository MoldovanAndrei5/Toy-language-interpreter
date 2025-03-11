package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.expressions.Exp;
import model.programState.FileTable.IFileTable;
import model.programState.Heap.IHeap;
import model.programState.MyDictionary.MyIDictionary;
import model.programState.PrgState;
import model.types.IntType;
import model.types.StringType;
import model.types.Type;
import model.values.IntValue;
import model.values.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFileStmt implements IStmt {
    Exp exp;
    String varName;

    public ReadFileStmt(Exp exp, String varName) {
        this.exp = exp;
        this.varName = varName;
    }

    public String toString() {
        return "readFile(" + exp + ", " + varName + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionException, ADTException {
        IFileTable<String, BufferedReader> fileTable = state.getFileTable();
        MyIDictionary<String, Value> symTable = state.getSymTable();
        IHeap<Integer, Value> heapTable = state.getHeapTable();

        if (symTable.isDefined(varName)) {
            if (symTable.lookup(varName).getType().equals(new IntType())) {
                Value val = exp.eval(symTable, heapTable);
                if (val.getType().equals(new StringType())) {
                    if (fileTable.isDefined(val.toString())) {
                        BufferedReader br = fileTable.lookup(val.toString());
                        String line;
                        try {
                            line = br.readLine();
                        }
                        catch (IOException e) {
                            throw new StatementException("There are no lines left to read");
                        }
                        int intValue;
                        if (line == null) {
                            intValue = 0;
                        }
                        else
                            intValue = Integer.parseInt(line);
                        symTable.update(varName, new IntValue(intValue));
                    }
                    else
                        throw new StatementException("The file has not been opened");
                }
                else
                    throw new StatementException("The expression must be a string");
            }
            else
                throw new StatementException("The variable must be an int");
        }
        else
            throw new StatementException("Variable " + varName + " not found");
        return null;
    }


    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws StatementException, ExpressionException, ADTException {
        Type typevar = typeEnv.lookup(varName);
        if (typevar.equals(new IntType())) {
            exp.typecheck(typeEnv);
            return typeEnv;
        }
        else
            throw new StatementException("ReadFileStmt: The variable " + varName + " is not an int");
    }
}
