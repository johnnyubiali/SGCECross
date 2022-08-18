package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.PacientDao;
import model.entities.CovidResults;
import model.entities.Pacient;

public class PacientDaoJDBC implements PacientDao {

	private Connection conn;

	public PacientDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Pacient obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO pacient " + "(RegistSigs, Name, Age, IdCovidResults) " + "VALUES " + "(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, obj.getRegistSigs());
			st.setString(2, obj.getName());
			st.setInt(3, obj.getAge());
			st.setInt(4, obj.getCovidResults().getId());

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
	public void update(Pacient obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE pacient " + "SET RegistSigs = ?, Name = ?, Age = ?, IdCovidResults = ?" + "WHERE Id = ? ");

			st.setInt(1, obj.getRegistSigs());
			st.setString(2, obj.getName());
			st.setInt(3, obj.getAge());
			st.setInt(4, obj.getCovidResults().getId());
			st.setInt(5, obj.getId());

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
			st = conn.prepareStatement("DELETE FROM pacient WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Pacient findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT pacient.*,covidresults.Results as ResultCovid "
					+"FROM pacient INNER JOIN covidresults "
					+"ON pacient.IdCovidResults = covidresults.Id "
					+"WHERE pacient.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				CovidResults cr = instatiateCovidResults(rs);
				Pacient obj = instantiatePacient(rs, cr);
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

	private Pacient instantiatePacient(ResultSet rs, CovidResults cr) throws SQLException {
		Pacient obj = new Pacient();
		obj.setId(rs.getInt("Id"));
		obj.setRegistSigs(rs.getInt("RegistSigs"));
		obj.setName(rs.getString("Name"));
		obj.setAge(rs.getInt("Age"));
		obj.setCovidResults(cr);
		return obj;
	}
	
	private CovidResults instatiateCovidResults(ResultSet rs) throws SQLException {
		CovidResults cr = new CovidResults();
		cr.setId(rs.getInt("IdCovidResults"));
		cr.setResults(rs.getString("Results"));
		return cr;
	}

	@Override
	public List<Pacient> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT pacient.*,covidresults.Results as ResultCovid "
							+"FROM pacient INNER JOIN covidresults "
							+"ON pacient.IdCovidResults = covidresults.Id "
							+"ORDER BY Name");

			rs = st.executeQuery();

			List<Pacient> list = new ArrayList<>();
			Map<Integer, CovidResults> map = new HashMap<>();
			
			while (rs.next()) {
				
				CovidResults cr = map.get(rs.getInt("IdCovidResults"));
				
				if(cr == null) {
					cr = instatiateCovidResults(rs);
					map.put(rs.getInt("IdCovidResults"), cr);
				}
				
				
				Pacient obj = instantiatePacient(rs, cr);
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
	
	@Override
	public List<Pacient> findByCovidResults(CovidResults covidresults) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT pacient.*,covidresults.Results as CovidResult "
					+"FROM pacient INNER JOIN covidresults "
					+"ON pacient.IdCovidResults = covidresults.Id "
					+"WHERE IdCovidResults = ? "
					+"ORDER BY Name");
			
			st.setInt(1, covidresults.getId());
			rs = st.executeQuery();
			
			List<Pacient> list = new ArrayList<>();
			Map<Integer, CovidResults> map = new HashMap<>();
			
			while (rs.next()) {
				
				CovidResults cr = map.get(rs.getInt("IdCovidResults"));
				
				if(cr == null) {
					cr = instatiateCovidResults(rs);
					map.put(rs.getInt("IdCovidResults"), cr);
				}
				
				
				Pacient obj = instantiatePacient(rs, cr);
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
