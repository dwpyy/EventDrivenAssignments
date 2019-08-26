import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Display implements Gui {
	private JFrame jf;
	private JButton addCoinBtn;
	private JButton startOrStopBtn;
	private JLabel jLabel;

	private Controller mController;

	private static final String INSERT_COIN_BTN = "Insert coin";
	private static final String GO_STOP_BTN = "Go/Stop";

	public Display() {
		initUI();
	}

	public JButton getAddCoinBtn() {
		return addCoinBtn;
	}

	public JButton getStartOrStopBtn() {
		return startOrStopBtn;
	}

	public JLabel getjLabel() {
		return jLabel;
	}

	/**
	 * UI 绘制
	 */
	public void initUI(){
		jf = new JFrame();
		jf.setSize(400, 300);
		jf.setResizable(true);
		jf.setDefaultCloseOperation(3);
		jf.setLocationRelativeTo(null);

		jLabel = new JLabel(INSERT_COIN_BTN);
		jLabel.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel.setVerticalAlignment(SwingConstants.CENTER);
		jf.add(jLabel, BorderLayout.CENTER);

		JPanel bottom = new JPanel();
		addCoinBtn = new javax.swing.JButton(INSERT_COIN_BTN);
		startOrStopBtn = new javax.swing.JButton(GO_STOP_BTN);

		bottom.add(addCoinBtn);
		bottom.add(startOrStopBtn);
		jf.add(bottom, BorderLayout.SOUTH);//用BorderLayout布局管理器，放在最底层

		jf.setVisible(true);
	}

	@Override
	public void connect(Controller controller) {
		this.mController = controller;
	}

	@Override
	public void init() {
		addCoinBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mController.coinInserted();
			}
		});

		startOrStopBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mController.goStopPressed();
			}
		});
	}

	@Override
	public void setDisplay(String s) {
		jf.setTitle(s);
	}

}
