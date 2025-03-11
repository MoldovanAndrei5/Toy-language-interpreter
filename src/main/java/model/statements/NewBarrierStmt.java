package model.statements;

import com.sun.jdi.IntegerType;
import exceptions.ExpressionException;
import exceptions.StatementException;
import javafx.util.Pair;
import model.expressions.Exp;
import model.programState.BarrierTable.IBarrierTable;
import model.programState.Heap.IHeap;
import model.programState.MyDictionary.MyIDictionary;
import model.programState.PrgState;
import model.types.IntType;
import model.types.Type;
import model.values.IntValue;
import model.values.Value;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NewBarrierStmt implements IStmt {
    private String var;
    private Exp exp;
    private static final Lock lock = new ReentrantLock();

    public NewBarrierStmt(String var, Exp exp) {
        this.var = var;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        lock.lock();
        MyIDictionary<String, Value> symTable = state.getSymTable();
        IHeap<Integer, Value> heap = state.getHeapTable();
        IBarrierTable barrierTable = state.getBarrierTable();
        Value num = exp.eval(symTable, heap);
        if (num instanceof IntValue) {
            IntValue n = (IntValue) num;
            int nr = n.getValue();
            int freeAddress = barrierTable.getFreeAddress();
            barrierTable.put(freeAddress, new Pair<>(nr, new ArrayList<>()));
            if (symTable.isDefined(var))
                if (symTable.lookup(var).getType() instanceof IntType)
                    symTable.update(var, new IntValue(freeAddress));
                else
                    throw new StatementException("Variable '" + var + "' is not an int");
            else
                throw new StatementException(var + " is not defined in the symbol table");
        }
        else
            throw new StatementException("Expression dos not have an int value");
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws StatementException, ExpressionException {
        if (typeEnv.lookup(var).equals(new IntType())) {
            if (exp.typecheck(typeEnv).equals(new IntType()))
                return typeEnv;
            else
                throw new StatementException("Expression is not of type int");
        }
        else
            throw new StatementException("Variable '" + var + "' is not an int");
    }

    @Override
    public String toString() {
        return "newBarrier(" + var + ", " + exp + ")";
    }
}
