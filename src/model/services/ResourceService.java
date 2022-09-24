package model.services;

import java.util.List;

import model.dao.ResourceDao;
import model.dao.DaoFactory;
import model.entities.Resource;

public class ResourceService {

	private ResourceDao dao = DaoFactory.createResourceDao();

	public List<Resource> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Resource obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(Resource obj) {
		dao.deleteById(obj.getId());
	}
}
