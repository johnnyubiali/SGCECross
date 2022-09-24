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
import model.dao.VentilationTypeDao;
import model.entities.VentilationType;

public class VentilationTypeDaoJDBC implements VentilationTypeDao {

	private Connection conn;

	public VentilationTypeDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(VentilationType obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO ventilationtype " 
										+ "(Description) " 
										+ "VALUES " 
										+ "(?)",
										Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getDescription());
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
	public void update(VentilationType obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE ventilationtype " + "SET Description = ?" + "WHERE Id = ? ");

			st.setString(1, obj.getDescription());
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
			st = conn.prepareStatement("DELETE FROM ventilationtype WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public VentilationType findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM ventilationtype WHERE Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				VentilationType obj = instantiateVentilationType(rs);
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

	private VentilationType instantiateVentilationType(ResultSet rs) throws SQLException {
		VentilationType obj = new VentilationType();
		obj.setId(rs.getInt("Id"));
		obj.setDescription(rs.getString("Description"));
		return obj;
	}

	@Override
	public List<VentilationType> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM ventilationtype ORDER BY Description");

			rs = st.executeQuery();

			List<VentilationType> list = new ArrayList<>();

			while (rs.next()) {
				VentilationType obj = instantiateVentilationType(rs);
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
