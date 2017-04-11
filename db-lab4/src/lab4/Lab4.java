package lab4;

import java.sql.*;
import java.awt.event.*;

import javax.swing.*;


public class Lab4 extends JFrame {
	/**
	 * author:1120310726 date:2015/5/11
	 */
	private static final long serialVersionUID = 1L;
	public static void main(String args[]) {
		ConnectDB con=new ConnectDB();
		connect=con.getconnect();
			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (Exception e) {
			}// 将界面设置为当前windows风格
			Login frame = new Login(connect);
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}

			});
	}
	private static Connection connect;
}

