package server;

import com.google.gson.Gson;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final String address = "127.0.0.1";
    private static final int PORT = 23456;
    private volatile static TreeMap<String, String> database = new TreeMap<>();
    private static final File dbFile = new File("./src/server/data/db.json");
    private static ReadWriteLock lock = new ReentrantReadWriteLock();
    private static final Lock writeLock = lock.writeLock();
    private static final Pattern getOrDeletePattern = Pattern.compile("^\\{\"type\":\"(.*)\",\"key\":\"(.*)\"\\}$");
    private static final Pattern setPattern = Pattern.compile("^\\{\"type\":\"(.*)\",\"key\":\"(\\w+)\",\"value\":\"(.*)\"\\}$");
    private static final String exitCommand = "{\"type\":\"exit\"}";


    public static void main(String[] args) {
        System.out.println("Server started!");
        ExecutorService executor = Executors.newScheduledThreadPool(4);

        executor.submit(() -> {
            while (true) {
                try (ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(address))) {
                    try (Socket socket = server.accept();
                         DataInputStream input = new DataInputStream(socket.getInputStream());
                         DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
                        processRequest(input.readUTF(), output);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        executor.shutdown();
    }

    private static void processRequest(String readUTF, DataOutputStream output) throws IOException {
        Matcher matcherGetDelete = getOrDeletePattern.matcher(readUTF);
        Matcher setMatcher = setPattern.matcher(readUTF);
        String command;
        String key;
        if (readUTF.equals(exitCommand)) {
            try {
                output.writeUTF(new Gson().toJson(new SuccessResponse()));
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        while (matcherGetDelete.find()) {
            command = matcherGetDelete.group(1);
            key = matcherGetDelete.group(2);
            if (command.equals("get")) {
                if (!database.containsKey(key)) {
                    output.writeUTF(new Gson().toJson(new FailedGetOrDelete()));
                } else {
                    output.writeUTF(new Gson().toJson(new SuccessfulGetOrDelete(database.get(key))));
                }
            } else if (command.equals("delete")) {
                if (!database.containsKey(key)) {
                    output.writeUTF(new Gson().toJson(new FailedGetOrDelete()));
                } else {
                    database.remove(key);
                    updateDatabaseFile(database, dbFile);
                    output.writeUTF(new Gson().toJson(new SuccessResponse()));
                }
            }
        }
        while (setMatcher.find()) {
            command = setMatcher.group(1);
            key = setMatcher.group(2);
            String valueToSet = setMatcher.group(3);
            if (command.equals("set")) {
                if (!database.containsKey(key)) {
                    database.put(key, valueToSet);
                } else {
                    database.replace(key, database.get(key), valueToSet);
                }
                updateDatabaseFile(database, dbFile);
                output.writeUTF(new Gson().toJson(new SuccessResponse()));
            }
        }
    }

    private synchronized static void updateDatabaseFile(TreeMap<String, String> database, File file) {
        writeLock.lock();
        try (FileWriter writer = new FileWriter(file);) {
            writer.write(new Gson().toJson(database));
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeLock.unlock();
    }

}
