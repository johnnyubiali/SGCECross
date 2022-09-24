package model.dao;

import java.util.List;

import model.entities.Destination;


public interface DestinationDao {
	
	void insert(Destination obj);
	void update(Destination obj);
	void deleteById (Integer id);
	Destination findById(Integer id);
	List<Destination> findAll();
}
