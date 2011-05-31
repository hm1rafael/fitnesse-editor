package fitedit.resource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fitedit.utils.DBUtil;

public class ResourceDatabase {
	private static ResourceDatabase instance;
	
	private DBUtil db;
	
	private ResourceDatabase(){
		try {
			db = new DBUtil(DriverManager.getConnection("jdbc:hsqldb:file:fitdb;shutdown=true", "sa", ""));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ResourceDatabase getInstance(){
		if (instance == null) {
			instance = new ResourceDatabase();
		}
		return instance;
	}
	
	public void init(){
		db.execute("create table FitTest ( name varchar(100), path varchar(1000) )");
		db.execute("create index IDX1 on FitTest ( name )");
		db.execute("create index IDX2 on FitTest ( path )");
	}
	
	public void add(String name, String path){
		db.execute("insert into FitTest (name, path) values (?, ?)", name, path);
		
		System.out.println(get("%"));
	}
	
	public void delete(String path){
		db.execute("delete from FitTest where path = ?", path);
		
		System.out.println(get("%"));
	}
	
	public List<Map<String, Object>> get(String pattern) {
		if (pattern == null) 
			return new ArrayList<Map<String,Object>>();
		
		pattern = pattern.replaceAll("_", "\\_");
		pattern.replaceAll("\\*", "%");
		pattern.replaceAll("\\?", "_");
		return db.select("select * from FitTest where name like ?", pattern);
	}
	
}
