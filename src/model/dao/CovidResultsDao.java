package model.dao;

import java.util.List;

import model.entities.CovidResults;


public interface CovidResultsDao {
	
	void insert(CovidResults obj);
	void update(CovidResults obj);
	void deleteById (Integer id);
	CovidResults findById(Integer id);
	List<CovidResults> findAll();

}
