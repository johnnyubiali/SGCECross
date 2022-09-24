package model.services;

import java.util.List;

import model.dao.BedTypeDao;
import model.dao.DaoFactory;
import model.entities.BedType;

public class BedTypeService {

	private BedTypeDao dao = DaoFactory.createBedTypeDao();

	public List<BedType> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(BedType obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(BedType obj) {
		dao.deleteById(obj.getId());
	}
}
