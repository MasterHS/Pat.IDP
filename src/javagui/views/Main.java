package javagui.views;



import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;

import Classes.*;

import java.util.Enumeration;








import javax.swing.JCheckBox;
import javax.swing.JLabel;


import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JRadioButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JTextField;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.AbstractButton;

public class Main extends JFrame {
	SettingsRetrieval SR=new SettingsRetrieval();
	ConnectToDatabase c=new ConnectToDatabase(SR.GetParam("HOST_NAME"),
			SR.GetParam("PORT"),
			SR.GetParam("USER_NAME"),
			SR.GetParam("PASSWORD")
			);
	Object[][] data=null;
	String[] columnNames=new String[]{"Auth","Number","Kind"};
	private JMenuBar menuBar;
	private JMenu mnView;
	private JMenuItem mntmConnectionSettings;
	private JTable Jtable1;
	private JScrollPane scrollPane;
	private final ButtonGroup bG_Dimension = new ButtonGroup();
	private final ButtonGroup bG_Hypertext = new ButtonGroup();
	private JTextField txtSeconds;
	private JTextField txtLayer;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		setTitle("Patent Cloud");
	
		setResizable(false);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 595, 436);
		
		final JCheckBox chkptnfam = new JCheckBox("Include patent family");
		chkptnfam.setBounds(318, 226, 127, 23);
		
		menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 581, 21);
		
		mnView = new JMenu("View");
		menuBar.add(mnView);
		
		mntmConnectionSettings = new JMenuItem("Connection Settings");
		mntmConnectionSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ConSettings window = new ConSettings();
				window.frmConnectionSettings.setVisible(true);
				
			}
		});
		mnView.add(mntmConnectionSettings);
		
		JButton btnAddPatent = new JButton("Add Patent");
		btnAddPatent.setBounds(10, 285, 127, 23);
		
		
		btnAddPatent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(Jtable1.getRowCount()==0)
				{
					data=new Object[1][3];
					String[] array=JOptionPane.showInputDialog("Please enter patent number ex: US 5861363 A" ).split(" ");
					data[0][0]=array[0];
					data[0][1]=array[1];
					data[0][2]=array[2];
					Jtable1.setModel(new DefaultTableModel(data,columnNames));
				}
				else
				{
					Object[][] temp=new Object[data.length+1][3];
					for(int i=0;i<data.length;i++)
						for(int j=0;j<3;j++)
						temp[i][j]=data[i][j];
					
					String[] array=JOptionPane.showInputDialog("Please enter patent number ex: US 5861363 A" ).split(" ");
					temp[data.length][0]=array[0];
					temp[data.length][1]=array[1];
					temp[data.length][2]=array[2];
					data=temp;
					Jtable1.setModel(new DefaultTableModel(data,columnNames));
					
				}
				}
			
		});
		
		JButton btnRmvPatent = new JButton("Remove Patent");
		btnRmvPatent.setBounds(155, 285, 127, 23);
		btnRmvPatent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int position=Jtable1.getSelectedRow();
				Object[][] temp=new Object[data.length-1][3];
				for(int i=0;i<position;i++)
				{
						temp[i][0]=data[i][0];
						temp[i][1]=data[i][1];
						temp[i][2]=data[i][2];
				}	
				for(int i=position+1;i<data.length;i++)
				{
						temp[i-1][0]=data[i][0];
						temp[i-1][1]=data[i][1];
						temp[i-1][2]=data[i][2];
						
				}
				data=temp;
				Jtable1.setModel(new DefaultTableModel(data,columnNames));
			}
		});
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 27, 276, 247);
		
				
				Jtable1 = new JTable();
				scrollPane.setViewportView(Jtable1);
				Jtable1.setModel(new DefaultTableModel(
					new Object[][] {
					},
					new String[] {
					}
				));
				
				JButton btnGetCloud = new JButton("Plot");
				btnGetCloud.setBounds(292, 341, 127, 39);
				
				btnGetCloud.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						SettingsRetrieval SR=new SettingsRetrieval();
						String workspacepath=SR.GetParam("WORKSPACE");
						String dataFilePath=workspacepath.replaceAll("\\\\", "/")+"/out/data.dat";		
						File f=new File(dataFilePath);
						if(f.exists())
							f.delete();
						System.out.println(dataFilePath);
						int rowsCount=Jtable1.getRowCount();						
						if(rowsCount>0)
						{
							for(int i=0;i<rowsCount;i++)
							{
								int modeDim=-1;
								int modeHyper=-1;
								Enumeration elements = bG_Dimension.getElements();
							    while (elements.hasMoreElements()) {
							      AbstractButton button = (AbstractButton)elements.nextElement();
							      if (button.isSelected()) {
							        if(button.getText()=="2D")
							        {
							        	modeDim=1;//2D
							        }
							        else
							        {
							        	modeDim=2;//3D
							        }
							      }
							    }
							    elements = bG_Hypertext.getElements();
							    while (elements.hasMoreElements()) {
								      AbstractButton button = (AbstractButton)elements.nextElement();
								      if (button.isSelected()) {
								        if(button.getText()=="Without hypertext" && modeDim==1)//2d+without are selected
								        {								        	
								        	modeHyper=1;
								        	System.out.println("2D+ Without Hypertext are selected");
								        }
								        else if(button.getText()=="Without hypertext" && modeDim==2)//3d+without are selected
								        {
								        	modeHyper=2;
								        	System.out.println("3D+ Without Hypertext are selected");
								        }
								        else if(button.getText()!="Without hypertext" && modeDim==1)//2d+with are selected
								        {								        	
								        	modeHyper=3;
								        	System.out.println("2D+ With Hypertext are selected");
								        }
								        else if(button.getText()!="Without hypertext" && modeDim==2)//3d+with are selected
								        {
								        	modeHyper=4;
								        	System.out.println("3D + With Hypertext are selected");
								        }
								        	
								      }
								    }
									String auth=Jtable1.getValueAt(i, 0).toString();
									String number=Jtable1.getValueAt(i, 1).toString();
									String kind=Jtable1.getValueAt(i, 2).toString();
									c.setMode(modeHyper);
									
									c.setDataFilePath(dataFilePath);									
									c.serLayerNumber(txtLayer.getText());
									c.setTimeout(Integer.parseInt(txtSeconds.getText()));
									c.setCircularFlag(false);
									c.setMaxLevel(4);
									c.setTimeOrderFlag(true);
									if(chkptnfam.isSelected())
										c.runStoredProcedure(auth,number,kind,"Y");
									else
										c.runStoredProcedure(auth,number,kind,"N");
									
									c.getPoints(i+1);
									
							
							}
							
						try {
							c.drawToGnuplot();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						}
					}
				});
				
				JPanel panel = new JPanel();
				panel.setBounds(318, 39, 243, 35);
				FlowLayout flowLayout_1 = (FlowLayout) panel.getLayout();
				flowLayout_1.setAlignment(FlowLayout.LEFT);
				panel.setBorder(new LineBorder(new Color(0, 0, 0)));
				
				JPanel panel_1 = new JPanel();
				panel_1.setBounds(318, 80, 243, 65);
				FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
				
				JPanel panel_2 = new JPanel();
				panel_2.setBounds(318, 156, 243, 63);
				FlowLayout flowLayout_2 = (FlowLayout) panel_2.getLayout();
				flowLayout_2.setAlignment(FlowLayout.LEFT);
				panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
				
				JButton btnRefresh = new JButton("Refresh");
				btnRefresh.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int rowsCount=Jtable1.getRowCount();						
						if(rowsCount>0)
						{
							//for(int i=0;i<rowsCount;i++)
							{
								int modeDim=-1;
								int modeHyper=-1;
								Enumeration elements = bG_Dimension.getElements();
							    while (elements.hasMoreElements()) {
							      AbstractButton button = (AbstractButton)elements.nextElement();
							      if (button.isSelected()) {
							        if(button.getText()=="2D")
							        {
							        	modeDim=1;//2D
							        }
							        else
							        {
							        	modeDim=2;//3D
							        }
							      }
							    }
							    elements = bG_Hypertext.getElements();
							    while (elements.hasMoreElements()) {
								      AbstractButton button = (AbstractButton)elements.nextElement();
								      if (button.isSelected()) {
								        if(button.getText()=="Without hypertext" && modeDim==1)//2d+without are selected
								        {								        	
								        	modeHyper=1;
								        	System.out.println("2D+ Without Hypertext are selected");
								        }
								        else if(button.getText()=="Without hypertext" && modeDim==2)//3d+without are selected
								        {
								        	modeHyper=2;
								        	System.out.println("3D+ Without Hypertext are selected");
								        }
								        else if(button.getText()!="Without hypertext" && modeDim==1)//2d+with are selected
								        {								        	
								        	modeHyper=3;
								        	System.out.println("2D+ With Hypertext are selected");
								        }
								        else if(button.getText()!="Without hypertext" && modeDim==2)//3d+with are selected
								        {
								        	modeHyper=4;
								        	System.out.println("3D + With Hypertext are selected");
								        }
								        	
								      }
								    }
									//String auth=Jtable1.getValueAt(i, 0).toString();
									//String number=Jtable1.getValueAt(i, 1).toString();
									//String kind=Jtable1.getValueAt(i, 2).toString();
									c.setMode(modeHyper);
									SettingsRetrieval SR=new SettingsRetrieval();
									String workspacepath=SR.GetParam("WORKSPACE");
									String dataFilePath=workspacepath.replaceAll("\\\\", "/")+"/out/data.dat";		
									System.out.println(dataFilePath);
									c.setDataFilePath(dataFilePath);									
									c.serLayerNumber(txtLayer.getText());
									c.setTimeout(Integer.parseInt(txtSeconds.getText()));
						
							}
							
						try {
							c.drawToGnuplot();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						}
					}
				});
				btnRefresh.setBounds(425, 341, 136, 39);
				
				JLabel lblDisconnectQueryAfter = new JLabel("Disconnect query after: ");
				panel_2.add(lblDisconnectQueryAfter);
				
				txtSeconds = new JTextField();
				txtSeconds.setText("30");
				panel_2.add(txtSeconds);
				txtSeconds.setColumns(5);
				
				JLabel lblSeconds = new JLabel("Seconds");
				panel_2.add(lblSeconds);
				
				JLabel lblActiveLayerd = new JLabel("Active Layer Numr (3D):");
				panel_2.add(lblActiveLayerd);
				
				txtLayer = new JTextField();
				txtLayer.setText("1");
				txtLayer.setColumns(5);
				panel_2.add(txtLayer);
				panel_2.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{lblDisconnectQueryAfter, txtSeconds, lblActiveLayerd}));
				
				JRadioButton radioNoHypertext = new JRadioButton("Without hypertext");
				radioNoHypertext.setSelected(true);
				radioNoHypertext.setMnemonic('y');
				radioNoHypertext.setHorizontalAlignment(SwingConstants.LEFT);
				radioNoHypertext.setVerticalAlignment(SwingConstants.TOP);
				panel_1.add(radioNoHypertext);
				bG_Hypertext.add(radioNoHypertext);
				
				JRadioButton radioWithHypertext = new JRadioButton("With hypertext (gnuplotv4.7 required)");
				radioWithHypertext.setMnemonic('n');
				panel_1.add(radioWithHypertext);
				bG_Hypertext.add(radioWithHypertext);
				radioWithHypertext.setHorizontalAlignment(SwingConstants.LEFT);
				
				JRadioButton radio2D = new JRadioButton("2D");
				panel.add(radio2D);
				bG_Dimension.add(radio2D);
				radio2D.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					
					}
				});
				radio2D.setSelected(true);
				radio2D.setHorizontalAlignment(SwingConstants.LEFT);
				
				JRadioButton radio3D = new JRadioButton("3D");
				panel.add(radio3D);
				bG_Dimension.add(radio3D);
				radio3D.setHorizontalAlignment(SwingConstants.LEFT);
				getContentPane().setLayout(null);
				getContentPane().add(menuBar);
				getContentPane().add(btnAddPatent);
				getContentPane().add(btnRmvPatent);
				getContentPane().add(scrollPane);
				getContentPane().add(btnGetCloud);
				getContentPane().add(btnRefresh);
				getContentPane().add(panel_1);
				getContentPane().add(panel);
				getContentPane().add(chkptnfam);
				getContentPane().add(panel_2);
				
				JButton btnGetLastPlot = new JButton("Get last plot as png file");
				btnGetLastPlot.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try{
						SettingsRetrieval SR=new SettingsRetrieval();
						String pngFilepath=SR.GetParam("WORKSPACE")+"/out/output.png";
						System.out.println("\""+pngFilepath+"\""); 
						String os= System.getProperty("os.name"); 
						ProcessBuilder pb;
						if(!os.matches("Mac OS X"))
						 pb = new ProcessBuilder("cmd", "/c","\""+pngFilepath+"\"");
						else
							 pb = new ProcessBuilder(new String[]{"open","-a" ,"preview",pngFilepath});
						Process p=pb.start();
			            p.waitFor(); 
			            BufferedReader reader=new BufferedReader(
			                new InputStreamReader(p.getInputStream())
			            ); 
			            String line=reader.readLine(); 
			            while(line!=null) 
			            { 
			                System.out.println(line); 
			                line=reader.readLine(); 
			            } 

			        }
			        catch(IOException e1) {} 
			        catch(InterruptedException e2) {} 

			        
					}
				});
				btnGetLastPlot.setBounds(318, 256, 163, 23);
				getContentPane().add(btnGetLastPlot);
	}
}
