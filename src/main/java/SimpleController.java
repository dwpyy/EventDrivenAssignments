import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;
import java.util.Timer;

public class SimpleController {
    private JButton addCoinBtn;
    private JButton startOrStopBtn;

    private JLabel jLabel;

    public static void main(String[] args) {
        SimpleController sc = new SimpleController();
        sc.initUI();
    }

    public void initUI() {
        JFrame jf = new JFrame();
        jf.setTitle("Simple Controller");
        jf.setSize(400, 300);
        jf.setResizable(true);
        jf.setDefaultCloseOperation(3);
        jf.setLocationRelativeTo(null);

        //给jf添加组件
        jLabel = new JLabel("Insert coin");
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setVerticalAlignment(SwingConstants.CENTER);
        jf.add(jLabel, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        addCoinBtn = new javax.swing.JButton("Insert Coin");
        startOrStopBtn = new javax.swing.JButton("Go/Stop");

        setInsertCoinBtnEnable();
        addCoinBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setState(STATE_PRESS_GO);
            }
        });
        startOrStopBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        });

        bottom.add(addCoinBtn);
        bottom.add(startOrStopBtn);
        jf.add(bottom, BorderLayout.SOUTH);//用BorderLayout布局管理器，放在最底层

        jf.setVisible(true);
    }

    /**
     * set different operation for different state
     * @param state
     */
    private void setState(int state){
        currentState = state;
        switch (state){
            case STATE_INSERT_COIN:
                jLabel.setText("Insert coin");
                setInsertCoinBtnEnable();
                stopWaitingTimer();
                stopPlayingTimer();
                break;
            case STATE_PRESS_GO:
                jLabel.setText("Press Go!");
                setStartBtnEnable();
                break;
            case STATE_WAITING:
                jLabel.setText("Waiting...");
                setStartBtnEnable();
                setWaitingTimer();
                break;
            case STATE_PLAYING:
                stopWaitingTimer();
                setStartBtnEnable();
                setPlayingTimer();
                break;
            case STATE_STOP:
                stopPlayingTimer();
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

    /**
     * different states
     */
    public static final int STATE_INSERT_COIN = 0;
    public static final int STATE_PRESS_GO = 1;
    public static final int STATE_WAITING = 2;
    public static final int STATE_PLAYING = 3;
    public static final int STATE_STOP = 4;

    public static int currentState = -1;


    private Timer mWaitingTimer;
    public void setWaitingTimer(){
        long randomTime = (long)(1 + Math.random() * 1.5) * 1000l;
        mWaitingTimer = new Timer();
        mWaitingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                setState(STATE_PLAYING);
            }
        }, randomTime, 100l);
    }

    public void stopWaitingTimer(){
        if(mWaitingTimer != null){
            mWaitingTimer.cancel();
            mWaitingTimer = null;
        }
    }

    private Timer mPlayingTimer;
    private long mPlayingTime = 0l;
    public void setPlayingTimer(){
        currentState = STATE_PLAYING;
        mPlayingTime = 0l;
        mPlayingTimer = new java.util.Timer();
        mPlayingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mPlayingTime += 10l;
                if(mPlayingTime < 2000l){
                    jLabel.setText(mPlayingTime + " millisecond");
                }else{
                    jLabel.setText(mPlayingTime + " millisecond, "+ "game over! Please insert coin");
                    stopPlayingTimer();
                    setInsertCoinBtnEnable();
                }
            }
        }, 0l, 10l);
    }

    public void stopPlayingTimer(){
        if(mPlayingTimer != null){
            mPlayingTimer.cancel();
            mPlayingTimer = null;
        }
    }
}