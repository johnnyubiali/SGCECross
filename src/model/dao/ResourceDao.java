package model.dao;

import java.util.List;

import model.entities.Resource;

public interface ResourceDao {

	void insert(Resource obj);
	void update(Resource obj);
	void deleteById (Integer id);
	Resource findById(Integer id);
	List<Resource> findAll();
}
