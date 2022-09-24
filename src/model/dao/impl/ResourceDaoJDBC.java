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
import model.dao.ResourceDao;
import model.entities.Resource;

public class ResourceDaoJDBC implements ResourceDao {

	private Connection conn;

	public ResourceDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Resource obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO resources " 
										+ "(Specialty) " 
										+ "VALUES " 
										+ "(?)",
										Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getSpecialty());
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
	public void update(Resource obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE resources " + "SET Specialty = ?" + "WHERE Id = ? ");

			st.setString(1, obj.getSpecialty());
			st.setInt(2, obj.getId());

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
			st = conn.prepareStatement("DELETE FROM resources WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Resource findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM resources WHERE Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Resource obj = instantiateResource(rs);
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

	private Resource instantiateResource(ResultSet rs) throws SQLException {
		Resource obj = new Resource();
		obj.setId(rs.getInt("Id"));
		obj.setSpecialty(rs.getString("Specialty"));
		return obj;
	}

	@Override
	public List<Resource> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM resources ORDER BY Specialty");

			rs = st.executeQuery();

			List<Resource> list = new ArrayList<>();

			while (rs.next()) {
				Resource obj = instantiateResource(rs);
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
