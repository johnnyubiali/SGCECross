package model.dao;

import db.DB;
import model.dao.impl.AnswerDaoJDBC;
import model.dao.impl.BedTypeDaoJDBC;
import model.dao.impl.CovidResultsDaoJDBC;
import model.dao.impl.DestinationDaoJDBC;
import model.dao.impl.EmployeeDaoJDBC;
import model.dao.impl.PacientDaoJDBC;
import model.dao.impl.RegisterDaoJDBC;
import model.dao.impl.ResourceDaoJDBC;
import model.dao.impl.VentilationTypeDaoJDBC;

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

	public static BedTypeDao createBedTypeDao() {
		return new BedTypeDaoJDBC(DB.getConnection());
	}

	public static DestinationDao createDestinationDao() {
		return new DestinationDaoJDBC(DB.getConnection());
	}

	public static VentilationTypeDao createVentilationTypeDao() {
		return new VentilationTypeDaoJDBC(DB.getConnection());
	}

	public static EmployeeDao createEmployeeDao() {
		return new EmployeeDaoJDBC(DB.getConnection());
	}

	public static AnswerDao createAnswerDao() {
		return new AnswerDaoJDBC(DB.getConnection());
	}
	
}
