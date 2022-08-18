package model.entities;

import java.io.Serializable;

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
	
	
	
}
