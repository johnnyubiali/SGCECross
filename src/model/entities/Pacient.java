package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Pacient implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer registSIGS;
	private String name;
	private Integer age;
	
	public Pacient() {
		
	}
	
	public Pacient(Integer id, Integer registSIGS, String name, Integer age) {
		super();
		this.id = id;
		this.registSIGS = registSIGS;
		this.name = name;
		this.age = age;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRegistSIGS() {
		return registSIGS;
	}

	public void setRegistSIGS(Integer registSIGS) {
		this.registSIGS = registSIGS;
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
