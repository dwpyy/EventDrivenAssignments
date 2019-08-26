import javax.swing.*;
import java.text.DecimalFormat;

public class SimpleReactionController implements Controller{
    private JLabel jLabel;
    private JButton addCoinBtn;
    private JButton startOrStopBtn;

    private static final String STR_INSERT_COIN = "Insert coin";
    private static final String STR_PRESS_GO = "Press GO!";
    private static final String STR_WAITING = "Waiting...";
    private static final String STR_PLAYING = "Playing...";

    private static final String STR_DOUBLE_FORMAT = "#0.00";

    /**
     * random waiting time
     */
    private int milliSeconds;

    //time
    private long tickWaitingTime;
    private long tickPlayingTime;
    private long tickStoppingTime;

    /**
     * different states
     */
    private static final int STATE_INSERT_COIN = 0;
    private static final int STATE_PRESS_GO = 1;
    private static final int STATE_WAITING = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_STOP = 4;

    private static int currentState = -1;

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
        }else if(currentState == STATE_WAITING){
            //if the state is waiting, change to insert coin
            setState(STATE_INSERT_COIN);
        }else if(currentState == STATE_PLAYING){
            //if the state is playing, change to stop
            setState(STATE_STOP);
        }
    }

    @Override
    public void tick() {
        if (currentState == STATE_WAITING){
            tickWaitingTime += 10;
            if(tickWaitingTime < milliSeconds){
                jLabel.setText(STR_WAITING);
            }else{
                setState(STATE_PLAYING);
            }
        }else if(currentState == STATE_PLAYING){
            tickPlayingTime += 10;
            if(tickPlayingTime <= 2000) {
                jLabel.setText(STR_PLAYING
                        + new DecimalFormat(STR_DOUBLE_FORMAT)
                                    .format((double)tickPlayingTime/1000) + "s");
            }else{
                setState(STATE_STOP);
            }
        } else if(currentState == STATE_STOP){
            tickStoppingTime += 10;
            if(tickStoppingTime <= 3000) {
//                jLabel.setText("Game Over!" + tickPlayingTime);
            }else{
                setState(STATE_INSERT_COIN);
            }
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
                tickWaitingTime = 0l;
                tickPlayingTime = 0l;
                tickStoppingTime = 0l;
                jLabel.setText(STR_INSERT_COIN);
                setInsertCoinBtnEnable();
                break;
            case STATE_PRESS_GO:
                jLabel.setText(STR_PRESS_GO);
                setStartBtnEnable();
                break;
            case STATE_WAITING:
                jLabel.setText(STR_WAITING);
                setStartBtnEnable();
                break;
            case STATE_PLAYING:
                setStartBtnEnable();
                break;
            case STATE_STOP:
                setInsertCoinBtnEnable();
                break;
            default:break;
        }
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

