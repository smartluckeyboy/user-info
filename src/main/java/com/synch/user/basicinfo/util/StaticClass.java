/**
 *
 */
package com.synch.user.basicinfo.util;

public class StaticClass {

    static private ThreadLocal<Long> threadLocalUserId = new ThreadLocal<>();

    public static ThreadLocal<Long> getThreadLocalUserId() {
        return threadLocalUserId;
    }


}
