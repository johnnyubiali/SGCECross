package model.dao;

import java.util.List;

import model.entities.Answer;

public interface AnswerDao {

	void insert(Answer obj);
	void update(Answer obj);
	void deleteById (Integer id);
	Answer findById(Integer id);
	List<Answer> findAll();
}
