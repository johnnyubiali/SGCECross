package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Resource implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String specialty;

	public Resource() {
	}

	public Resource(Integer id, String specialty) {
		this.id = id;
		this.specialty = specialty;
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

}
