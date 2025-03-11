package view;

import view.commands.Command;
import view.commands.RunExample;

import java.util.*;
import java.util.List;

public class TextMenu {
        private Map<String, Command> commands;
        List<Command> executedCms = new ArrayList<>();

        public TextMenu() {
            commands = new HashMap<>();
        }

        public void addCommand(Command c) {
            commands.put(c.getKey(), c);
        }

        private void printMenu() {
            for (Command com : commands.values()) {
                String line = String.format("%4s : %s", com.getKey(), com.getDescription());
                System.out.println(line);
            }
        }

        public void show() {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                printMenu();
                System.out.print("Enter your choice: ");
                String key = scanner.nextLine();
                Command com = commands.get(key);
                if (com == null) {
                    System.out.println("Invalid choice");
                    continue;
                }
                if (executedCms.contains(com) && com.getClass() == RunExample.class)
                    System.out.println("The example has already been run!");
                else {
                    com.execute();
                    executedCms.add(com);
                }
            }
        }
    }
