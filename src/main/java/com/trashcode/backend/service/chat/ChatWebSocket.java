package com.trashcode.backend.service.chat;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@WebSocket
public class ChatWebSocket {

    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocket.class);
    private Chat chat;

    public ChatWebSocket(Chat chat) {
        this.chat = chat;
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        logger.info("New user connected");
        String user = "User " + UserIdGenerator.getNewUserID();
        chat.addUser(session, user);
        logger.info("Added user to users map");
        // chat.broadcast("Server", user + " joined the chat");
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        chat.removeUser(user);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        logger.info("Broadcast message: " + message);
        String sender = chat.getUser(user);

        if (isJSONValid(message)) {
            String name = (String) new JSONObject(message).get("name");
            chat.changeName(user, name);
            sender = "Server";
            message = name + " joined the chat";
        }
        chat.registerMessage(sender, message);
        chat.broadcast(sender, message);
    }

    private void setMessageToUserConnected(String message, String name, String sender) {
        message = name + " joined the chat";
        sender = "Server";
    }

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
            return true;
        } catch (JSONException ex) {
            logger.info("Not a JSON message");
        }
        return false;
    }
}
