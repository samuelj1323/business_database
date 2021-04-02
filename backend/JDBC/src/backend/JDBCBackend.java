package backend;

import java.sql.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class JDBCBackend {
	private Connection conn;
	
	private Statement stmt;
	private ResultSet rs;
	private ResultSetMetaData rsmd;
	
	public JDBCBackend() throws SQLException {
		String url = "jdbc:postgresql://54.237.167.5:5432/db905_group3_project2";
		conn = DriverManager.getConnection(url, DBSetUp.USR, DBSetUp.PSWD);

		stmt = null;
		rs = null;
		rsmd = null;
	}
	
	public void executeQuery(JDBCQuery query) throws InputException, SQLException {
		stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs = stmt.executeQuery(query.toQueryString());
	}

	public void executeExact(String query) throws SQLException {
		stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		stmt.execute(query);
	}

	public void executeExactQuery(String query) throws SQLException {
		stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs = stmt.executeQuery(query);
	}
	
	public String getOutput(String filename) throws SQLException, IOException {
		rs.last();
		int rows = rs.getRow()+1;
		rs.beforeFirst();
		
		rsmd = rs.getMetaData();
		int cols = rsmd.getColumnCount();
		
		String[][] outputs = new String[rows][cols];
		
		for (int j = 1; j <= rsmd.getColumnCount(); j++) {
			outputs[0][j-1] = rsmd.getColumnName(j);
		}
		
		int i = 0;
		while (rs.next()) {
			for (int j = 1; j <= rsmd.getColumnCount(); j++) {
				outputs[i+1][j-1] = rs.getString(j);
			}
			i++;
		}
		
		if (!filename.equals("")) {
			formatOutputFile(outputs, rows, cols, filename);
			return filename;
		}
		return formatOutputDisplay(outputs, rows, cols);
	}
	
	private String formatOutputDisplay(String[][] arr, int r, int c) {
		StringBuilder ret = new StringBuilder();
		ret.append("<html><table border=1>");
		
		ret.append("<tr>");
		for (int j = 0; j < c; j++) {
			ret.append("<th align=\'center\'>" + arr[0][j] + "</th>");
		}
		ret.append("</tr>");
		
		for (int i = 1; i < r; i++) {
			ret.append("<tr>");
			for (int j = 0; j < c; j++) {
				String align = isNumeric(arr[0][j]);
				ret.append("<td align=" + align + ">" + arr[i][j] + "</td>");
			}
			ret.append("</tr>");
		}
		
		ret.append("</table></html>");
		return ret.toString();
	}
	
	private void formatOutputFile(String[][] arr, int r, int c, String filename) throws IOException {
		File file = new File(filename);
		if (file.getParentFile() != null) {
			file.getParentFile().mkdirs();
		}

		try (BufferedWriter filewriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
			for (int j = 0; j < c; j++) {
				filewriter.write(arr[0][j]);
				if (j < c-1) {
					filewriter.write(",");
				}
			}
			filewriter.newLine();

			for (int i = 1; i < r; i++) {
				for (int j = 0; j < c; j++) {
					filewriter.write(arr[i][j]);
					if (j < c-1) {
						filewriter.write(",");
					}
				}
				filewriter.newLine();
			}
		}
	}
	
	private String isNumeric(String colName) {
		if (colName.contains("id") || colName.contains("lat") || colName.contains("long")
			|| colName.contains("reviewcount") || colName.contains("rating") || colName.contains("recv")
			|| colName.contains("current") || colName.contains("date")) {
			return "\'right\'";
		}
		return "\'left\'";
	}
	
	public void close() throws SQLException {
		conn.close();
	}
}
