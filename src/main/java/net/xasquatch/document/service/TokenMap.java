package net.xasquatch.document.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class TokenMap {

    private Map<String, Object> map = new HashMap<String, Object>();

    public void addToken(String token) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 5);
        map.put(token, calendar);
    }

    /**
     * @param token: 실제로 email + token형식으로 token이 파파미터로 쓰임
     *             예시) test@gmail.com854563
     *             토큰 인증이 완료되면 "success"로 값을 변경
     *             그 후 쓰레드를 생성하여 한 시간 후에 해당 토큰이 삭제되도록 구현
     *             (그 안에 회원가입을 완료처리 해야됨)
     * @return 인증 결과값
     */
    public boolean confirmToken(String token) {
        Calendar date = (Calendar) map.get(token);
        boolean result = false;
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(new Date());
        if (date.after(currentCalendar))
            //인증완료
            map.put(token, "success");
        result = true;

        new Thread(() -> {
            Thread subThread = Thread.currentThread();
            try {
                subThread.wait(60 * 60);
                map.remove(token);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();

        return result;
    }

    /**
     *
     * @param email: 확인할 이메일 값
     *              map의 ketset에서 이메일이 포함되는 토큰이 있는지 문자열 검사 후
     *              맞다면 그 key의 값이 success처리되었는지 확인하여
     *             결과값 리턴
     * @return
     */
    public boolean isConfirmedTokenAuthorization(String email) {
        boolean result = false;
        for (String emailContainedString : map.keySet())
            if (emailContainedString.indexOf(email) == 0) {
                String successString = (String) map.get(emailContainedString);
                return successString == "success" ? true : false;
            }

        return result;
    }

}