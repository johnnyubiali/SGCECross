package model.dao;

import db.DB;
import model.dao.impl.RegisterDaoJDBC;

public class DaoFactory {

	public static RegisterDao createRegisterDao(){
		return new RegisterDaoJDBC(DB.getConnection());
		
	}
}
