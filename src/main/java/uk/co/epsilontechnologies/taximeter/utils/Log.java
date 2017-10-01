package uk.co.epsilontechnologies.taximeter.utils;

import org.joda.time.DateTime;

public class Log {

    public static void info(String message) {
        log(message);
    }

    public static void exception(Throwable ex) {
        log(ex.getMessage());
        for (StackTraceElement element : ex.getStackTrace()) {
            log(element.toString());
        }
    }

    private static void log(String message) {
        System.out.println((new DateTime())+":T"+Thread.currentThread().getId()+":"+message);
    }
}
