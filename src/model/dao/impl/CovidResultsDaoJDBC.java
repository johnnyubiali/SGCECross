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
import model.dao.CovidResultsDao;
import model.entities.CovidResults;

public class CovidResultsDaoJDBC implements CovidResultsDao {

	private Connection conn;

	public CovidResultsDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(CovidResults obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO covidresults " 
										+ "(Results) " 
										+ "VALUES " 
										+ "(?)",
										Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getResults());
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
	public void update(CovidResults obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE covidresults " + "SET Results = ?" + "WHERE Id = ? ");

			st.setString(1, obj.getResults());
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
			st = conn.prepareStatement("DELETE FROM covidresults WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public CovidResults findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM covidresults WHERE Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				CovidResults obj = instantiateCovidResults(rs);
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

	private CovidResults instantiateCovidResults(ResultSet rs) throws SQLException {
		CovidResults obj = new CovidResults();
		obj.setId(rs.getInt("Id"));
		obj.setResults(rs.getString("Results"));
		return obj;
	}

	@Override
	public List<CovidResults> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM covidresults ORDER BY Results");

			rs = st.executeQuery();

			List<CovidResults> list = new ArrayList<>();

			while (rs.next()) {
				CovidResults obj = instantiateCovidResults(rs);
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
