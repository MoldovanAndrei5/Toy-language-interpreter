package com.example.test2;

import controller.Controller;
import exceptions.StatementException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.expressions.*;
import model.programState.BarrierTable.BarrierTable;
import model.programState.FileTable.FileTable;
import model.programState.PrgState;
import model.statements.*;
import model.programState.MyDictionary.*;
import model.programState.Heap.*;
import model.programState.MyList.*;
import model.programState.MyStack.*;
import model.types.IntType;
import model.types.RefType;
import model.values.IntValue;
import repository.IRepository;
import repository.Repository;

import javax.xml.validation.Validator;
import java.util.ArrayList;
import java.util.List;

public class ProgramChooserController {
    private ProgramExecutorController programExecutorController;

    public void setProgramExecutorController(ProgramExecutorController programExecutorController) {
        this.programExecutorController = programExecutorController;
    }
    @FXML
    private ListView<IStmt> programsListView;

    @FXML
    private Button displayButton;

    @FXML
    public void initialize() {
        programsListView.setItems(getAllStatements());
        programsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @FXML
    private void displayProgram(ActionEvent actionEvent) {
        IStmt selectedStatement = programsListView.getSelectionModel().getSelectedItem();
        if (selectedStatement == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error encountered!");
            alert.setContentText("No statement selected!");
            alert.showAndWait();
        } else {
            int id = programsListView.getSelectionModel().getSelectedIndex();
            try {
                selectedStatement.typecheck(new MyDictionary<>());
                PrgState programState = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new FileTable<>(), new Heap<>(), new BarrierTable(), selectedStatement);
                List<PrgState> prgList = new ArrayList<PrgState>();
                prgList.add(programState);
                IRepository repository = new Repository(prgList, "log" + (id + 1) + ".txt");
                Controller controller = new Controller(repository);
                programExecutorController.setController(controller);
            } catch (StatementException exception) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error encountered!");
                alert.setContentText(exception.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private ObservableList<IStmt> getAllStatements() {
        List<IStmt> allStatements = new ArrayList<>();

        IStmt ex1 = new ComptStmt(new VarDeclStmt("v", new model.types.IntType()),
                new ComptStmt(new AssignStmt("v", new ValueExp(new model.values.IntValue(2))),
                        new PrintStmt(new VarExp("v"))));

        IStmt ex2 = new ComptStmt(new VarDeclStmt("a", new model.types.IntType()),
                new ComptStmt(new VarDeclStmt("b", new model.types.IntType()),
                        new ComptStmt(new AssignStmt("a", new ArithExp('+', new ValueExp(new model.values.IntValue(2)),
                                new ArithExp('*', new ValueExp(new model.values.IntValue(3)), new ValueExp(new model.values.IntValue(5))))),
                                new ComptStmt(new AssignStmt("b", new ArithExp('+', new VarExp("a"), new ValueExp(new model.values.IntValue(1)))),
                                        new PrintStmt(new VarExp("b"))))));

        IStmt ex3 = new ComptStmt(new VarDeclStmt("a", new model.types.BoolType()),
                new ComptStmt(new VarDeclStmt("v", new model.types.IntType()),
                        new ComptStmt(new AssignStmt("a", new ValueExp(new model.values.BoolValue(true))),
                                new ComptStmt(new IfStmt(new VarExp("a"), new AssignStmt("v", new ValueExp(new model.values.IntValue(2))),
                                        new AssignStmt("v", new ValueExp(new model.values.IntValue(3)))),
                                        new PrintStmt(new VarExp("v"))))));

        IStmt ex4 = new ComptStmt(new VarDeclStmt("varf", new model.types.StringType()),
                new ComptStmt(new AssignStmt("varf", new ValueExp(new model.values.StringValue("test.in"))),
                        new ComptStmt(new OpenRFileStmt(new VarExp("varf")),
                                new ComptStmt(new VarDeclStmt("varc", new model.types.IntType()),

                                        new ComptStmt(new ReadFileStmt(new VarExp("varf"),"varc"),
                                                new ComptStmt(new PrintStmt(new VarExp("varc")),
                                                        new ComptStmt(new ReadFileStmt(new VarExp("varf"), "varc"),
                                                                new ComptStmt(new PrintStmt(new VarExp("varc")),
                                                                        new CloseRFileStmt(new VarExp("varf"))))))))));

        IStmt ex5 = new ComptStmt(new VarDeclStmt("v", new model.types.RefType(new model.types.IntType())),
                new ComptStmt(new NewStmt("v", new ValueExp(new model.values.IntValue(20))),
                        new ComptStmt(new VarDeclStmt("a", new model.types.RefType(new model.types.RefType(new model.types.IntType()))),
                                new ComptStmt(new NewStmt("a", new VarExp("v")),
                                        new ComptStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new VarExp("a")))))));

        IStmt ex6 = new ComptStmt(new VarDeclStmt("v", new model.types.RefType(new model.types.IntType())),
                new ComptStmt(new NewStmt("v", new ValueExp(new model.values.IntValue(20))),
                        new ComptStmt(new VarDeclStmt("a", new model.types.RefType(new model.types.RefType(new model.types.IntType()))),
                                new ComptStmt(new NewStmt("a", new VarExp("v")),
                                        new ComptStmt(new NewStmt("v", new ValueExp(new model.values.IntValue(30))), new PrintStmt(new ReadHeapExp(new ReadHeapExp(new VarExp("a")))))))));

        IStmt ex7 = new ComptStmt(new VarDeclStmt("v", new model.types.RefType(new model.types.IntType())),
                new ComptStmt(new NewStmt("v", new ValueExp(new model.values.IntValue(20))),
                        new ComptStmt(new PrintStmt(new ReadHeapExp(new VarExp("v"))),
                                new ComptStmt(new WriteHeapStmt("v", new ValueExp(new model.values.IntValue(30))), new PrintStmt(new ArithExp('+', new ReadHeapExp(new VarExp("v")), new ValueExp(new model.values.IntValue(5))))))));

        IStmt ex8 = new ComptStmt(new VarDeclStmt("v1", new model.types.RefType(new model.types.IntType())),
                new ComptStmt(new NewStmt("v1", new ValueExp(new model.values.IntValue(10))),
                        new ComptStmt(new PrintStmt(new ReadHeapExp(new VarExp("v1"))),
                                new ComptStmt(new VarDeclStmt("v2", new model.types.RefType(new model.types.IntType())),
                                        new ComptStmt(new NewStmt("v2", new ValueExp(new model.values.IntValue(20))),
                                                new ComptStmt(new PrintStmt(new ReadHeapExp(new VarExp("v2"))),
                                                        new ComptStmt(new VarDeclStmt("v3", new model.types.RefType(new model.types.IntType())),
                                                                new ComptStmt(new AssignStmt("v1", new VarExp("v2")),
                                                                        new ComptStmt(new NewStmt("v3", new ValueExp(new model.values.IntValue(30))), new PrintStmt(new ReadHeapExp(new VarExp("v3"))))))))))));

        IStmt ex9 = new ComptStmt(new VarDeclStmt("v", new model.types.IntType()),
                new ComptStmt(new VarDeclStmt("a", new model.types.RefType(new model.types.IntType())),
                        new ComptStmt(new AssignStmt("v", new ValueExp(new model.values.IntValue(10))),
                                new ComptStmt(new NewStmt("a", new ValueExp(new model.values.IntValue(22))),
                                        new ComptStmt(new ForkStmt(new ComptStmt(new WriteHeapStmt("a", new ValueExp(new model.values.IntValue(30))),
                                                new ComptStmt(new AssignStmt("v", new ValueExp(new model.values.IntValue(32))),
                                                        new ComptStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new ReadHeapExp(new VarExp("a"))))))),
                                                new ComptStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new ReadHeapExp(new VarExp("a")))))))));

        IStmt ex10 = new ComptStmt(new VarDeclStmt("v", new IntType()),
                new ComptStmt(new VarDeclStmt("x", new IntType()),
                        new ComptStmt(new VarDeclStmt("y", new IntType()),
                                new ComptStmt(new AssignStmt("v", new ValueExp(new IntValue(0))),
                                        new ComptStmt(new RepeatUntilStmt(new ComptStmt(new ForkStmt(new ComptStmt(new PrintStmt(new VarExp("v")), new AssignStmt("v", new ArithExp('-', new VarExp("v"), new ValueExp(new IntValue(1)))))), new AssignStmt("v", new ArithExp('+', new VarExp("v"), new ValueExp(new IntValue(1))))), new RelExp(new VarExp("v"), new ValueExp(new IntValue(3)), "==")),
                                                new ComptStmt(new AssignStmt("x", new ValueExp(new IntValue(1))),
                                                        new ComptStmt(new NopStmt(),
                                                                new ComptStmt(new AssignStmt("y", new ValueExp(new IntValue(3))),
                                                                        new ComptStmt(new NopStmt(),
                                                                                new PrintStmt(new ArithExp('*', new VarExp("v"), new ValueExp(new IntValue(10)))))))))))));

        IStmt ex11 = new ComptStmt(new VarDeclStmt("v1", new RefType(new IntType())),
                new ComptStmt(new VarDeclStmt("v2", new RefType(new IntType())),
                    new ComptStmt(new VarDeclStmt("v3", new RefType(new IntType())),
                        new ComptStmt(new VarDeclStmt("cnt", new IntType()),
                                new ComptStmt(new NewStmt("v1", new ValueExp(new IntValue(2))),
                                        new ComptStmt(new NewStmt("v2", new ValueExp(new IntValue(3))),
                                                new ComptStmt(new NewStmt("v3", new ValueExp(new IntValue(4))),
                                                        new ComptStmt(new NewBarrierStmt("cnt", new ReadHeapExp(new VarExp("v2"))),
                                                                new ComptStmt(new ForkStmt(new ComptStmt(new AwaitStmt("cnt"), new WriteHeapStmt("v1", new ArithExp('*', new ReadHeapExp(new VarExp("v1")), new ValueExp(new IntValue(10)))))),
                                                                        new ComptStmt(new PrintStmt(new ReadHeapExp(new VarExp("v2"))),
                                                                                new ComptStmt(new AwaitStmt("cnt"), new PrintStmt(new ReadHeapExp(new VarExp("v3"))))))))))))));

        allStatements.add(ex1);
        allStatements.add(ex2);
        allStatements.add(ex3);
        allStatements.add(ex4);
        allStatements.add(ex5);
        allStatements.add(ex6);
        allStatements.add(ex7);
        allStatements.add(ex8);
        allStatements.add(ex9);
        allStatements.add(ex10);
        allStatements.add(ex11);
        return FXCollections.observableArrayList(allStatements);
    }
}