package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Pacient implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer registSigs;
	private String name;
	private Integer age;
	
	CovidResults covidResults;	
	
	public Pacient() {
	}
	
	public Pacient(Integer id, Integer registSigs, String name, Integer age, CovidResults covidResults) {
		super();
		this.id = id;
		this.registSigs = registSigs;
		this.name = name;
		this.age = age;
		this.covidResults =covidResults;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRegistSigs() {
		return registSigs;
	}

	public void setRegistSigs(Integer registSigs) {
		this.registSigs = registSigs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public CovidResults getCovidResults() {
		return covidResults;
	}

	public void setCovidResults(CovidResults covidResults) {
		this.covidResults = covidResults;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(id);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pacient other = (Pacient) obj;
		return Objects.equals(id, other.id);
	}
}
