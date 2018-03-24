package com.trashcode.backend.service.chat;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Chat {

    private final Logger logger = LoggerFactory.getLogger(Chat.class);
    private Map<Session, String> users = new ConcurrentHashMap<>();
    private Queue<String> messages = new ConcurrentLinkedQueue<>();

    public synchronized void broadcast(String sender, String message) {
        logger.info("Broadcasting from sender: " + sender + ", message: " + message);
        sendAll(sender, message);
    }

    private void sendAll(String sender, String message) {
        users.keySet()
                .stream()
                .filter(Session::isOpen)
                .forEach(session -> {
                    try {
                        sendMessage(sender, message, session);
                    } catch (IOException e) {
                        logger.error("Error occurred while trying to broadcast message: "
                                + message, e);
                    }
                });
    }

    private void sendMessage(String sender, String message, Session session) throws IOException {
        session.getRemote().sendString(createJson(sender, message));
    }

    private String createJson(String sender, String message) {
        logger.info("Creating JSON object");
        String object = new JSONObject().put("sender", sender)
                .put("userMessage", message)
                .put("userList", users.values())
                .toString();
        logger.info("Object: " + object);
        return object;
    }

    public synchronized void addUser(Session session, String name) {
        logger.info("Adding user: " + name);
        users.put(session, name);
    }

    public synchronized void removeUser(Session session) {
        String userName = users.get(session);
        logger.info("Removing user: " + userName);
        users.remove(session);
        broadcast("Server", "User: " + userName + " left channel");
    }

    public synchronized String getUser(Session session) {
        return users.get(session);
    }

    public synchronized void registerMessage(String message) {
        messages.add(message);
    }

}
