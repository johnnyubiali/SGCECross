package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Pacient implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer registSigs;
	private String name;
	private Integer age;
	
	public Pacient() {
	}
	
	public Pacient(Integer id, Integer registSigs, String name, Integer age) {
		super();
		this.id = id;
		this.registSigs = registSigs;
		this.name = name;
		this.age = age;
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

	@Override
	public int hashCode() {
		return Objects.hash(age, id, name, registSigs);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pacient other = (Pacient) obj;
		return Objects.equals(age, other.age) && Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Objects.equals(registSigs, other.registSigs);
	}

	@Override
	public String toString() {
		return "Pacient [id=" + id + ", registSigs=" + registSigs + ", name=" + name + ", age=" + age
				+  "]";
	}

	
}
