package com.virajprakash.sqlapi;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class MySQL implements Cloneable {

	private static final String INSERT = "INSERT INTO :table VALUES(:total);";
	private static final String SELECT = "SELECT :fields FROM :table WHERE :values;";
	private static final String SELECT_ALL = "SELECT :fields FROM :table;";
	private static final String UPDATE = "UPDATE :table SET :fields WHERE :values;";
	private static final String DELETE = "DELETE FROM :table WHERE :values;";

	//MySQL Credentials
	private String server;
	private String port;
	private String database;
	private String user;
	private String pass;
	private Connection sqlConnection;

	public MySQL(String server, String port, String database, String user, String pass) {
		this.server = server;
		this.port = port;
		this.database = database;
		this.user = user;
		this.pass = pass;
	}

	public Connection getConnection() {
		try {
			//If the connection is closed then reconnect
			if(this.sqlConnection.isClosed()) {
				this.connect();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this.sqlConnection;
	}

	/**
	 * Attempts connection to MySQL Database
	 * @return Returns true if the connection was successful
	 */
	public boolean connect() {
		System.out.println("Attempting connection to " + "jdbc:mysql://" + server + ":" + port + "/" + database);
		try {
			this.sqlConnection = DriverManager.getConnection("jdbc:mysql://" + server + ":" + port + "/" + database, user, pass);
			return true;
		} catch (SQLException e) {
			System.out.println("Connection failed.");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Executes a query
	 * @param query Query to execute
	 * @param sets Parameters for preparedstatement
	 * @return Server response
	 */
	public MySQLResponse executeQuery(String query, Object... sets) {
		try {
			if (this.sqlConnection.isClosed()) {
				this.connect();
			}
		} catch (SQLException localException) {
		}
		try {
			PreparedStatement statement = this.sqlConnection.prepareStatement(query);

			int index = 1;
			if (sets != null) {
				for (Object obj : sets) {
					statement.setObject(index, obj);
					index++;
				}
			}

			return new MySQLResponse(statement, statement.executeQuery());
		} catch (SQLException localException) {
			localException.printStackTrace();

			return null;
		}
	}

	/**
	 * Executes an SQL statement
	 * @param statement Statement to execute
	 * @param sets Parameters for PreparedStatement
	 * @return Server response
	 */
	public MySQLResponse execute(String statement, Object... sets) {
		try {
			if (this.sqlConnection.isClosed()) {
				this.connect();
			}
		} catch (SQLException localException) {
		}
		try {
			PreparedStatement preparedStatement = this.sqlConnection.prepareStatement(statement);

			int index = 1;
			if (sets != null) for (Object obj : sets) {
				preparedStatement.setObject(index, obj);

				index++;
			}

			preparedStatement.execute();

			return new MySQLResponse(preparedStatement);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Executes an SQL update
	 * @param update Statement to execute
	 * @param sets Parameters for PreparedStatement
	 * @return Server response
	 */
	public MySQLResponse executeUpdate(String update, Object... sets) {
		try {
			if (this.sqlConnection.isClosed()) {
				this.connect();
			}
		} catch (SQLException e) {

		}

		try {
			PreparedStatement statement = this.sqlConnection.prepareStatement(update);

			int index = 1;
			if (sets != null) {
				for (Object obj : sets) {
					statement.setObject(index, obj);
					index++;
				}
			}

			statement.executeUpdate();

			return new MySQLResponse(statement);
		} catch (SQLException e) {
			e.printStackTrace();

			return null;
		}
	}

	public MySQLResponse exists(String field, String table, String fieldName, String value) {
		try {
			if (this.sqlConnection.isClosed()) {
				this.connect();
			}
		} catch (SQLException e) {

		}
		PreparedStatement statement = null;
		try {
			statement = this.sqlConnection.prepareStatement(select(Arrays.asList(field), table, fieldName));
			statement.setString(1, value);
			ResultSet set = statement.executeQuery();

			return new MySQLResponse(statement, set, set.next());
		} catch (SQLException localException) {
			localException.printStackTrace();

			return new MySQLResponse(statement, null, false);
		}
	}

	public String update(String table, List<String> fields, String... values) {
		String fieldString = "";
		int index = 0;
		for (String field : fields) {
			if (index == 0) fieldString += field + "=?";
			else fieldString += ", " + field + "=?";
			index += 1;
		}

		StringBuilder formattedValues = new StringBuilder();
		for (int x = 0; x<values.length; x++) {
			if (x != 0) {
				formattedValues.append(" AND ");
			}
			formattedValues.append(values[x] + "=?");
		}
		String update = UPDATE.replace(":table", table).replace(":fields", fieldString).replace(":values", formattedValues.toString());
		System.out.println(update);
		return update;
	}

	public String insert(String table, int unknowns) {
		String total = "";

		for (int i = 0; i < unknowns; i++) {
			if (i == 0) total += "?";
			else total += ", ?";
		}

		System.out.println(INSERT.replace(":table", table).replace(":total", total));

		return INSERT.replace(":table", table).replace(":total", total);
	}

	public String select(List<String> fields, String table, String... values) {
		StringBuilder formattedFields = new StringBuilder();
		for (int x = 0; x<fields.size(); x++) {
			if (x != 0) {
				formattedFields.append(", ");
			}
			formattedFields.append(fields.get(x));
		}
		StringBuilder formattedValues = new StringBuilder();
		for (int x = 0; x<values.length; x++) {
			if (x != 0) {
				formattedValues.append(" AND ");
			}
			formattedValues.append(values[x] + "=?");
		}
		String query = SELECT.replace(":fields", formattedFields.toString()).replace(":table", table).replace(":values", formattedValues.toString());
		System.out.println(query);
		return query;
	}

	public String selectAll(String table, String... fields) {
		StringBuilder formattedFields = new StringBuilder();
		for (int x = 0; x<fields.length; x++) {
			if (x != 0) {
				formattedFields.append(" ");
			}
			formattedFields.append(fields[x]);
		}
		String query = SELECT_ALL.replace(":table", table).replace(":fields", formattedFields.toString().replace(" ", ", "));
		System.out.println(query);
		return query;
	}

	public String delete(String table, String... values) {
		StringBuilder formattedValues = new StringBuilder();
		for (int x = 0; x<values.length; x++) {
			if (x != 0) {
				formattedValues.append(" AND ");
			}
			formattedValues.append(values[x] + "=?");
		}
		String update = DELETE.replace(":table", table).replace(":values", formattedValues.toString());
		System.out.println(update);
		return update;
	}

}