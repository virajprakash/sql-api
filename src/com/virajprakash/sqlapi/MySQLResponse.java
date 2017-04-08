package com.virajprakash.sqlapi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MySQLResponse {
	private PreparedStatement statement;
	private ResultSet set;
	private boolean exists;

	public MySQLResponse(PreparedStatement statement) {
		this(statement, null);
	}

	public MySQLResponse(PreparedStatement statement, ResultSet set) {
		this(statement, set, false);
	}

	public MySQLResponse(PreparedStatement statement, ResultSet set, boolean exists) {
		this.statement = statement;
		this.set = set;
		this.exists = exists;
	}

	public boolean exists() {
		return this.exists;
	}

	public ResultSet getSet() {
		return this.set;
	}

	public PreparedStatement getStatement() {
		return this.statement;
	}

	public void closeAll() {
		try {
			if (this.statement != null) this.statement.close();
			if (this.set != null) this.set.close();
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}
}