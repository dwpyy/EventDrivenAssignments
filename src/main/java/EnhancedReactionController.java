import javax.swing.*;
import java.text.DecimalFormat;

public class EnhancedReactionController implements Controller {
    /**
     * screen
     */
    private JLabel jLabel;
    /**
     * insert coin btn
     */
    private JButton addCoinBtn;
    /**
     * go/stop btn
     */
    private JButton startOrStopBtn;
    /**
     * random waiting time
     */
    private int milliSeconds;

    //time
    private long tickInsertTime;
    private long tickWaitingTime;
    private long tickPlayingTime;
    private long tickStoppingTime;
    private long tickAverageTime;

    private long realPlayTime1 = 0;
    private long realPlayTime2 = 0;
    private long realPlayTime3 = 0;

    /**
     * different states
     */
    private static final int STATE_INSERT_COIN = 0;
    private static final int STATE_PRESS_GO = 1;
    private static final int STATE_WAITING = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_STOP = 4;
    private static final int STATE_AVERAGE = 5;


    private static int currentState = -1;
    private static int currentGameNum = 1;//current game

    /**
     * string for show
     */
    private static final String STR_INSERT_COIN = "Insert coin";
    private static final String STR_PRESS_GO = "Press GO!";
    private static final String STR_WAITING = "Waiting...";
    private static final String STR_PLAYING = "Playing...";
    private static final String STR_AVERAGE = "Average = ";

    /**
     * double format
     */
    private static final String STR_DOUBLE_FORMAT = "#0.00";

    @Override
    public void connect(Gui gui, Random rng) {
        Display mGui = (Display) gui;
        milliSeconds = rng.getRandom(1000, 2500);
        jLabel = mGui.getjLabel();
        addCoinBtn =  mGui.getAddCoinBtn();
        startOrStopBtn =  ((Display)gui).getStartOrStopBtn();
    }

    @Override
    public void init() {
        setState(STATE_INSERT_COIN);
    }


    @Override
    public void coinInserted() {
        setState(STATE_PRESS_GO);
    }

    @Override
    public void goStopPressed() {
        if(currentState == STATE_PRESS_GO){
            setState(STATE_WAITING);
        }else if(currentState == STATE_WAITING || currentState == STATE_AVERAGE){
            //if the state is waiting, change to insert coin
            setState(STATE_INSERT_COIN);
        }else if(currentState == STATE_PLAYING){
            if(currentGameNum <= 3){
                setState(STATE_STOP);
            }
        }else if(currentState == STATE_STOP){
            currentGameNum ++;
            if(currentGameNum <= 3){
                setState(STATE_WAITING);
            }else{
                setState(STATE_AVERAGE);
            }
        }
    }

    @Override
    public void tick() {
        if (currentState == STATE_PRESS_GO){
            tickInsertTime += 10;
            if(tickInsertTime > 10 * 1000){
                setState(STATE_INSERT_COIN);
            }
        }else if (currentState == STATE_WAITING){
            tickWaitingTime += 10;
            if(tickWaitingTime < milliSeconds){
                jLabel.setText(STR_WAITING);
            }else{
                setState(STATE_PLAYING);
            }
        }else if(currentState == STATE_PLAYING){
            tickPlayingTime += 10;
            if(tickPlayingTime <= 2 * 1000) {
                jLabel.setText(STR_PLAYING
                        + new DecimalFormat(STR_DOUBLE_FORMAT)
                        .format((double)tickPlayingTime/1000) + "s");

                getPlayTime();
            }else{
                setState(STATE_STOP);
            }
        } else if(currentState == STATE_STOP) {

            tickStoppingTime += 10;
            if(tickStoppingTime > 3 * 1000){
                currentGameNum ++;
                tickWaitingTime = 0;
                tickPlayingTime = 0;
                tickStoppingTime = 0;
                if(currentGameNum <= 3){
                    setState(STATE_WAITING);
                }else{
                    setState(STATE_AVERAGE);
                }
            }
        }else if(currentState == STATE_AVERAGE){
            tickAverageTime += 10;
            if(tickAverageTime <= 5 * 1000) {
                long realPlayTime = realPlayTime1 + realPlayTime2 + realPlayTime3;
                jLabel.setText(STR_AVERAGE
                        + new DecimalFormat(STR_DOUBLE_FORMAT)
                        .format((double)realPlayTime/1000) + "s");
            }else{
                setState(STATE_INSERT_COIN);
            }
        }

    }

    /**
     * get three game time
     */
    private void getPlayTime(){
        switch (currentGameNum){
            case 1:
                realPlayTime1 = tickPlayingTime;
                break;
            case 2:
                realPlayTime2 = tickPlayingTime;
                break;
            case 3:
                realPlayTime3 = tickPlayingTime;
                break;
        }
    }

    /**
     * set different operation for different state
     * @param state
     */
    private void setState(int state){
        currentState = state;
        switch (state){
            case STATE_INSERT_COIN:
                initTimeData();
                jLabel.setText(STR_INSERT_COIN);
                setInsertCoinBtnEnable();
                break;
            case STATE_PRESS_GO:
                jLabel.setText(STR_PRESS_GO);
                setStartBtnEnable();
                break;
            case STATE_WAITING:
                tickWaitingTime = 0;
                tickStoppingTime = 0;
                tickPlayingTime = 0;
                jLabel.setText(STR_WAITING);
                setStartBtnEnable();
                break;
            case STATE_PLAYING:
            case STATE_STOP:
            case STATE_AVERAGE:
                setStartBtnEnable();
                break;
            default:break;
        }
    }

    /**
     * initial time
     */
    private void initTimeData(){
        tickInsertTime = 0l;
        tickWaitingTime = 0l;
        tickPlayingTime = 0l;
        tickStoppingTime = 0l;
        tickAverageTime = 0l;
        currentGameNum = 1;
        realPlayTime1 = 0;
        realPlayTime2 = 0;
        realPlayTime3 = 0;
    }

    /**
     * startOrStopBtn is available to use
     */
    private void setStartBtnEnable(){
        addCoinBtn.setEnabled(false);
        startOrStopBtn.setEnabled(true);
    }

    /**
     * addCoinBtn is available to use
     */
    private void setInsertCoinBtnEnable(){
        addCoinBtn.setEnabled(true);
        startOrStopBtn.setEnabled(false);
    }
}