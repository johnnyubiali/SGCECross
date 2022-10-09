package model.services;

import java.util.List;

import model.dao.AnswerDao;
import model.dao.DaoFactory;
import model.entities.Answer;

public class AnswerService {

	private AnswerDao dao = DaoFactory.createAnswerDao();

	public List<Answer> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Answer obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(Answer obj) {
		dao.deleteById(obj.getId());
	}
}
