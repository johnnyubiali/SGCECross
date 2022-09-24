package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class CovidResults implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String results;
	
	public CovidResults() {
	}

	public CovidResults(Integer id, String results) {
		super();
		this.id = id;
		this.results = results;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
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
		CovidResults other = (CovidResults) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "CovidResults [id=" + id + ", results=" + results + "]";
	}

	
	
	
}
