package model.statements;

import exceptions.ExpressionException;
import exceptions.StatementException;
import javafx.util.Pair;
import model.programState.BarrierTable.IBarrierTable;
import model.programState.MyDictionary.MyIDictionary;
import model.programState.PrgState;
import model.types.IntType;
import model.types.Type;
import model.values.IntValue;
import model.values.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AwaitStmt implements IStmt {
    private String var;
    private static final Lock lock = new ReentrantLock();

    public AwaitStmt(String var) {
        this.var = var;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        lock.lock();
        MyIDictionary<String, Value> symTable = state.getSymTable();
        IBarrierTable barrierTable = state.getBarrierTable();
        if (symTable.isDefined(var)) {
            if (symTable.lookup(var).getType().equals(new IntType())) {
                int foundIndex = ((IntValue) symTable.lookup(var)).getValue();
                if (barrierTable.containsKey(foundIndex)) {
                    Pair<Integer, List<Integer>> foundBarrier = barrierTable.get(foundIndex);
                    int NL = foundBarrier.getValue().size();
                    int N1 = foundBarrier.getKey();
                    ArrayList<Integer> list = (ArrayList<Integer>) foundBarrier.getValue();
                    if (N1 > NL) {
                        if (list.contains(state.getId()))
                            state.getStack().push(this);
                        else {
                            list.add(state.getId());
                            barrierTable.update(foundIndex, new Pair<>(N1, list));
                            state.setBarrierTable(barrierTable);
                            state.getStack().push(this);
                        }
                    }
                }
                else
                    throw new StatementException("Variable " + var + " is not in the barrier table");
            }
            else
                throw new StatementException("Variable " + var + " is not of type int");
        }
        else
            throw new StatementException("Variable " + var + " is not defined");
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws StatementException, ExpressionException {
        if (typeEnv.lookup(var).equals(new IntType()))
            return typeEnv;
        else
            throw new StatementException("Variable " + var + " does not have the type int");
    }

    @Override
    public String toString() {
        return "await(" + var + ")";
    }
}