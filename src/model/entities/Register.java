package model.entities;

import java.util.Date;
import java.util.Objects;

public class Register {

	private Integer id;
	private Date dateInsert;
	private Date timeInsert;
	private Date dateFinal;
	private Date timeFinal;
	private String obs;

	Answer answer;
	Employee employee;
	Destination destination;
	Pacient pacient;
	CovidResults covidResults;
	Resource resource;
	BedType bedType;
	VentilationType ventilationType;
	

	public Register() {

	}

	public Register(Integer id, Date dateInsert, Date timeInsert, Date dateFinal, Date timeFinal, Answer answer, Employee employee, Destination destination, String obs, 
					Pacient pacient, CovidResults covidResults, Resource resource, BedType bedType, VentilationType ventilationType) {
		this.id = id;
		this.dateInsert = dateInsert;
		this.timeInsert = timeInsert;
		this.dateFinal = dateFinal;
		this.timeFinal = timeFinal;
		this.answer = answer;
		this.employee = employee;
		this.destination = destination;
		this.obs = obs;
		this.pacient = pacient;
		this.covidResults = covidResults;
		this.resource = resource;
		this.bedType = bedType;
		this.ventilationType = ventilationType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDateInsert() {
		return dateInsert;
	}

	public void setDateInsert(Date dateInsert) {
		this.dateInsert = dateInsert;
	}
	
	public Date getTimeInsert() {
		return timeInsert;
	}

	public void setTimeInsert(Date timeInsert) {
		this.timeInsert = timeInsert;
	}

	public Date getDateFinal() {
		return dateFinal;
	}

	public void setDateFinal(Date dateFinal) {
		this.dateFinal = dateFinal;
	}
	
	public Date getTimeFinal() {
		return timeFinal;
	}

	public void setTimeFinal(Date timeFinal) {
		this.timeFinal = timeFinal;
	}

	public	Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public Pacient getPacient() {
		return pacient;
	}

	public void setPacient(Pacient pacient) {
		this.pacient = pacient;
	}
	
	public CovidResults getCovidResults() {
		return covidResults;
	}

	public void setCovidResults(CovidResults covidResults) {
		this.covidResults = covidResults;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public BedType getBedType() {
		return bedType;
	}

	public void setBedType(BedType bedType) {
		this.bedType = bedType;
	}

	public VentilationType getVentilationType() {
		return ventilationType;
	}

	public void setVentilationType(VentilationType ventilationType) {
		this.ventilationType = ventilationType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Register other = (Register) obj;
		return Objects.equals(id, other.id);
	}
}
