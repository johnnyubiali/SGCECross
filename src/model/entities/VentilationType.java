package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class VentilationType implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String description;

	public VentilationType() {
	}

	public VentilationType(Integer id, String description) {
		this.id = id;
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
		VentilationType other = (VentilationType) obj;
		return Objects.equals(id, other.id);
	}

}
