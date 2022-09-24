package model.services;

import java.util.List;

import model.dao.VentilationTypeDao;
import model.dao.DaoFactory;
import model.entities.VentilationType;

public class VentilationTypeService {

	private VentilationTypeDao dao = DaoFactory.createVentilationTypeDao();

	public List<VentilationType> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(VentilationType obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(VentilationType obj) {
		dao.deleteById(obj.getId());
	}
}
