package es.upm.miw.SolitarioCelta.models;

/**
 * Created by nuonuo-jtl on 16/10/31.
 */
public class GameTimer {
    private long startTimeMillis;
    private long pauseTimeMillis;
    public void gameTimerStart(){
        startTimeMillis = System.currentTimeMillis();
    }
    public String getTimerString(){
        String format = "%d%d:%d%d.%d%d";
        long time = System.currentTimeMillis() - startTimeMillis;
        int min = (int)(time/60000);
        int sec = (int)((time - min*60000)/1000);
        int hun = (int)((time - min*60000 - sec*1000)/10);
        return String.format(format,min/10,min%10,sec/10,sec%10,hun/10,hun%10);
    }

    private long setTimer(String s){
        int minA = Integer.parseInt(s.charAt(0) + "");
        int minB = Integer.parseInt(s.charAt(1) + "");
        int secA = Integer.parseInt(s.charAt(3) + "");
        int secB = Integer.parseInt(s.charAt(4) + "");
        int hunA = Integer.parseInt(s.charAt(6) + "");
        int hunB = Integer.parseInt(s.charAt(7) + "");
        long millis = minA*600000+minB*60000+secA*10000+secB*1000+hunA*100+hunB*10;
        return millis;
    }
    public void gameTimerRestartFromTimer(String s){
        long millis = setTimer(s);
        startTimeMillis = System.currentTimeMillis() -  millis;
    }

    public void gameTimerStop(){
        pauseTimeMillis = System.currentTimeMillis();
    }

    public void gameTimerRestart(){
        long nowTimeMillis = System.currentTimeMillis();
        startTimeMillis = nowTimeMillis - (pauseTimeMillis-startTimeMillis);
    }

}
