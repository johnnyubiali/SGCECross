package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.EmployeeDao;
import model.entities.Employee;

public class EmployeeDaoJDBC implements EmployeeDao {

	private Connection conn;

	public EmployeeDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Employee obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO employee " 
										+ "(Name, Office) " 
										+ "VALUES " 
										+ "(?, ?)",
										Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());
			st.setString(2, obj.getOffice());
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
	public void update(Employee obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE employee " + "SET Name = ?, Office = ?" + "WHERE Id = ? ");

			st.setString(1, obj.getName());
			st.setString(2, obj.getOffice());
			st.setInt(3, obj.getId());

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
			st = conn.prepareStatement("DELETE FROM employee WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Employee findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM employee WHERE Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Employee obj = instantiateEmployee(rs);
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

	private Employee instantiateEmployee(ResultSet rs) throws SQLException {
		Employee obj = new Employee();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setOffice(rs.getString("Office"));
		return obj;
	}

	@Override
	public List<Employee> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM employee ORDER BY name");

			rs = st.executeQuery();

			List<Employee> list = new ArrayList<>();

			while (rs.next()) {
				Employee obj = instantiateEmployee(rs);
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
