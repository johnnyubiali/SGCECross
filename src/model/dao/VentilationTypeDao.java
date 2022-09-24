package model.dao;

import java.util.List;

import model.entities.VentilationType;

public interface VentilationTypeDao {

	void insert(VentilationType obj);
	void update(VentilationType obj);
	void deleteById (Integer id);
	VentilationType findById(Integer id);
	List<VentilationType> findAll();
}
