package lab4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Login extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel, panel2;
	private JLabel prompt;
	private JButton login, Sign_up;
	private JTextField user, password;
	private String sql, userString, passwordString;
	private ResultSet rSet;
	public Login(){}
	public Login(final Connection connection) {
		// TODO Auto-generated constructor stub
		super("Login");
		panel = new JPanel();
		panel2 = new JPanel();
		prompt =new JLabel();
		login = new JButton("Login");
		Sign_up = new JButton("Sign up");
		user = new JTextField("user");
		password = new JTextField("password");
		Container c = getContentPane();
		panel.setBorder(BorderFactory.createEmptyBorder(HEIGHT/8, WIDTH/50, HEIGHT/50, WIDTH/50));
		c.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(4, 1,0,HEIGHT/50));
		panel.add(user);
		panel.add(password);
		panel.add(panel2);
		panel.add(prompt);
		panel2.setLayout(new GridLayout(1, 2));
		panel2.add(login);
		panel2.add(Sign_up);
		login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				userString = user.getText();
				passwordString = password.getText();
				if (userString.equals("") || passwordString.equals("")) {
					prompt.setForeground(Color.RED);
					prompt.setText("username or password can not be empty.");
				} else {
					try {
						sql = "select * from person where username = ? and password = ?";
						PreparedStatement statement = connection
								.prepareStatement(sql);
						statement.setString(1, userString);
						statement.setString(2, passwordString);
						rSet=statement.executeQuery();
						if(rSet.next()){
							prompt.setForeground(Color.BLACK);
							//System.out.println(rSet.getString("username")+"   "+rSet.getString("name"));
							prompt.setText(rSet.getString("username")+rSet.getString("name"));
							Mainframe frame = new Mainframe(rSet.getString("username"),rSet.getString("name"),connection);
							frame.addWindowListener(new WindowAdapter() {
								public void windowClosing(WindowEvent e) {
									System.exit(0);
								}
							});
							setVisible(false);
						}else{	
							prompt.setForeground(Color.RED);
							prompt.setText("username or password wrong.");
						}
						} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
			}
		});
		Sign_up.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				userString = user.getText();
				passwordString = password.getText();
				if (userString.equals("") || passwordString.equals("")) {
					prompt.setForeground(Color.RED);
					prompt.setText("username or password can not be empty.");
				} else {
					try {
						sql = "select * from person where username = ?";
						PreparedStatement statement = connection
								.prepareStatement(sql);
						statement.setString(1, userString);
						rSet=statement.executeQuery();
						if(rSet.next()){
							prompt.setForeground(Color.RED);
							prompt.setText("username already exists.");
						}else{
							try{
								sql ="insert into person(username,password) values(?,?)";
								PreparedStatement statement2= connection.prepareStatement(sql);
								statement2.setString(1, userString);
								statement2.setString(2, passwordString);
								if(statement2.executeUpdate()==1){
									prompt.setForeground(Color.GREEN);
									prompt.setText("sign up succeed.please login.");
								}else{
									prompt.setForeground(Color.RED);
									prompt.setText("unknown problem occurs");
								}
							}catch(SQLException e2){
								e2.printStackTrace();
							}
						}
						} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		Dimension temp = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((temp.width - WIDTH) / 2, (temp.height - HEIGHT) / 2, WIDTH,
				HEIGHT);
		setSize(WIDTH, HEIGHT);
		setVisible(true);
		setResizable(false);
	}
	private final static int WIDTH = 400;
	private final static int HEIGHT = 200;
}
