package model.dao;

import db.DB;
import model.dao.impl.CovidResultsDaoJDBC;
import model.dao.impl.PacientDaoJDBC;
import model.dao.impl.RegisterDaoJDBC;
import model.dao.impl.ResourceDaoJDBC;

public class DaoFactory {

	public static PacientDao createPacientDao(){
		return new PacientDaoJDBC(DB.getConnection());
	}

	public static RegisterDao createRegisterDao() {
		return new RegisterDaoJDBC(DB.getConnection());
	}

	public static CovidResultsDao createCovidResultsDao() {
		return new CovidResultsDaoJDBC(DB.getConnection());
	}
	
	public static ResourceDao createResourceDao(){
		return new ResourceDaoJDBC(DB.getConnection());
	}
	
}
