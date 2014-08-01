package Classes;

import java.sql.*;
import java.util.Scanner;
import java.io.*;

import javax.swing.JOptionPane;

public class ConnectToDatabase {

	String url;
	String uName;
	String uPass;
	int _mode;
	int timeOut;
	String layer;
	String tmpFileName = "out/temp.txt";
	String dataFilepath;

	public void setMode(int m) {
		_mode = m;
	}

	public void setTimeout(int seconds) {
		timeOut = seconds;
	}

	public void serLayerNumber(String layerNumber) {
		layer = layerNumber;
	}

	public void setDataFilePath(String path) {
		dataFilepath = path;
	}

	public ConnectToDatabase(String vHostName, String vPort, String vUsername,
			String vPassword) {
		this.url = "jdbc:mysql://" + vHostName + ":" + vPort + "/";
		this.uName = vUsername;
		this.uPass = vPassword;
	}

	public boolean connect() {
		try {
			Connection conn = DriverManager.getConnection(url, uName, uPass);
			conn.isValid(10);
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		}

	}

	public void runStoredProcedure(String auth, String publnr, String kind,
			String withFamYN) {

		CallableStatement cstmt = null;
		try {
			Connection conn = DriverManager.getConnection(url, uName, uPass);
			String SQL = "{call temp.Start_Procedure('" + auth + "','" + publnr
					+ "','" + kind + "','" + withFamYN + "')}";
			System.out.println(SQL);
			cstmt = conn.prepareCall(SQL);
			cstmt.setQueryTimeout(timeOut);
			cstmt.executeQuery();
		} catch (SQLException e) {

		} finally {
			// cstmt.close();
		}

	}

	private ResultSet getCitations() {

		try {
			Connection conn = DriverManager.getConnection(url, uName, uPass);
			Statement stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery("select * from temp.a_citations");
			return rs;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return null;

	}

	public void getPoints(int layerNumber) {
		try {

			String content = "";
			ResultSet rs = getCitations();
			int numberOfRecords = 0;
			while (rs.next()) {
				Date pubDate = rs.getDate("publn_date");
				int lvl = rs.getInt("pub_lvl");
				int cited_count = rs.getInt("cited_count");
				int occu = rs.getInt("occu");
				String patent = rs.getString("publn_auth") + " "
						+ rs.getString("publn_nr") + " "
						+ rs.getString("publn_kind");
				content += pubDate + "," + cited_count + "," + lvl + "," + occu
						+ "," + patent + "," + layerNumber + "\n";
				numberOfRecords++;
			}
			WriteToFile(content);
			if (numberOfRecords == 0) {
				JOptionPane.showMessageDialog(null,
						"Data File returns no records!");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	boolean accessed = false;

	private void WriteToFile(String content) {
		try {
			File file = new File("out/data.dat");

			if (!file.exists()) {
				file.createNewFile();
				accessed = true;
			}

			if (file.exists() && accessed == false) {
				file.delete();
				file.createNewFile();
				accessed = true;
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void drawToGnuplot() throws Exception {
		readFile(dataFilepath);
		//sudo port install gnuplot +wxwidgets to install this variant, but be careful it may conflict with wxwidgets_devel.
		try {
			SettingsRetrieval SR = new SettingsRetrieval();
			String workspacepath = SR.GetParam("WORKSPACE");

			String tempFilePath = workspacepath.replaceAll("\\\\", "/")
					+ "/out/temp.txt";			
			String[] command={};
			File f = new File("/opt/local/bin/gnuplot");
			if(f.exists()) {//some gnuplot versions use this path
				command=new String[]{"/opt/local/bin/gnuplot",tempFilePath};
			}
			else
			{
				f = new File("/usr/local/bin/gnuplot");
				if(f.exists()) {//other gnuplot versions use this path
					command=new String[]{"/usr/local/bin/gnuplot",tempFilePath};
				}
				else//for windows
				{
					 command=new String[]{"gnuplot",tempFilePath};
				}
			}
			ProcessBuilder pb = new ProcessBuilder(command);
			pb.start();

		} catch (IOException e1) {
			throw new Exception("Exception: "+e1.getMessage());
		} 
		System.out.println("Done");
	}

	public void replace(String oldFileName, String newFileName,
			String oldValue, String newValue) {

		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(new FileReader(oldFileName));
			bw = new BufferedWriter(new FileWriter(newFileName));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains(oldValue))
					line = line.replace(oldValue, newValue);
				bw.write(line + "\n");
			}
		} catch (Exception e) {
			return;
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				//
			}
			try {
				if (bw != null)
					bw.close();
			} catch (IOException e) {
				//
			}
		}

	}

	private void readFile(String dataFilePath) {
		String content = "";
		try {
			SettingsRetrieval SR = new SettingsRetrieval();
			String dataFile = "filename='" + dataFilePath + "'";
			String pngFilename = "set output '" + SR.GetParam("WORKSPACE")
					+ "/out/output.png'";
			String templateFilename = "";
			if (_mode == 1)// 2d+without are selected
			{
				templateFilename = "out/2D_NO_HYPERTEXT.txt";
				replace(templateFilename, tmpFileName + "_", "filename=",
						dataFile);
				replace(tmpFileName + "_", tmpFileName, "set output ",
						pngFilename);
				content = new Scanner(new File(tmpFileName))
						.useDelimiter("\\Z").next();
			} else if (_mode == 2)// 3d+without are selected
			{
				templateFilename = "out/3D_NO_HYPERTEXT.txt";
				replace(templateFilename, "out/temp3D.txt", "filename=",
						dataFile);
				replace("out/temp3D.txt", tmpFileName + "_", "layer=", "layer="
						+ layer);
				replace(tmpFileName + "_", tmpFileName, "set output ",
						pngFilename);
				content = new Scanner(new File(tmpFileName))
						.useDelimiter("\\Z").next();
			}
			if (_mode == 3)// 2d+with are selected
			{
				templateFilename = "out/2D_WITH_HYPERTEXT.txt";
				replace(templateFilename, tmpFileName + "_", "filename=",
						dataFile);
				replace(tmpFileName + "_", tmpFileName, "set output ",
						pngFilename);
				content = new Scanner(new File(tmpFileName))
						.useDelimiter("\\Z").next();
			}
			if (_mode == 4)// 3d+with are selected
			{
				templateFilename = "out/3D_WITH_HYPERTEXT.txt";
				replace(templateFilename, "out/temp3D.txt", "filename=",
						dataFile);
				replace("out/temp3D.txt", tmpFileName + "_", "layer=", "layer="
						+ layer);
				replace(tmpFileName + "_", tmpFileName, "set output ",
						pngFilename);
				content = new Scanner(new File(tmpFileName))
						.useDelimiter("\\Z").next();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
