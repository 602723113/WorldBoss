package cc.zoyn.worldboss.model;

import java.util.List;

public class Kit {

    private List<String> commands;
    private String message;

    public Kit(List<String> commands, String message) {
        this.commands = commands;
        this.message = message;
    }

    public List<String> getCommands() {
        return commands;
    }


    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Kit{" +
                "commands=" + commands +
                ", message='" + message + '\'' +
                '}';
    }
}
