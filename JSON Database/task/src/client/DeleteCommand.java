package client;

public class DeleteCommand {
    private final String type = "delete";
    private final String key;

    public DeleteCommand(String key) {
        this.key = key;
    }
}
