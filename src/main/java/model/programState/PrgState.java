package model.programState;

import exceptions.ADTException;
import exceptions.ControllerException;
import exceptions.StatementException;
import exceptions.PrgStateException;
import javafx.util.Pair;
import model.programState.BarrierTable.IBarrierTable;
import model.programState.FileTable.IFileTable;
import model.programState.Heap.IHeap;
import model.programState.MyDictionary.MyIDictionary;
import model.programState.MyList.MyIList;
import model.programState.MyStack.MyIStack;
import model.statements.IStmt;
import model.values.Value;
import java.io.BufferedReader;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PrgState {
    MyIStack<IStmt> exeStack;
    MyIDictionary<String, Value> symTable;
    MyIList<Value> out;
    IFileTable<String, BufferedReader> fileTable;
    IHeap<Integer, Value> heapTable;
    IBarrierTable barrierTable;
    IStmt originalProgram;
    private int id;
    private static int lastId = 0;

    public PrgState(MyIStack<IStmt> stack, MyIDictionary<String, Value> table, MyIList<Value> o, IFileTable<String, BufferedReader> fTable, IHeap<Integer, Value> hTable, IBarrierTable barTable, IStmt prg) {
        exeStack = stack;
        symTable = table;
        out = o;
        fileTable = fTable;
        heapTable = hTable;
        barrierTable = barTable;
        originalProgram = deepCopy(prg);
        id = setId();
        exeStack.push(originalProgram);
    }

    public synchronized int setId()
    {
        lastId++;
        return lastId;
    }

    public int getId() {
        return this.id;
    }

    public boolean isNotCompleted() {
        return !exeStack.isEmpty();
    }

    public PrgState oneStep() throws PrgStateException, StatementException, ADTException {
        if (exeStack.isEmpty()) {
            throw new PrgStateException("Stack is empty");
        }
        IStmt stmt = exeStack.pop();
        return stmt.execute(this);
    }

    @Override
    public String toString() {
        return "Program State id " + id + ": \n" + exeStack.toString() + "\n" + heapTable.toString() + "\n" + symTable.toString() + "\n" + out.toString() + "\n" + fileTable.toString() + "\n\n";
    }

    public MyIStack<IStmt> getStack() {
        return exeStack;
    }

    public MyIDictionary<String, Value> getSymTable() {
        return symTable;
    }

    public MyIList<Value> getOut() {
        return out;
    }

    public IFileTable<String, BufferedReader> getFileTable() {
        return fileTable;
    }

    public IHeap<Integer, Value> getHeapTable() {
        return heapTable;
    }

    public IBarrierTable getBarrierTable() {
        return barrierTable;
    }

    public void setExeStack(MyIStack<IStmt> exeStack) {
        this.exeStack = exeStack;
    }

    public void setSymTable(MyIDictionary<String, Value> symTable) {
        this.symTable = symTable;
    }

    public void setOut(MyIList<Value> out) {
        this.out = out;
    }

    public void setFileTable(IFileTable<String, BufferedReader> fileTable) {
        this.fileTable = fileTable;
    }

    public void setHeapTable(IHeap<Integer, Value> heapTable) {
        this.heapTable = heapTable;
    }

    public void setBarrierTable(IBarrierTable barrierTable) {
        this.barrierTable = barrierTable;
    }

    IStmt deepCopy(IStmt stmt) {
        return stmt;
    }
}
