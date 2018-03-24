package com.trashcode.backend;

import com.trashcode.backend.config.WebAppConfig;
import com.trashcode.backend.service.chat.ChatWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;

public class Backend {

    final static Logger logger = LoggerFactory.getLogger(Backend.class);

    public static void main(String[] args) {
        final int port = 5678;
        port(port);
        logger.info("Starting web app...");
        logger.info("Registering init handler");
        registerInitHandler();

        staticFileLocation(WebAppConfig.FILES_FOLDER);

        webSocket("/chat", new ChatWebSocket());
        init();
        logger.info("Exiting web app");
    }

    private static void registerInitHandler() {
        initExceptionHandler((e) -> {
            logger.error("Caught exception on startup: ", e);
            logger.error("Exiting...");
            stop();
        });
    }
}
