package view.commands;

import controller.Controller;
import exceptions.*;

import java.io.IOException;

public class RunExample extends Command {
    private Controller controller;

    public RunExample(String key, String description, Controller controller) {
        super(key, description);
        this.controller = controller;
    }

    @Override
    public void execute() {
        try {
            controller.allStep();
        }
        catch (PrgStateException | ADTException | ExpressionException | StatementException | ControllerException e) {
            System.out.println(e.getMessage());
        }
    }
}
