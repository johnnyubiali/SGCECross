package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.UsersDao;
import model.entities.Users;

public class UsersDaoJDBC implements UsersDao {

	private Connection conn;

	public UsersDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Users obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO users " + "(user, password) " + "VALUES " + "(?, ?)");

			st.setString(1, obj.getUsername());
			st.setString(2, obj.getPassword());
			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Erro ao inserir dados! Nenhuma linha foi afetada!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void update(Users obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE users " + "SET Username = ?, Password = ?" + "WHERE Id = ? ");

			st.setString(1, obj.getUsername());
			st.setString(2, obj.getPassword());
			st.setInt(4, obj.getId());

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM users WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Users findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM users WHERE Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Users obj = instantiateUsers(rs);
				return obj;

			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Users instantiateUsers(ResultSet rs) throws SQLException {
		Users obj = new Users();
		obj.setId(rs.getInt("Id"));
		obj.setUsername(rs.getString("Username"));
		obj.setPassword(rs.getString("Password"));
		return obj;
	}

	@Override
	public List<Users> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM users" + "ORDER BY Name");

			rs = st.executeQuery();

			List<Users> list = new ArrayList<>();

			while (rs.next()) {
				Users obj = instantiateUsers(rs);
				list.add(obj);
				
			}
			return list;  
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
