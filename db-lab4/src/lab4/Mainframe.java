package lab4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.xml.soap.Node;

public class Mainframe extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel MainArea, friendArea, showFriendArea, messageListArea,
			messageArea, replyArea;
	private JLabel status;
	private String sql;
	private JTextPane messageTextPane;
	// private JTextArea messageTextArea;
	private JScrollPane friendscrollPane, messageListScrollPane,
			messageContentScrollPane, replyScrollPane;
	private DefaultTreeCellRenderer cellRenderer;
	private static user self;
	// private DefaultListModel<String> listModel;
	// JList<String> friendsList;
	DefaultMutableTreeNode top = new DefaultMutableTreeNode("好友分组");
	private DefaultTreeModel friendTreeModel = new DefaultTreeModel(top);
	private JTree friendTree = new JTree(friendTreeModel);
	private DefaultListModel<message> messageListModel;
	private DefaultListModel<reply> replyListModel;
	private JList<message> messageList;
	private JList<reply> replyList;
	private final Connection connection;
	HashMap<String, DefaultMutableTreeNode> group = new HashMap<>();
	HashMap<String, DefaultMutableTreeNode> friend = new HashMap<>();
	private message messageChosen;

	public Mainframe() {
		connection = null;
	}

	public Mainframe(final String user, final String n, final Connection con) {
		// TODO Auto-generated constructor stub
		super("Lab4");
		// 将用户名存入self
		self = new user(user, n, "你自己");
		connection = con;
		// listModel=new DefaultListModel<>();
		// friendsList=new JList<>(listModel);
		JMenuBar bar = new JMenuBar(); // 定义菜单条
		// 新建文件菜单条
		JMenu fileMenu = new JMenu("文件");
		bar.add(fileMenu);
		JMenuItem newItem = new JMenuItem("退出");
		newItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
		fileMenu.add(newItem);
		// *************************************
		// 用户菜单
		JMenu UserMenu = new JMenu("用户");
		bar.add(UserMenu);
		JMenuItem addFriend = new JMenuItem("添加好友");
		addFriend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				addFriend();
			}
		});
		UserMenu.add(addFriend);
		JMenuItem delFriend = new JMenuItem("删除好友");
		delFriend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				deleteFriend();
			}
		});
		UserMenu.add(delFriend);
		// *************************************
		// 设置提示菜单条
		JMenu helpMenu = new JMenu("帮助");
		JMenuItem aboutItem = new JMenuItem("关于该程序");
		aboutItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "该程序由HIT 1120310726 Ben编写",
						" 程序说明 ", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		helpMenu.add(aboutItem);
		bar.add(helpMenu);
		// ********************************
		MainArea = new JPanel();
		friendArea = new JPanel();
		messageArea = new JPanel();
		replyArea = new JPanel();
		showFriendArea = new JPanel();
		Container c = getContentPane();
		super.setJMenuBar(bar);
		c.add(MainArea, BorderLayout.CENTER);
		// MainArea.setLayout(new GridLayout(1, 3));
		MainArea.setLayout(null);
		// MainArea.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		MainArea.add(friendArea);
		MainArea.add(messageArea);
		MainArea.add(replyArea);
		status = new JLabel();
		c.add(status, BorderLayout.SOUTH);

		// ************设置好友显示区域******************************************
		// friendArea.setPreferredSize(new Dimension(WIDTH/10,HEIGHT));
		friendArea.setBounds(0, 0, WIDTH / 5, HEIGHT * 7 / 8);
		// friendArea.setLayout(new GridLayout(2,1));
		friendArea.add(showFriendArea);
		// friendTree = new JTree();
		showFriend(); // 将朋友显示在friendList中
						// MainArea->friendArea->showFriendArea->friendList
		// showFriendArea.setBackground(Color.WHITE);
		// friendArea.setBackground(Color.WHITE);
		MainArea.setBackground(Color.WHITE);
		friendscrollPane = new JScrollPane(friendTree);
		friendTree.setBorder(BorderFactory.createLoweredBevelBorder());
		cellRenderer = new DefaultTreeCellRenderer();
		cellRenderer.setClosedIcon(new ImageIcon("group.png"));
		cellRenderer.setOpenIcon(new ImageIcon("group.png"));
		cellRenderer.setLeafIcon(new ImageIcon("User.png"));
		friendTree.setCellRenderer(cellRenderer);
		showFriendArea.add(friendscrollPane);
		friendscrollPane.setBorder(BorderFactory.createTitledBorder("好友"));
		friendscrollPane.setPreferredSize(new Dimension(WIDTH * 7 / 40,
				HEIGHT * 9 / 20));
		friendscrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		messageListModel = new DefaultListModel<>();
		messageList = new JList<>(messageListModel);
		messageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		messageListScrollPane = new JScrollPane(messageList);
		messageList.setBorder(BorderFactory.createLoweredBevelBorder());
		messageListScrollPane.setBorder(BorderFactory.createTitledBorder("文章"));
		messageListArea = new JPanel();
		friendArea.add(messageListArea);
		messageListArea.add(messageListScrollPane);
		// friendArea.setBackground(Color.black);
		// messageScrollPane.setBackground(Color.black);
		messageListScrollPane.setPreferredSize(new Dimension(WIDTH * 7 / 40,
				HEIGHT * 3 / 8));
		messageListScrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// messageListScrollPane.add(messageList);
		// messageList.setVisible(true);
		// *********************************************************************************
		// ************设置文章显示区域******************************************
		// messageArea.setBorder(BorderFactory.createEmptyBorder(0,5,5,5));
		messageArea.setBounds(WIDTH / 5, 0, WIDTH * 187 / 240, HEIGHT * 3 / 5);
		// friendArea.setBounds(0, 0, WIDTH/5, HEIGHT);
		// messageArea.setBackground(Color.DARK_GRAY);
		messageArea.setLayout(new BorderLayout());
		messageTextPane = new JTextPane();
		// messageArea.setBackground(Color.DARK_GRAY);
		// messageField.setPreferredSize(new Dimension(WIDTH*2/5,HEIGHT*3/5));
		// messageTextArea.setBackground(Color.LIGHT_GRAY);
		messageContentScrollPane = new JScrollPane(messageTextPane);
		messageArea.add(messageContentScrollPane, BorderLayout.CENTER);
		// messageContentScrollPane.setBorder(BorderFactory.createEmptyBorder(8,
		// 0, 0, 0));
		messageContentScrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		messageContentScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		messageContentScrollPane.setBorder(BorderFactory
				.createTitledBorder("文章内容"));
		messageTextPane.setBorder(BorderFactory.createLoweredBevelBorder());
		// messageContentScrollPane.setBackground(Color.GRAY);
		// messageContentScrollPane.setPreferredSize(new
		// Dimension(WIDTH*29/40,HEIGHT*4/5));
		// messageArea.setBackground(Color.DARK_GRAY);
		// messageField.setPreferredSize(new
		// Dimension(WIDTH*39/50,HEIGHT*59/100));
		// messageField.setBorder(null);
		// messageField.setBackground(Color.blue);
		// messageTextPane.setText(messageTextPane.getText() + "123\n");
		messageTextPane.setEditable(false);
		// messageTextPane.setFocusable(false);
		showMessageList(self);
		// *********************************************************************************
		// ************设置评论区域******************************************************
		replyArea.setBounds(WIDTH / 5, HEIGHT * 3 / 5, WIDTH * 187 / 240,
				HEIGHT * 11 / 40);
		replyArea.setBackground(Color.GREEN);
		replyArea.setLayout(new BorderLayout());
		replyListModel = new DefaultListModel<>();
		replyList = new JList<>(replyListModel);
		replyScrollPane = new JScrollPane(replyList);
		// replyScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),
		// "评论"));
		replyScrollPane.setBorder(BorderFactory.createTitledBorder("评论"));
		replyList.setBorder(BorderFactory.createLoweredBevelBorder());
		replyArea.add(replyScrollPane);
		// *********************************************************************************
		// ************设置文章显示区域******************************************
		friendTree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				// TODO Auto-generated method stub
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) friendTree
						.getLastSelectedPathComponent();
				if (node == null) {
					return;
				}
				Object object = node.getUserObject();
				if (node.isLeaf()) {
					user user = (user) object;
					showMessageList(user);
					status.setText("你选择的是:" + user.getUsername() + " index:"
							+ friendTree.getSelectionPath());
				}
			}
		});
		messageList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				message temp = messageList.getSelectedValue();
				if (!e.getValueIsAdjusting() && temp != null) {
					messageChosen = temp;
					if (temp.getMessage_id() == -1) {
						messageTextPane.setEditable(true);
						messageTextPane.setText("");
						final JTextField titleField = new JTextField("title");
						messageTextPane.setLayout(new BorderLayout());
						messageTextPane.add(titleField, BorderLayout.NORTH);
						final JTextPane newMessagePane = new JTextPane();
						messageTextPane.add(new JScrollPane(newMessagePane),
								BorderLayout.CENTER);
						JButton OK = new JButton("提交");
						messageTextPane.add(OK, BorderLayout.SOUTH);
						OK.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								sql = "insert into message( content , publish_time , title , username ) "
										+ "values( ? , ? , ? , ? )";
								try {
									PreparedStatement stmt = connection
											.prepareStatement(sql);
									String temp_Date = new SimpleDateFormat(
											"yyyy-MM-dd HH:mm:ss")
											.format(new Date());
									stmt.setString(1, newMessagePane.getText());
									stmt.setString(2, temp_Date);
									stmt.setString(3, titleField.getText());
									stmt.setString(4, self.getUsername());
									stmt.executeUpdate();
									status.setText("submit success!");
									showMessageList(self);
								} catch (SQLException e1) {
									e1.printStackTrace();
								}

							}
						});
					} else {
						messageTextPane.removeAll();
						messageTextPane.setEditable(false);
						showMessage(temp);
					}
				}

			}
		});
		// replyList.addListSelectionListener(new ListSelectionListener() {
		//
		// @Override
		// public void valueChanged(ListSelectionEvent e) {
		// // TODO Auto-generated method stub
		// reply temp =replyList.getSelectedValue();
		// if(!e.getValueIsAdjusting()&&temp!=null) {
		// //int selections[] = messageList.getSelectedIndices();
		// //int s= messageList.getSelectedIndex();
		// //messageTextArea.append("choose file :"+messageList.getSelectedValue().toString()+"\n");
		// //showMessage(temp);
		// String
		// temp_reply=JOptionPane.showInputDialog(null,temp.getReply_username()+" : "+temp.getContent());
		// if(temp_reply!=null&&!temp_reply.equals("")) {
		// status.setText("reply to "+temp.getReply_username()+" :"+temp_reply);
		// }
		// }
		// }
		// });
		replyList.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				reply temp = replyList.getSelectedValue();
				if (e.getClickCount() == 2 && temp != null) {
					if (temp.getReply_id() == -1) {
						String replyString = JOptionPane
								.showInputDialog("输入评论：");
						if (replyString != null
								&& messageChosen.getMessage_id() != -1) {
							sql = "insert into "
									+ "reply( content , reply_time , reply_username ) "
									+ "values( ? , ? , ? )";
							try {
								PreparedStatement stmt = connection
										.prepareStatement(sql);
								String temp_Date = new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss")
										.format(new Date());
								stmt.setString(1, replyString);
								stmt.setString(2, temp_Date);
								stmt.setString(3, self.getUsername());
								stmt.executeUpdate();
								sql = "select * from reply where reply_time = ? ";
								PreparedStatement temp1 = connection
										.prepareStatement(sql);
								temp1.setString(1, temp_Date);
								ResultSet rs = temp1.executeQuery();
								if (rs.next()) {
									int reply_id = rs.getInt("reply_id");
									sql = "insert into "
											+ "message_reply( reply_id , message_id ) "
											+ "values( ? , ? )";
									PreparedStatement temp2 = connection
											.prepareStatement(sql);
									temp2.setInt(1, reply_id);
									temp2.setInt(2,
											messageChosen.getMessage_id());
									temp2.executeUpdate();
									getcontent(messageChosen);
									status.setText("reply success!");
								} else {
									JOptionPane.showMessageDialog(null,
											"未知错误：插入reply表确找不到该项", "错误",
											JOptionPane.ERROR_MESSAGE);
								}
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
						}
					} else {
						sql = "select * from reply "
								+ "join person on username = reply_username "
								+ "where reply_id in "
								+ "( select reply_for_reply_id from reply_reply "
								+ "where reply_id = "
								+ temp.getReply_id()
								+ " )";
						String showString = temp.getNick_name() + "(username:"
								+ temp.getReply_username() + ")的评论:"
								+ temp.getContent() + "\n";
						try {
							Statement stmt1 = connection.createStatement();
							ResultSet tempResultSet = stmt1.executeQuery(sql);
							while (tempResultSet.next()) {
								showString += "\t\t"
										+ tempResultSet.getString("name")
										+ "(username:"
										+ tempResultSet.getString("username")
										+ ")回复："
										+ tempResultSet.getString("content")
										+ "\n";
							}
						} catch (SQLException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						showString += "输入想回复的或取消";
						String temp_reply = JOptionPane.showInputDialog(null,
								showString);
						if (temp_reply != null && !temp_reply.equals("")) {
							sql = "insert into "
									+ "reply( content , reply_time , reply_username )"
									+ " values( ? , ? , ? )";
							try {
								Statement stmt1 = connection.createStatement();
								String temp_Date = new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss")
										.format(new Date());
								PreparedStatement stmt = connection
										.prepareStatement(sql);
								stmt.setString(1, temp_reply);
								stmt.setString(2, temp_Date);
								stmt.setString(3, self.getUsername());
								stmt.executeUpdate();
								sql = "select reply_id from reply where reply_time = ? ";
								PreparedStatement stmt2 = connection
										.prepareStatement(sql);
								stmt2.setString(1, temp_Date);
								ResultSet rs = stmt2.executeQuery();
								if (rs.next()) {
									int reply_id = rs.getInt("reply_id");
									sql = "insert into reply_reply( reply_id , reply_for_reply_id ) values( "
											+ temp.getReply_id()
											+ " , "
											+ reply_id + " )";
									stmt1.executeUpdate(sql);
									status.setText("reply success!");
								} else
									throw (new SQLException());
							} catch (SQLException e1) {
								// TODO: handle exception
								e1.printStackTrace();
							}
						}
					}
				}
			}
		});
		// *********************************************************************************
		// **********friendTree事件***************************************************
		friendTree.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

				if (e.getClickCount() == 2) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) friendTree
							.getLastSelectedPathComponent();
					if (node != null) {
						user shown_user = (user) node.getUserObject();
						if (shown_user != null) {
							System.out.println(shown_user.getUsername());
							if (shown_user.getUsername() == self.getUsername()) {
								messageTextPane.setEditable(true);
								messageTextPane.setText("");
								final JTextField school = new JTextField(
										"school");
								final JTextField degree = new JTextField(
										"degree");
								final JTextField start_year = new JTextField(
										"start_year");
								final JTextField end_year = new JTextField(
										"end_year");
								messageTextPane
										.setLayout(new GridLayout(10, 1));
								messageTextPane.add(school);
								messageTextPane.add(degree);
								messageTextPane.add(start_year);
								messageTextPane.add(end_year);
								JButton submit_study = new JButton("提交学习经历");
								submit_study
										.addActionListener(new ActionListener() {

											@Override
											public void actionPerformed(
													ActionEvent e) {
												// TODO Auto-generated method
												// stub
												if (start_year
														.getText()
														.matches(
																"190[1-9]|19[1-9][0-9]|20[0-9]{2}|21[0-4][0-9]|215[0-5]")
														&& end_year
																.getText()
																.matches(
																		"190[1-9]|19[1-9][0-9]|20[0-9]{2}|21[0-4][0-9]|215[0-5]")
														&& Integer
																.parseInt(start_year
																		.getText()) <= Integer
																.parseInt(end_year
																		.getText())) {
													sql = "select * from education where username = ? ";
													try {
														PreparedStatement stmt = connection
																.prepareStatement(sql);
														stmt.setString(1, self
																.getUsername());
														ResultSet rs = stmt
																.executeQuery();
														if (rs.next()) {
															sql = "update education "
																	+ "set school = ? , degree = ? , start_year = ? , end_year = ?  "
																	+ "where username = ? ; ";
															PreparedStatement temp = connection
																	.prepareStatement(sql);
															temp.setString(
																	1,
																	school.getText());
															temp.setString(
																	2,
																	degree.getText());
															temp.setString(
																	3,
																	start_year
																			.getText());
															temp.setString(
																	4,
																	end_year.getText());
															temp.setString(
																	5,
																	self.getUsername());
															temp.executeUpdate();
															status.setText("replace success!");
														} else {
															sql = "insert education"
																	+ "( school , degree , start_year , end_year , username ) "
																	+ "values( ? , ? , ? , ? , ? )";
															PreparedStatement temp = connection
																	.prepareStatement(sql);
															temp.setString(
																	1,
																	school.getText());
															temp.setString(
																	2,
																	degree.getText());
															temp.setString(
																	3,
																	start_year
																			.getText());
															temp.setString(
																	4,
																	end_year.getText());
															temp.setString(
																	5,
																	self.getUsername());
															temp.executeUpdate();
															status.setText("insert success!");
														}
													} catch (SQLException e1) {
														// TODO Auto-generated
														// catch block
														e1.printStackTrace();
													}

												} else {
													JOptionPane
															.showMessageDialog(
																	null,
																	"起始和结束年份范围：1901-2155",
																	"错误",
																	JOptionPane.ERROR_MESSAGE);
												}
											}
										});
								messageTextPane.add(submit_study);
								final JTextField start = new JTextField("start");
								final JTextField end = new JTextField("end");
								final JTextField position = new JTextField(
										"position");
								final JTextField company = new JTextField(
										"company");
								messageTextPane
										.setLayout(new GridLayout(10, 1));
								messageTextPane.add(start);
								messageTextPane.add(end);
								messageTextPane.add(position);
								messageTextPane.add(company);
								JButton submit_work = new JButton("提交工作经历");
								submit_work
										.addActionListener(new ActionListener() {

											@Override
											public void actionPerformed(
													ActionEvent e) {
												// TODO Auto-generated method
												// stub
												if (start
														.getText()
														.matches(
																"190[1-9]|19[1-9][0-9]|20[0-9]{2}|21[0-4][0-9]|215[0-5]")
														&& end.getText()
																.matches(
																		"190[1-9]|19[1-9][0-9]|20[0-9]{2}|21[0-4][0-9]|215[0-5]")
														&& Integer.parseInt(start
																.getText()) <= Integer.parseInt(end
																.getText())) {
													sql = "select * from work_experience where username = ? ";
													try {
														PreparedStatement stmt = connection
																.prepareStatement(sql);
														stmt.setString(1, self
																.getUsername());
														ResultSet rs = stmt
																.executeQuery();
														if (rs.next()) {
															sql = "update work_experience "
																	+ "set start = ? , end = ? , position = ? , company = ?  "
																	+ "where username = ? ; ";
															PreparedStatement temp = connection
																	.prepareStatement(sql);
															temp.setString(
																	1,
																	start.getText());
															temp.setString(
																	2,
																	end.getText());
															temp.setString(
																	3,
																	position.getText());
															temp.setString(
																	4,
																	company.getText());
															temp.setString(
																	5,
																	self.getUsername());
															temp.executeUpdate();
															status.setText("replace success!");
														} else {
															sql = "insert work_experience"
																	+ "( start , end , position , company , username ) "
																	+ "values( ? , ? , ? , ? , ? )";
															PreparedStatement temp = connection
																	.prepareStatement(sql);
															temp.setString(
																	1,
																	start.getText());
															temp.setString(
																	2,
																	end.getText());
															temp.setString(
																	3,
																	position.getText());
															temp.setString(
																	4,
																	company.getText());
															temp.setString(
																	5,
																	self.getUsername());
															temp.executeUpdate();
															status.setText("insert success!");
														}
													} catch (SQLException e1) {
														// TODO Auto-generated
														// catch block
														e1.printStackTrace();
													}

												} else {
													JOptionPane
															.showMessageDialog(
																	null,
																	"起始和结束年份范围：1901-2155",
																	"错误",
																	JOptionPane.ERROR_MESSAGE);
												}
											}
										});
								messageTextPane.add(submit_work);

							} else {
								String information = "EDUCATION:\n	";
								sql = "select * from education where username = ? ";
								PreparedStatement temp1, temp2;
								try {
									temp1 = connection.prepareStatement(sql);
									temp1.setString(1, shown_user.getUsername());
									ResultSet rSet = temp1.executeQuery();
									if (rSet.next()) {
										information += "school:"
												+ rSet.getString("school")
												+ "\n	";
										information += "degree:"
												+ rSet.getString("degree")
												+ "\n	";
										information += "start_year:"
												+ rSet.getString("start_year")
												+ "\n	";
										information += "end_year:"
												+ rSet.getString("end_year")
												+ "\n";
									} else {
										information += "null\n";
									}
									information += "WORK EXPERIENCE:\n	";
									sql = "select * from work_experience where username = ? ";
									temp2 = connection.prepareStatement(sql);
									temp2.setString(1, shown_user.getUsername());
									ResultSet rSet1 = temp2.executeQuery();
									if (rSet1.next()) {
										information += "start:"
												+ rSet1.getString("start")
												+ "\n	";
										information += "end:"
												+ rSet1.getString("end")
												+ "\n	";
										information += "position:"
												+ rSet1.getString("position")
												+ "\n	";
										information += "company:"
												+ rSet1.getString("company")
												+ "\n";
									} else {
										information += "null\n";
									}
								} catch (SQLException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								JOptionPane
										.showMessageDialog(null, information);
							}
						} else {
							System.out.println("user null!!!!");
						}
					} else {
						System.out.println("select null!!");
					}
				}
			}
		});
		// *****************************************************************************
		Dimension temp = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((temp.width - WIDTH) / 2, (temp.height - HEIGHT) / 2, WIDTH,
				HEIGHT);
		setVisible(true);
		status.setText("welcome " + self.getName()
				+ " , connect database succeed");
		// setResizable(false);
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
	}

	private void showMessage(message m) {
		messageTextPane.removeAll();
		StyledDocument doc = messageTextPane.getStyledDocument();
		// 编辑title的格式
		SimpleAttributeSet title = new SimpleAttributeSet();
		StyleConstants.setAlignment(title, StyleConstants.ALIGN_CENTER);
		StyleConstants.setBold(title, true);
		StyleConstants.setFontSize(title, 26);
		StyleConstants.setFontFamily(title, "幼圆");
		// 编辑content的格式
		SimpleAttributeSet content = new SimpleAttributeSet();
		StyleConstants.setLineSpacing(content, 2);
		StyleConstants.setFontFamily(content, "宋体");
		// 编辑date的格式
		SimpleAttributeSet date = new SimpleAttributeSet();
		StyleConstants.setAlignment(date, StyleConstants.ALIGN_RIGHT);
		StyleConstants.setFontSize(date, 8);
		try {
			doc.remove(0, doc.getLength());
			doc.insertString(doc.getLength(), m.getTitle() + "\n", title);
			doc.insertString(doc.getLength(), m.getContent() + "\n", content);
			doc.insertString(doc.getLength(), m.getPublish_time() + "\n", date);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getcontent(m);
		// doc.setParagraphAttributes(doc.getLength(),
		// doc.getLength()+m.getTitle().length(), title, false);
		// messageTextPane.setText(m.getTitle()+"\n");
		// doc.setParagraphAttributes(doc.getLength(),
		// doc.getLength()+m.getContent().length(), content, false);
		// messageTextPane.setText(m.getContent()+"\n");
		// doc.setParagraphAttributes(doc.getLength(),
		// doc.getLength()+String.valueOf(m.getPublish_time()).length(), date,
		// false);
		// messageTextPane.setText(String.valueOf(m.getPublish_time())+"\n");
	}

	private void getcontent(message m) {
		sql = "select * from person_reply "
				+ "where reply_id in ( "
				+ "select reply_id "
				+ "from message_reply "
				+ "where message_id= ? )";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, m.getMessage_id());
			ResultSet rSet = stmt.executeQuery();
			replyListModel.clear();
			while (rSet.next()) {
				reply temp = new reply(rSet.getInt("reply_id"),
						rSet.getString("content"),
						rSet.getString("reply_username"),
						rSet.getString("name"), rSet.getDate("reply_time"));
				replyListModel.addElement(temp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		reply temp = new reply(-1);
		replyListModel.addElement(temp);
	}

	private void showMessageList(user u) {
		sql = "select * from message where username = ?";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, u.getUsername());
			ResultSet rSet = stmt.executeQuery();
			messageListModel.clear();
			while (rSet.next()) {
				// System.out.println(rSet.getInt("message_id")+rSet.getString("content")+rSet.getDate("publish_time"));
				message temp = new message(rSet.getInt("message_id"),
						rSet.getString("title"), rSet.getString("content"),
						rSet.getDate("publish_time"));
				messageListModel.addElement(temp);
			}

			// messageList.setModel(messageListModel);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (u.getUsername().equals(self.getUsername()))
			messageListModel.addElement(new message(-1));
	}

	private void deleteFriend() {
		String friend_id = JOptionPane.showInputDialog(null, "输入想删除的好友的用户名");
		sql = "select username,genre,group_id "
				+ " from friend_group_s_person join friend_group using(group_id) "
				+ " where group_id in( " + " select group_id "
				+ " from friend_group " + " where group_owner = ? " + ")";
		PreparedStatement stmt, stmt1;
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, self.getUsername());
			ResultSet rs = stmt.executeQuery();
			boolean found = false;
			// System.out.println("try find: "+friend_id);
			while (rs.next()) {
				// System.out.println("friend: "+rs.getString("username"));
				if (rs.getString("username").equals(friend_id)) {
					found = true;
					sql = "delete from friend_group_s_person where group_id = ? and username = ? ";
					stmt1 = connection.prepareStatement(sql);
					stmt1.setInt(1, rs.getInt("group_id"));
					stmt1.setString(2, rs.getString("username"));
					stmt1.executeUpdate();
					friendTreeModel.removeNodeFromParent(friend.get(rs
							.getString("username")));
					break;
				}
			}
			if (!found) {
				JOptionPane.showMessageDialog(null, "没有这个用户", "错误",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addFriend() {
		String friend_id = JOptionPane.showInputDialog(null, "输入想添加的用户的用户名");
		sql = "select * from person where username = ?";
		PreparedStatement stmt, stmt1;
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, friend_id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				String username = rs.getString("username");
				String genre = JOptionPane.showInputDialog(null,
						"昵称：" + rs.getString("name") + "\n用户名：" + username
								+ "\n输入他的分组：");
				sql = "select group_id "
						+ "from friend_group "
						+ "where group_owner = ? "
						+ "and genre = ? ";
				stmt1 = connection.prepareStatement(sql);
				stmt1.setString(1, self.getUsername());
				stmt1.setString(2, genre);
				ResultSet tempSet = stmt1.executeQuery();
				if (tempSet.next()) {
					int group_id = tempSet.getInt("group_id");
					sql = "insert into "
							+ "friend_group_s_person( group_id , username )"
							+ " values( ? , ? )";
					PreparedStatement stmt2 = connection.prepareStatement(sql);
					stmt2.setInt(1, group_id);
					stmt2.setString(2, username);
					stmt2.executeUpdate();
					DefaultMutableTreeNode newfriend = new DefaultMutableTreeNode(
							new user(friend_id, rs.getString("name"), genre));
					group.get(genre).add(newfriend);
					friend.put(username, newfriend);
					friendTree.updateUI();
					status.setText("add friend success!");
				} else {
					JOptionPane.showMessageDialog(null, "没有这个分组", "错误",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, "没有这个用户", "错误",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showFriend() {
		sql = "select name,username,genre "
				+ " from friend_group_s_person "
				+ "join person using(username) "
				+ "join friend_group using(group_id) "
				+ " where group_id in( " + " select group_id "
				+ " from friend_group " + " where group_owner = ? " + ")";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, self.getUsername());
			ResultSet rSet = stmt.executeQuery();
			String temp;
			top.removeAllChildren();
			DefaultMutableTreeNode self_group = new DefaultMutableTreeNode(
					"你自己");
			DefaultMutableTreeNode selfNode = new DefaultMutableTreeNode(self);
			top.add(self_group);
			self_group.add(selfNode);
			while (rSet.next()) {
				DefaultMutableTreeNode person = new DefaultMutableTreeNode(
						new user(rSet.getString("username"), rSet
								.getString("name"), rSet.getString("genre")));
				temp = rSet.getString("genre");
				DefaultMutableTreeNode group_name = group.get(temp);
				if (group_name == null) {
					group_name = new DefaultMutableTreeNode(temp);
					group_name.add(person);
					group.put(temp, group_name);
				} else {
					group_name.add(person);
				}
				friend.put(rSet.getString("username"), person);
				top.add(group_name);
			}
			friendTreeModel.reload();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static final int WIDTH = 800, HEIGHT = 550;

	class user {
		public user() {
			username = "";
			name = "";
			genre = "";
		}

		public user(String u, String n) {
			username = u;
			name = n;
		}

		public user(String u, String n, String g) {
			username = u;
			name = n;
			genre = g;
		}

		public String getUsername() {
			return username;
		}

		public String getName() {
			return name;
		}

		public String getGenre() {
			return genre;
		}

		public String toString() {
			return name + " (" + username + ")";
		}

		private String username, name, genre;
	}

	class message {
		public int getMessage_id() {
			return message_id;
		}

		public String getTitle() {
			return title;
		}

		public String getContent() {
			return content;
		}

		public Date getPublish_time() {
			return publish_time;
		}

		public String toString() {
			return title;// Integer.toString(message_id);
		}

		public message() {
		}

		public message(int id) {
			if (id == -1) {
				message_id = id;
				title = "新文章";
			} else
				System.err.println("message wrong");
		}

		public message(int id, String con, Date publishDate) {
			message_id = id;
			// con.replaceAll("\\n", "\n");
			content = con;
			publish_time = publishDate;
		}

		public message(int id, String t, String con, Date publishDate) {
			message_id = id;
			// con.replaceAll("\\n", "\n");
			content = con;
			publish_time = publishDate;
			title = t;
		}

		private int message_id;
		private String title, content;
		private Date publish_time;
	}

	class reply {
		public String getContent() {
			return content;
		}

		public int getReply_id() {
			return reply_id;
		}

		public String getReply_username() {
			return reply_username;
		}

		public Date getReply_time() {
			return reply_time;
		}

		public String getNick_name() {
			return nick_name;
		}

		public String toString() {
			return content;
		}

		public reply() {
		}

		public reply(int i) {
			if (i == -1) {
				reply_id = i;
				content = "新评论";
			}
		}

		public reply(int i, String c) {
			reply_id = i;
			content = c;
		}

		public reply(int i, String c, String ru, String nn, Date r) {
			reply_id = i;
			content = c;
			reply_username = ru;
			reply_time = r;
			nick_name = nn;
		}

		private int reply_id;
		private String content, reply_username, nick_name;
		private Date reply_time;
	}
}
