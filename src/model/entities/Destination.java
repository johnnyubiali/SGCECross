package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Destination implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String destiny;
	
	public Destination() {
	}

	public Destination(Integer id, String destiny) {
		super();
		this.id = id;
		this.destiny = destiny;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDestiny() {
		return destiny;
	}

	public void setDestiny(String destiny) {
		this.destiny = destiny;
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
		Destination other = (Destination) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "CovidDestiny [id=" + id + ", destiny=" + destiny + "]";
	}

	
	
	
}
