package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.RegisterDao;
import model.entities.Register;

public class RegisterService {
	private RegisterDao dao = DaoFactory.createRegisterDao();

	public List<Register> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Register obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(Register obj) {
		dao.deleteById(obj.getId());
	}
}