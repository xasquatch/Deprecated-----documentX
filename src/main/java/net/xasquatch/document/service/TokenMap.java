package net.xasquatch.document.service;

import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class TokenMap {

    private Map<String, Object> map = new HashMap<String, Object>();

    /**
     * @param token: 해당 토큰 파라미터를 키값으로 map에 삽입
     *               value는 5분후의 Calender인스턴스
     *               5분이 경과하면 해당 키의 밸류 값이 삭제되도록 구현
     *               (5분이 경과하면 재인증해야된다는 말)
     */
    public void addToken(String token) {

        //TODO: 이메일 파싱하여 기존 것 제거, 나중에 다시 조치 필요
        for (String emailContainedString : map.keySet()) {
            if (emailContainedString.indexOf(token.substring(0, token.length() - 7)) == 0)
                map.remove(emailContainedString);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 5);
        map.put(token, calendar);

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000 * 60 * 5);
                map.remove(token);
            } catch (InterruptedException e) {
                log.warn("TokenMap sleep Exception: {}", e.getMessage());
            } finally {
                Thread.interrupted();
            }
        });
        thread.setName(token);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * @param threadname 해당 쓰레드 이름
     * @return 쓰레드 전체 조회 후 해당 쓰레드를 인터럽트 완료하면 true 리턴
     */
    public boolean removeThread(String threadname) {
        boolean result = false;
        Thread currentThread = Thread.currentThread();
        ThreadGroup threadGroup = currentThread.getThreadGroup();
        int activeCount = threadGroup.activeCount();
        Thread[] activeThreads = new Thread[activeCount];
        threadGroup.enumerate(activeThreads);

        for (Thread activeThread : activeThreads) {
            if (activeThread.getName().equals(threadname)) {
                activeThread.interrupt();
                result = true;
            }
        }

        return result;
    }

    /**
     * @param token: 실제로 email + token형식으로 token이 파파미터로 쓰임
     *               예시) test@gmail.com854563
     *               해당 토큰 5분제한 쓰레드 제거
     *               토큰 인증이 완료되면 "success"로 값을 변경
     *               그 후 쓰레드를 생성하여 한 시간 후에 해당 토큰이 삭제되도록 구현
     *               (그 안에 회원가입을 완료처리 해야됨)
     * @return 인증 결과값
     */
    public boolean confirmToken(String token) throws Exception {
        Calendar date = (Calendar) map.get(token);
        boolean result = false;
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(new Date());

        //인증완료
        if (date.after(currentCalendar)) {
            boolean removeResult = removeThread(token);
            if (!removeResult) throw new Exception("Occurred Exception RemoveThread");

            map.put(token, "success");
            result = true;

            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(1000 * 60 * 60);
                    map.remove(token);
                } catch (InterruptedException e) {
                    log.error("token 인증 완료 에러");
                }

            });
            thread.setDaemon(true);
            thread.start();

        }


        return result;
    }

    /**
     * @param email: 확인할 이메일 값
     *               map의 ketset에서 이메일이 포함되는 토큰이 있는지 문자열 검사 후
     *               맞다면 그 key의 값이 success처리되었는지 확인하여
     *               결과값 리턴
     * @return
     */
    public boolean isConfirmedTokenAuthorization(String email) {
        boolean result = false;
        for (String emailContainedString : map.keySet())
            if (emailContainedString.indexOf(email) == 0) {
                String successString = map.get(emailContainedString).toString();
                return successString.equals("success") ? true : false;
            }

        return result;
    }

}