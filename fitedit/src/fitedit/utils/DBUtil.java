package fitedit.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtil {
	
	Connection con;
	
	public DBUtil(Connection con) {
		this.con = con;
	}
	
	public int execute(String sql, Object ... args) {
		System.out.println("executing ... [" + sql + "] with " + Arrays.asList(args));
		
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				pst.setObject(i+1, args[i]);
			}
			return pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
			
		return 0;
	}
	
	public List<Map<String, Object>> select(String sql, Object ... args) {
		System.out.println("selecting ... [" + sql + "] with " + Arrays.asList(args));
		
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				pst.setObject(i+1, args[i]);
			}
			
			rs = pst.executeQuery();
			ResultSetMetaData rsMetaData = rs.getMetaData();
			while(rs.next()){
				Map<String, Object> row = new HashMap<String, Object>();
				for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
					row.put(rsMetaData.getColumnName(i), rs.getObject(i));
				}
				result.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
			
		return result;
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}
	
	
}
