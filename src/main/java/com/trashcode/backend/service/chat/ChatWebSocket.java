package com.trashcode.backend.service.chat;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebSocket
public class ChatWebSocket {

    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocket.class);

    @OnWebSocketConnect
    public void onConnect(Session session) {
        logger.info("New user connected");
        String user = "User: " + UserIdGenerator.getNewUserID();
        Chat.users.put(session, user);
        Chat.broadcast("Server", user + " joined the chat");
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        logger.info("User left chat");
        String username = Chat.users.get(user);
        Chat.users.remove(user);
        Chat.broadcast("Server", (username + " left the chat"));
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        logger.info("Broadcast message");
        Chat.broadcast(Chat.users.get(user), message);
    }
}
