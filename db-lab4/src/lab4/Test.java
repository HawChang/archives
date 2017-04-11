package lab4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import lab4.Mainframe.user;

public class Test extends JFrame {
	private JPanel MainArea, friendArea, showFriendArea, messageArea, area2;
	private JLabel status;
	private JTree jTree;
	private JTextField messagField;
	private JScrollPane scrollPane;
	private Connection connection;
	private String username, name;
	public Test(final String user, final String n, final Connection con) {
		super("test");
		username = user; // 将用户名存入user中
		name = n;
		connection = con;
		MainArea = new JPanel();
		friendArea = new JPanel();
		showFriendArea = new JPanel();
		Container c = getContentPane();
		c.add(MainArea, BorderLayout.CENTER);
		MainArea.setLayout(null);
		MainArea.add(friendArea);
		friendArea.setBounds(0, 0, WIDTH / 5, HEIGHT);
		// friendArea.setLayout(new GridLayout(2,1));
		friendArea.add(showFriendArea);
		//jTree=new JTree();
		showFriend();
		scrollPane = new JScrollPane(jTree);
		jTree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree
						.getLastSelectedPathComponent();
				if (node == null)
					return;
				Object object = node.getUserObject();
				if (node.isLeaf()) {
					status.setText("你选择了：" + ((user) object).getUsername());
				}

			}
		});
		showFriendArea.add(scrollPane);
		setResizable(false);
		setSize(400, 200);
		setVisible(true);
	}

	private void showFriend() {

	}
}
