package model.dao;

import java.util.List;

import model.entities.Pacient;
import model.entities.Register;

public interface RegisterDao {

	void insert(Register obj);
	void update(Register obj);
	void deleteById (Integer id);
	Register findById(Integer id);
	List<Register> findAll();
	List<Register> findByPacient(Pacient pacient);
}
