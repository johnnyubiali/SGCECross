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
import model.dao.AnswerDao;
import model.entities.Answer;

public class AnswerDaoJDBC implements AnswerDao {

	private Connection conn;

	public AnswerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Answer obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO answer " 
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
	public void update(Answer obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE answer " + "SET Description = ?" + "WHERE Id = ? ");

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
			st = conn.prepareStatement("DELETE FROM answer WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Answer findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM answer WHERE Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Answer obj = instantiateAnswer(rs);
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

	private Answer instantiateAnswer(ResultSet rs) throws SQLException {
		Answer obj = new Answer();
		obj.setId(rs.getInt("Id"));
		obj.setDescription(rs.getString("Description"));
		return obj;
	}

	@Override
	public List<Answer> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM answer ORDER BY Description");

			rs = st.executeQuery();

			List<Answer> list = new ArrayList<>();

			while (rs.next()) {
				Answer obj = instantiateAnswer(rs);
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
