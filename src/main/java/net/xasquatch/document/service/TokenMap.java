package net.xasquatch.document.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenMap implements Runnable {

    static Map<String, Calendar> map;

    @Override
    public void run() {
        map = new HashMap<>();
    }

    public static void addToken(String token) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 5);
        map.put(token, calendar);
    }

    public static boolean confirmToken(String token) {
        Calendar date = map.get(token);
        boolean result = false;
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(new Date());
        if (date.after(currentCalendar))
            //인증완료
            result = true;

        map.remove(token);

        return result;
    }
}