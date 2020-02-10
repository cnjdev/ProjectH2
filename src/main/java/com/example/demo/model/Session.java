package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.sql.Timestamp;

@Entity // This tells Hibernate to make a table out of this class
public class Session {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

	private String name;

	private Timestamp start;

	public Session(){
		this.id=0;
	}
	
	public Session(String name){
		this.id = 0;
		this.name = name;
		this.start = new Timestamp(System.currentTimeMillis());
	}
	
	public Session(long id, String name, Timestamp start){
		this.id = id;
		this.name = name;
		this.start = start;
	}
	
	public void refresh(){
		this.start = new Timestamp(System.currentTimeMillis());
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getStart() {
		return start;
	}
	
	public void setStart(Timestamp start) {
		this.start = start;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Session other = (Session) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Session [id=" + id + ", name=" + name + ", start=" + start + "]";
	}

}
