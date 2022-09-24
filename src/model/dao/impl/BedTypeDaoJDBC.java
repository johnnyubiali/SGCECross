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
import model.dao.BedTypeDao;
import model.entities.BedType;

public class BedTypeDaoJDBC implements BedTypeDao {

	private Connection conn;

	public BedTypeDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(BedType obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO bedtype " 
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
	public void update(BedType obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE bedtype " + "SET Description = ?" + "WHERE Id = ? ");

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
			st = conn.prepareStatement("DELETE FROM bedtype WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public BedType findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM bedtype WHERE Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				BedType obj = instantiateBedType(rs);
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

	private BedType instantiateBedType(ResultSet rs) throws SQLException {
		BedType obj = new BedType();
		obj.setId(rs.getInt("Id"));
		obj.setDescription(rs.getString("Description"));
		return obj;
	}

	@Override
	public List<BedType> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM bedtype ORDER BY Description");

			rs = st.executeQuery();

			List<BedType> list = new ArrayList<>();

			while (rs.next()) {
				BedType obj = instantiateBedType(rs);
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
