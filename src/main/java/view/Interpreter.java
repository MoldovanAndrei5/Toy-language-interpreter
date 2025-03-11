package view;

import controller.Controller;
import exceptions.ExpressionException;
import exceptions.StatementException;
import javafx.util.Pair;
import model.expressions.ArithExp;
import model.expressions.ReadHeapExp;
import model.expressions.ValueExp;
import model.expressions.VarExp;
import model.programState.BarrierTable.BarrierTable;
import model.programState.FileTable.FileTable;
import model.programState.Heap.Heap;
import model.programState.MyDictionary.MyDictionary;
import model.programState.MyDictionary.MyIDictionary;
import model.programState.MyList.MyList;
import model.programState.MyStack.MyStack;
import model.programState.PrgState;
import model.statements.*;
import model.types.*;
import model.values.BoolValue;
import model.values.IntValue;
import model.values.StringValue;
import model.values.Value;
import repository.IRepository;
import repository.Repository;
import view.*;
import view.commands.ExitCommand;
import view.commands.RunExample;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class Interpreter {
    public static Controller createController(IStmt ex, String logFilePath) {
        MyStack<IStmt> stack = new MyStack<IStmt>();
        MyDictionary<String, Value> symTable = new MyDictionary<String, Value>();
        MyList<Value> out = new MyList<Value>();
        FileTable<String, BufferedReader> fileTable = new FileTable<String, BufferedReader>();
        Heap<Integer, Value> heap = new Heap<Integer, Value>();
        BarrierTable barrier = new BarrierTable();
        try {
            ex.typecheck(new MyDictionary<String, Type>());
        }
        catch (StatementException | ExpressionException e) {
            System.out.println("Type check failed!\n" + e.getMessage());
            exit(1);
        }
        PrgState prg = new PrgState(stack, symTable, out, fileTable, heap, barrier, ex);
        List<PrgState> prgList = new ArrayList<PrgState>();
        prgList.add(prg);
        IRepository repo = new Repository(prgList, logFilePath);
        return new Controller(repo);
    }

    public static void main(String[] args) {
        IStmt ex1 = new ComptStmt(new VarDeclStmt("v", new IntType()),
                new ComptStmt(new AssignStmt("v", new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v"))));

        IStmt ex2 = new ComptStmt(new VarDeclStmt("a", new IntType()),
                new ComptStmt(new VarDeclStmt("b", new IntType()),
                        new ComptStmt(new AssignStmt("a", new ArithExp('+', new ValueExp(new IntValue(2)),
                                new ArithExp('*', new ValueExp(new IntValue(3)), new ValueExp(new IntValue(5))))),
                                new ComptStmt(new AssignStmt("b", new ArithExp('+', new VarExp("a"), new ValueExp(new IntValue(1)))),
                                        new PrintStmt(new VarExp("b"))))));

        IStmt ex3 = new ComptStmt(new VarDeclStmt("a", new BoolType()),
                new ComptStmt(new VarDeclStmt("v", new IntType()),
                        new ComptStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))),
                                new ComptStmt(new IfStmt(new VarExp("a"), new AssignStmt("v", new ValueExp(new IntValue(2))),
                                        new AssignStmt("v", new ValueExp(new IntValue(3)))),
                                        new PrintStmt(new VarExp("v"))))));

        IStmt ex4 = new ComptStmt(new VarDeclStmt("varf", new StringType()),
                new ComptStmt(new AssignStmt("varf", new ValueExp(new StringValue("test.in"))),
                        new ComptStmt(new OpenRFileStmt(new VarExp("varf")),
                                new ComptStmt(new VarDeclStmt("varc", new IntType()),

                                        new ComptStmt(new ReadFileStmt(new VarExp("varf"),"varc"),
                                                new ComptStmt(new PrintStmt(new VarExp("varc")),
                                                        new ComptStmt(new ReadFileStmt(new VarExp("varf"), "varc"),
                                                                new ComptStmt(new PrintStmt(new VarExp("varc")),
                                                                        new CloseRFileStmt(new VarExp("varf"))))))))));

        IStmt ex5 = new ComptStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new ComptStmt(new NewStmt("v", new ValueExp(new IntValue(20))),
                        new ComptStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new ComptStmt(new NewStmt("a", new VarExp("v")),
                                        new ComptStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new VarExp("a")))))));

        IStmt ex6 = new ComptStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new ComptStmt(new NewStmt("v", new ValueExp(new IntValue(20))),
                        new ComptStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new ComptStmt(new NewStmt("a", new VarExp("v")),
                                        new ComptStmt(new NewStmt("v", new ValueExp(new IntValue(30))), new PrintStmt(new ReadHeapExp(new ReadHeapExp(new VarExp("a")))))))));

        IStmt ex7 = new ComptStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new ComptStmt(new NewStmt("v", new ValueExp(new IntValue(20))),
                        new ComptStmt(new PrintStmt(new ReadHeapExp(new VarExp("v"))),
                                new ComptStmt(new WriteHeapStmt("v", new ValueExp(new IntValue(30))), new PrintStmt(new ArithExp('+', new ReadHeapExp(new VarExp("v")), new ValueExp(new IntValue(5))))))));

        IStmt ex8 = new ComptStmt(new VarDeclStmt("v1", new RefType(new IntType())),
                new ComptStmt(new NewStmt("v1", new ValueExp(new IntValue(10))),
                        new ComptStmt(new PrintStmt(new ReadHeapExp(new VarExp("v1"))),
                                new ComptStmt(new VarDeclStmt("v2", new RefType(new IntType())),
                                        new ComptStmt(new NewStmt("v2", new ValueExp(new IntValue(20))),
                                                new ComptStmt(new PrintStmt(new ReadHeapExp(new VarExp("v2"))),
                                                        new ComptStmt(new VarDeclStmt("v3", new RefType(new IntType())),
                                                                new ComptStmt(new AssignStmt("v1", new VarExp("v2")),
                                                                        new ComptStmt(new NewStmt("v3", new ValueExp(new IntValue(30))), new PrintStmt(new ReadHeapExp(new VarExp("v3"))))))))))));

        IStmt ex9 = new ComptStmt(new VarDeclStmt("v", new IntType()),
                new ComptStmt(new VarDeclStmt("a", new RefType(new IntType())),
                        new ComptStmt(new AssignStmt("v", new ValueExp(new IntValue(10))),
                                new ComptStmt(new NewStmt("a", new ValueExp(new IntValue(22))),
                                        new ComptStmt(new ForkStmt(new ComptStmt(new WriteHeapStmt("a", new ValueExp(new IntValue(30))),
                                                new ComptStmt(new AssignStmt("v", new ValueExp(new IntValue(32))),
                                                        new ComptStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new ReadHeapExp(new VarExp("a"))))))),
                                                new ComptStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new ReadHeapExp(new VarExp("a")))))))));

        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));
        menu.addCommand(new RunExample("1", ex1.toString(), createController(ex1, "log1.txt")));
        menu.addCommand(new RunExample("2", ex2.toString(), createController(ex2, "log2.txt")));
        menu.addCommand(new RunExample("3", ex3.toString(), createController(ex3, "log3.txt")));
        menu.addCommand(new RunExample("4", ex4.toString(), createController(ex4, "log4.txt")));
        menu.addCommand(new RunExample("5", ex5.toString(), createController(ex5, "log5.txt")));
        menu.addCommand(new RunExample("6", ex6.toString(), createController(ex6, "log6.txt")));
        menu.addCommand(new RunExample("7", ex7.toString(), createController(ex7, "log7.txt")));
        menu.addCommand(new RunExample("8", ex8.toString(), createController(ex8, "log8.txt")));
        menu.addCommand(new RunExample("9", ex9.toString(), createController(ex9, "log9.txt")));
        menu.show();
    }
}