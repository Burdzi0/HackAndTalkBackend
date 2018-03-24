package com.trashcode.backend.service.chat;

import java.util.concurrent.atomic.AtomicInteger;

public class UserIdGenerator {

    private static AtomicInteger usersCount = new AtomicInteger(0);

    public static synchronized int getNewUserID() {
        return usersCount.addAndGet(1);
    }

    public static synchronized int decrement() {
        return usersCount.decrementAndGet();
    }
}
