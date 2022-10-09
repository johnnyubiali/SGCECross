package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.RegisterDao;
import model.entities.Answer;
import model.entities.BedType;
import model.entities.CovidResults;
import model.entities.Destination;
import model.entities.Employee;
import model.entities.Pacient;
import model.entities.Register;
import model.entities.Resource;
import model.entities.VentilationType;

public class RegisterDaoJDBC implements RegisterDao {

	private Connection conn;

	public RegisterDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Register obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO register " 
					+ "(DateInsert, TimeInsert, DateFinal, TimeFinal, Obs, IdAnswer "
					+ "IdEmployee, IdPacient, IdResource, IdDestination, IdCovidResults, IdBedType, IdVentilationType) " 
					+ "VALUES " 
					+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
					Statement.RETURN_GENERATED_KEYS);

			st.setDate(1, new java.sql.Date(obj.getDateInsert().getTime()));
			st.setDate(2, new java.sql.Date(obj.getTimeInsert().getTime()));
			st.setDate(3, new java.sql.Date(obj.getDateFinal().getTime()));
			st.setDate(4, new java.sql.Date(obj.getTimeFinal().getTime()));
			st.setString(5, obj.getObs());
			st.setInt(6, obj.getAnswer().getId());
			st.setInt(7, obj.getEmployee().getId());
			st.setInt(8, obj.getPacient().getId());
			st.setInt(9, obj.getResource().getId());
			st.setInt(10, obj.getDestination().getId());
			st.setInt(11, obj.getCovidResults().getId());
			st.setInt(12, obj.getBedType().getId());
			st.setInt(13, obj.getVentilationType().getId());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Erro ao inserir dados! Nenhuma linha foi afetada!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void update(Register obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE register " 
					+ "SET DateInsert = ?, TimeInsert = ?, DateFinal = ?, TimeFinal = ?, Obs = ?, IdAnswer = ?"
					+ "IdEmployee = ?, IdPacient = ?, IdResource = ?, IdDestination = ?, IdCovidResults = ?, IdBedType = ?, IdVentilationType = ?"
					+ "WHERE Id = ? ");

			st.setDate(1, new java.sql.Date(obj.getDateInsert().getTime()));
			st.setDate(2, new java.sql.Date(obj.getTimeInsert().getTime()));
			st.setDate(3, new java.sql.Date(obj.getDateFinal().getTime()));
			st.setDate(4, new java.sql.Date(obj.getTimeFinal().getTime()));
			st.setString(5, obj.getObs());
			st.setInt(6, obj.getAnswer().getId());
			st.setInt(7, obj.getEmployee().getId());
			st.setInt(8, obj.getPacient().getId());
			st.setInt(9, obj.getResource().getId());
			st.setInt(10, obj.getDestination().getId());
			st.setInt(11, obj.getCovidResults().getId());
			st.setInt(12, obj.getBedType().getId());
			st.setInt(13, obj.getVentilationType().getId());
			st.setInt(14, obj.getId());

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM register WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Register findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT register.*,employee.Name as EmpName, employee.Office as EmpOffice,"
					+ "pacient.RegistSigs as PacSIGS, pacient.Name as PacName, pacient.Age as PacAge, answer.Description as AnsDescription"
					+ "resource.Specialty as ResSpecialty, destination.Destiny as DestDestiny, covidresults.Results as CvResults,"
					+ "bedtype.Description as BtDescription, ventilationtype.Description as VtDescription "
					+"FROM register INNER JOIN employee, pacient, answer, resource, destination, covidresults, bedtype, ventilationtype "
					+"ON register.ResourceId = employee.Id, pacient.Id, answer.Id, resource.Id, destination.Id, covidresults.Id, bedtype.Id, ventilationtype.Id "
					+"WHERE register.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Employee emp = instantiateEmployee(rs);
				Pacient pac = instantiatePacient(rs);
				Answer ans = instantiateAnswer(rs);
				Resource res = instantiateResource(rs);
				Destination dest = instantiateDestination(rs);
				CovidResults cov = instantiateCovidResults(rs);
				BedType bt = instantiateBedType(rs);
				VentilationType vt = instantiateVentilationType(rs);
				Register obj = instantiateRegister(rs, emp, pac, ans, res, dest, cov, bt, vt);
				return obj;

			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Register instantiateRegister(ResultSet rs, Employee emp, Pacient pac, Answer ans, Resource res, 
			Destination dest, CovidResults cov, BedType bt, VentilationType vent) throws SQLException {
		Register obj = new Register();
		obj.setId(rs.getInt("Id"));
		obj.setDateInsert(new java.util.Date(rs.getTimestamp("DateInsert").getTime()));
		obj.setTimeInsert(new java.util.Date(rs.getTimestamp("TimeInsert").getTime()));
		obj.setDateFinal(new java.util.Date(rs.getTimestamp("DateFinal").getTime()));
		obj.setTimeFinal(new java.util.Date(rs.getTimestamp("TimeFinal").getTime()));
		obj.setObs(rs.getString("Obs"));
		obj.setEmployee(emp);
		obj.setPacient(pac);
		obj.setAnswer(ans);
		obj.setResource(res);
		obj.setDestination(dest);
		obj.setCovidResults(cov);
		obj.setBedType(bt);
		obj.setVentilationType(vent);
		return obj;
	}
	
	private Resource instantiateResource(ResultSet rs) throws SQLException {
		Resource res = new Resource();
		res.setId(rs.getInt("IdResource"));
		res.setSpecialty(rs.getString("ResSpecialty"));
		return res;
	}
	
	private Employee instantiateEmployee(ResultSet rs) throws SQLException {
		Employee emp = new Employee();
		emp.setId(rs.getInt("IdEmployee"));
		emp.setName(rs.getString("EmpName"));
		emp.setOffice(rs.getString("EmpOffice"));
		return emp;
	}
	
	private Pacient instantiatePacient(ResultSet rs) throws SQLException {
		Pacient pac = new Pacient();
		pac.setId(rs.getInt("IdPacient"));
		pac.setName(rs.getString("PacName"));
		pac.setRegistSigs(rs.getInt("PacSIGS"));
		pac.setAge(rs.getInt("PacAge"));
		return pac;
	}
	
	private Destination instantiateDestination(ResultSet rs) throws SQLException {
		Destination dest = new Destination();
		dest.setId(rs.getInt("IdDestination"));
		dest.setDestiny(rs.getString("DestDestiny"));
		return dest;
	}
	
	private CovidResults instantiateCovidResults(ResultSet rs) throws SQLException {
		CovidResults cv = new CovidResults();
		cv.setId(rs.getInt("IdCovidResults"));
		cv.setResults(rs.getString("CvResults"));
		return cv;
	}
	
	private BedType instantiateBedType(ResultSet rs) throws SQLException {
		BedType bt = new BedType();
		bt.setId(rs.getInt("IdBedType"));
		bt.setDescription(rs.getString("BtDescription"));
		return bt;
	}
	
	private VentilationType instantiateVentilationType(ResultSet rs) throws SQLException {
		VentilationType vt = new VentilationType();
		vt.setId(rs.getInt("IdVentilationType"));
		vt.setDescription(rs.getString("BtDescription"));
		return vt;
	}
	
	private Answer instantiateAnswer(ResultSet rs) throws SQLException {
		Answer ans = new Answer();
		ans.setId(rs.getInt("IdAnswer"));
		ans.setDescription(rs.getString("AnsDescription"));
		return ans;
	}

	@Override
	public List<Register> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT register.*,employee.Name as EmpName, employee.Office as EmpOffice,"
							+ "pacient.RegistSigs as PacSIGS, pacient.Name as PacName, pacient.Age as PacAge, answer.Description as AnsDescription,"
							+ "resource.Specialty as ResSpecialty, destination.Destiny as DestDestiny, covidresults.Results as CvResults,"
							+ "bedtype.Description as BtDescription, ventilationtype.Description as VtDescription "
							+"FROM register INNER JOIN employee, pacient, answer, resource, destination, covidresults, bedtype, ventilationtype "
							+"ON register.IdEmployee = employee.Id, register.IdPacient = pacient.Id, register.IdAnswer = answer.Id, "
							+ "register.IdResource = resource.Id, register.IdDestination = destination.Id, register.IdCovidResults = covidresults.Id,"
							+ "register.IdBedType = bedtype.Id, register.IdVentilationType = ventilationtype.Id "
							+"ORDER BY DateInsert");

			rs = st.executeQuery();

			List<Register> list = new ArrayList<>();
			Map<Integer, Employee> mapEmp = new HashMap<>();
			Map<Integer, Pacient> mapPac = new HashMap<>();
			Map<Integer, Answer> mapAns = new HashMap<>();
			Map<Integer, Resource> mapRes = new HashMap<>();
			Map<Integer, Destination> mapDest = new HashMap<>();
			Map<Integer, CovidResults> mapCov = new HashMap<>();
			Map<Integer, BedType> mapBt = new HashMap<>();
			Map<Integer, VentilationType> mapVt = new HashMap<>();
			
while (rs.next()) {
				
				Employee emp = mapEmp.get(rs.getInt("IdEmployee"));
				Pacient pac = mapPac.get(rs.getInt("IdPacient"));
				Answer ans = mapAns.get(rs.getInt("IdAnswer"));
				Resource res = mapRes.get(rs.getInt("IdResource"));
				Destination dest = mapDest.get(rs.getInt("IdDestination"));
				CovidResults cov = mapCov.get(rs.getInt("IdCovidResults"));
				BedType bt = mapBt.get(rs.getInt("IdBedType"));
				VentilationType vt = mapVt.get(rs.getInt("IdVentilationType"));
				
				
				if(emp == null) {
					emp = instantiateEmployee(rs);
					mapEmp.put(rs.getInt("IdEmployee"), emp);
				}
				if(pac == null) {
					pac = instantiatePacient(rs);
					mapPac.put(rs.getInt("IdPacient"), pac);
				}
				if(ans == null) {
					ans = instantiateAnswer(rs);
					mapAns.put(rs.getInt("IdAnswer"), ans);
				}
				if(res == null) {
					res = instantiateResource(rs);
					mapRes.put(rs.getInt("IdResource"), res);
				}
				if(dest == null) {
					dest = instantiateDestination(rs); 
					mapDest.put(rs.getInt("IdDestination"), dest);
				}
				if(cov == null) {
					cov = instantiateCovidResults(rs);
					mapCov.put(rs.getInt("IdCovidResults"), cov);
				}
				if(bt == null) {
					bt = instantiateBedType(rs);
					mapBt.put(rs.getInt("IdBedType"), bt);
				}
				if(vt == null) {
					vt = instantiateVentilationType(rs);
					mapVt.put(rs.getInt("IdVentilationType"), vt);
				}
				
				Register obj = instantiateRegister(rs, emp, pac, ans, res, dest, cov, bt, vt);
				list.add(obj);
				
			}
			return list;    
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Register> findByPacient(Pacient pacient) {
		// TODO Auto-generated method stub
		return null;
	}

}
