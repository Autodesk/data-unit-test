package com.autodesk.adp.validation_framework.tests;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp.BasicDataSource;

public class Test {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		String db_url = "jdbc:hive2://fastaccess-dev.api.autodesk.com:10008/;ssl=true;sslTrustStore=/usr/lib/jvm/java-8-oracle/jre/lib/security/cacerts;trustStorePassword=changeit";
		String user = "svc_p_adpcompute";
		String password = "eY8#xS8@";
		//Class.forName("org.apache.hive.jdbc.HiveDriver");
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("org.apache.hive.jdbc.HiveDriver");
		ds.setUrl(db_url);
		ds.setUsername(user);
		ds.setPassword(password);
		Connection con = ds.getConnection();
		Statement stmt = con.createStatement();
		ResultSet res = stmt.executeQuery("show databases");
		if (res.next()) {
			System.out.println(res.getString(1));
		}
		res.close();
		stmt.close();
		con.close();
	}

}
