package model.services;

import java.util.List;

import model.dao.CovidResultsDao;
import model.dao.DaoFactory;
import model.entities.CovidResults;

public class CovidResultsService {

	private CovidResultsDao dao = DaoFactory.createCovidResultsDao();

	public List<CovidResults> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(CovidResults obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(CovidResults obj) {
		dao.deleteById(obj.getId());
	}
}
