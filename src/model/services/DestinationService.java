package model.services;

import java.util.List;

import model.dao.DestinationDao;
import model.dao.DaoFactory;
import model.entities.Destination;

public class DestinationService {

	private DestinationDao dao = DaoFactory.createDestinationDao();

	public List<Destination> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Destination obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(Destination obj) {
		dao.deleteById(obj.getId());
	}
}
