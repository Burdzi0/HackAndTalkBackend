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
    private Chat chat;

    public ChatWebSocket(Chat chat) {
        this.chat = chat;
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        logger.info("New user connected");
        String user = "User " + UserIdGenerator.getNewUserID();
        chat.addUser(session, user);
        chat.broadcast("Server", user + " joined the chat");
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        chat.removeUser(user);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        logger.info("Broadcast message: " + message);
        chat.registerMessage(message);
        chat.broadcast(chat.getUser(user), message);
    }
}
