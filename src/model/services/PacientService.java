package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.PacientDao;
import model.entities.Pacient;

public class PacientService {

	private PacientDao dao = DaoFactory.createPacientDao();

	public List<Pacient> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Pacient obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(Pacient obj) {
		dao.deleteById(obj.getId());
	}
}
