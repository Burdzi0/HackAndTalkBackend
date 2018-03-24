package com.trashcode.backend.service.chat;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Chat {

    private static final Logger logger = LoggerFactory.getLogger(Chat.class);

    static Map<Session, String> users = new ConcurrentHashMap<>();

    public static synchronized void broadcast(String sender, String message) {
        logger.info("Broadcasting from sender: " + sender + ", message: " + message);
        users.keySet().stream()
                .filter(Session::isOpen)
                .forEach(session -> {
                    try {
                        session.getRemote().sendString(createJson(sender, message));
                    } catch (IOException e) {
                        logger.error("Error occurred while trying to broadcast message: "
                                + message, e);
                    }
                });
    }

    private static String createJson(String sender, String message) {
        logger.info("Creating JSON object");
        return new JSONObject().put("sender", sender)
        .put("userMessage", message)
        .put("userList", users.values())
        .toString();
    }

}
