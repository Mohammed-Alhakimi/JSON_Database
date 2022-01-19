package client;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    private static final String address = "127.0.0.1";
    private static final int PORT = 23456;

    @Parameter(names = {"-t", "-type"}, description = "Command type")
    private String type;
    @Parameter(names = {"-k", "-key"}, description = "Key in remote database")
    private String key;
    @Parameter(names = {"-v", "-value"}, description = "value to set")
    private String value;
    @Parameter(names = {"-in", "-file"}, description = "file path to read")
    private String fileName;

    public static void main(String[] args) {
        System.out.println("Client started!");
        Main main = new Main();
        JCommander
                .newBuilder()
                .addObject(main)
                .build()
                .parse(args);

        try (Socket socket = new Socket(InetAddress.getByName(address), PORT);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            String type = main.getType();
            String key = main.getKey();
            String fileName = main.getFilename();
            String commandSent = null;

            boolean readFromFile = fileName != null;

            if (readFromFile) {
                commandSent = Files.readString(Path.of("./src/client/data/" + fileName));
                output.writeUTF(commandSent);
            } else {
                if (type.equals("exit")) {
                    commandSent = new Gson().toJson(new ExitRequest());
                    output.writeUTF(commandSent);
                } else {
                    switch (type) {
                        case "get":
                            commandSent = new Gson().toJson(new GetCommand(key));
                            output.writeUTF(new Gson().toJson(new GetCommand(key)));
                            break;
                        case "delete":
                            commandSent = new Gson().toJson(new DeleteCommand(key));
                            output.writeUTF(commandSent);
                            break;
                        case "set":
                            String value = main.getValueToSet();
                            commandSent = new Gson().toJson(new SetCommand(key, value));
                            output.writeUTF(commandSent);
                            break;
                    }
                }
            }
            if (commandSent != null) {
                System.out.println("Sent: " + commandSent);
                System.out.println("Received: " + input.readUTF());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getValueToSet() {
        return value;
    }

    public String getFilename() {
        return fileName;
    }
}
