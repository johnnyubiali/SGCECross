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
import model.dao.DestinationDao;
import model.entities.Destination;

public class DestinationDaoJDBC implements DestinationDao {

	private Connection conn;

	public DestinationDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Destination obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO destination " 
										+ "(Destiny) " 
										+ "VALUES " 
										+ "(?)",
										Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getDestiny());
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
	public void update(Destination obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE destination " + "SET Destiny = ?" + "WHERE Id = ? ");

			st.setString(1, obj.getDestiny());
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
			st = conn.prepareStatement("DELETE FROM destination WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Destination findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM destination WHERE Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Destination obj = instantiateDestination(rs);
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

	private Destination instantiateDestination(ResultSet rs) throws SQLException {
		Destination obj = new Destination();
		obj.setId(rs.getInt("Id"));
		obj.setDestiny(rs.getString("Destiny"));
		return obj;
	}

	@Override
	public List<Destination> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM destination ORDER BY Destiny");

			rs = st.executeQuery();

			List<Destination> list = new ArrayList<>();

			while (rs.next()) {
				Destination obj = instantiateDestination(rs);
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
