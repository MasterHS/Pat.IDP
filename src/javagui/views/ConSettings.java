package javagui.views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;

import java.io.File;
//import java.nio.file.*;
import java.awt.GridBagLayout;

import javax.swing.JSplitPane;

import java.awt.BorderLayout;

import Classes.ConnectToDatabase;
import Classes.SettingsRetrieval;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextPane;

import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JPasswordField;
import javax.swing.JFormattedTextField;

//import java.awt.Window.Type;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.SwingConstants;
import javax.swing.JFileChooser;
public class ConSettings {

	public JFrame frmConnectionSettings;
	private JTextField txtHostname;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JTextField txtPort;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConSettings window = new ConSettings();
					window.frmConnectionSettings.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ConSettings() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmConnectionSettings = new JFrame();
		//frmConnectionSettings.setType(Type.POPUP);
		frmConnectionSettings.setAlwaysOnTop(true);
		frmConnectionSettings.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				SettingsRetrieval SR=new SettingsRetrieval();
				txtHostname.setText(SR.GetParam("HOST_NAME"));
				txtPort.setText(SR.GetParam("PORT"));
				txtUsername.setText(SR.GetParam("USER_NAME"));
				txtPassword.setText(SR.GetParam("PASSWORD"));
				//txtPgnuplotPath.setText(SR.GetParam("WORKSPACE"));
			}
		});
		frmConnectionSettings.setTitle("Settings");
		frmConnectionSettings.setResizable(false);
		frmConnectionSettings.setBounds(100, 100, 434, 200);
		frmConnectionSettings.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmConnectionSettings.getContentPane().setLayout(null);
		
		txtHostname = new JTextField();
		txtHostname.setBounds(92, 22, 232, 20);
		frmConnectionSettings.getContentPane().add(txtHostname);
		txtHostname.setColumns(10);
		
		JLabel lblHostName = new JLabel("Host Name");
		lblHostName.setBounds(21, 25, 61, 14);
		frmConnectionSettings.getContentPane().add(lblHostName);
		
		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(334, 25, 32, 14);
		frmConnectionSettings.getContentPane().add(lblPort);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(21, 56, 61, 14);
		frmConnectionSettings.getContentPane().add(lblUsername);
		
		txtUsername = new JTextField();
		txtUsername.setColumns(10);
		txtUsername.setBounds(92, 53, 134, 20);
		frmConnectionSettings.getContentPane().add(txtUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(21, 84, 61, 14);
		frmConnectionSettings.getContentPane().add(lblPassword);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SettingsRetrieval SR=new SettingsRetrieval();
				SR.saveParam("HOST_NAME", txtHostname.getText());
				SR.saveParam("PORT", txtPort.getText());
				SR.saveParam("USER_NAME", txtUsername.getText());
				SR.saveParam("PASSWORD", txtPassword.getText());
				//SR.saveParam("WORKSPACE", txtPgnuplotPath.getText());
				//SR.saveParam("WORKSPACE", Paths.get("../out/temp.txt").toAbsolutePath().toString());
				File f=new File("");
				
				SR.saveParam("WORKSPACE", f.getAbsolutePath());
				frmConnectionSettings.setVisible(false);
				
			}
		});
		btnOk.setBounds(334, 135, 89, 23);
		frmConnectionSettings.getContentPane().add(btnOk);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmConnectionSettings.setVisible(false);
			}
		});
		btnCancel.setBounds(236, 135, 89, 23);
		frmConnectionSettings.getContentPane().add(btnCancel);
		
		txtPassword = new JPasswordField();
		txtPassword.setText("txtPassword");
		txtPassword.setBounds(92, 84, 134, 20);
		frmConnectionSettings.getContentPane().add(txtPassword);
		
		txtPort = new JTextField();
		txtPort.setText("0");
		txtPort.setBounds(364, 22, 47, 20);
		frmConnectionSettings.getContentPane().add(txtPort);
		txtPort.setColumns(10);
		
		JPanel panel = new JPanel();
		panel.setToolTipText("");
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(10, 11, 411, 113);
		frmConnectionSettings.getContentPane().add(panel);
		panel.setLayout(null);
		
		JButton btnTestConnection = new JButton("Test Connection");
		btnTestConnection.setBounds(280, 69, 121, 33);
		panel.add(btnTestConnection);
		btnTestConnection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SettingsRetrieval SR=new SettingsRetrieval();
				ConnectToDatabase con=new ConnectToDatabase(txtHostname.getText(),
						txtPort.getText(),
						txtUsername.getText(),
						txtPassword.getText()
						);
				if(con.connect())
				{
					JOptionPane.showMessageDialog(null,"Succesfully connected");
				}
				else
				{
					JOptionPane.showMessageDialog(null,"error");
				}
			}
		});
	}
}
