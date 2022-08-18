package model.entities;

import java.util.Date;
import java.util.Objects;

public class Register {

	private Integer id;
	public Date dateInsert;
	public Date dateFinal;
	private String nameEmployee;
	private String destination;
	private String obs;

	Pacient pacient;
	Resource resource;

	public Register() {

	}

	public Register(Integer id, Date dateInsert, Date dateFinal, String nameEmployee, String destination, String obs, Pacient pacient,
			Resource resource) {
		this.id = id;
		this.dateInsert = dateInsert;
		this.dateFinal = dateFinal;
		this.nameEmployee = nameEmployee;
		this.destination = destination;
		this.obs = obs;
		this.pacient = pacient;
		this.resource = resource;
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

	public Date getDateFinal() {
		return dateFinal;
	}

	public void setDateFinal(Date dateFinal) {
		this.dateFinal = dateFinal;
	}

	public String getNameEmployee() {
		return nameEmployee;
	}

	public void setNameEmployee(String nameEmployee) {
		this.nameEmployee = nameEmployee;
	}
	
	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
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

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dateFinal, dateInsert, id, nameEmployee, destination, obs, pacient, resource);
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
		return Objects.equals(dateFinal, other.dateFinal) && Objects.equals(dateInsert, other.dateInsert)
				&& Objects.equals(id, other.id) && Objects.equals(nameEmployee, other.nameEmployee)
				&& Objects.equals(destination, other.destination) && Objects.equals(obs, other.obs) 
				&& Objects.equals(pacient, other.pacient) && Objects.equals(resource, other.resource);
	}

}
