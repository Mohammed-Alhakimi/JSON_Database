package client;

public class SetCommand {
    private final String type="set";
    private final String key;
    private final String value;

    public SetCommand(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
