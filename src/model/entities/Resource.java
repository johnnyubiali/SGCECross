package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Resource implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String specialty;
	private String bedType;
	private String destination;
	private String covidResults;
	private String ventilationType;

	public Resource() {

	}

	public Resource(Integer id, String specialty, String bedType, String destination, String covidResults,
			String ventilationType) {
		this.id = id;
		this.specialty = specialty;
		this.bedType = bedType;
		this.destination = destination;
		this.covidResults = covidResults;
		this.ventilationType = ventilationType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	public String getBedType() {
		return bedType;
	}

	public void setBedType(String bedType) {
		this.bedType = bedType;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getCovidResults() {
		return covidResults;
	}

	public void setCovidResults(String covidResults) {
		this.covidResults = covidResults;
	}

	public String getVentilationType() {
		return ventilationType;
	}

	public void setVentilationType(String ventilationType) {
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
		Resource other = (Resource) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Resource [id=" + id + ", specialty=" + specialty + ", bedType=" + bedType + ", destination="
				+ destination + ", covidResults=" + covidResults + ", ventilationType=" + ventilationType + "]";
	}

	
	
}
