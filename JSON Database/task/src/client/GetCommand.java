package client;

public class GetCommand {
     final String type="get";
     final String key;

    public GetCommand(String key) {
        this.key = key;
    }
}
