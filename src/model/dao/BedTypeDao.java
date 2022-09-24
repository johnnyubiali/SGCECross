package model.dao;

import java.util.List;

import model.entities.BedType;

public interface BedTypeDao {

	void insert(BedType obj);
	void update(BedType obj);
	void deleteById (Integer id);
	BedType findById(Integer id);
	List<BedType> findAll();
}
