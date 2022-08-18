package model.dao;

import java.util.List;

import model.entities.CovidResults;
import model.entities.Pacient;


public interface PacientDao {
	
	void insert(Pacient obj);
	void update(Pacient obj);
	void deleteById (Integer id);
	Pacient findById(Integer id);
	List<Pacient> findAll();
	List<Pacient> findByCovidResults(CovidResults covidresults);

}
