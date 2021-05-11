package net.xasquatch.document.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenMap {

    private Map<String, Calendar> map = new HashMap<String, Calendar>();

    public void addToken(String token) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 5);
        map.put(token, calendar);
    }

    public boolean confirmToken(String token) {
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