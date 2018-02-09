package LosOdiosos3.prueba_servidor.Entities;
import LosOdiosos3.prueba_servidor.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Event {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String name;	
	private String place;	
	private int year;
	private int month;
	private int day;
	private double fee;
	private String description;


	private String image;
	
	@ManyToMany(mappedBy="events")
	private List<Game> games;
	
	@ManyToMany(mappedBy="events")
	private List<Company> companies;

	protected Event() {
	}


	public Event(String name, String place, int year, int month, int day, double fee, String description, String image) {
		this.name = name;
		this.place = place;
		this.year = year;
		this.month = month;
		this.day = day;
		this.fee = fee;
		this.description = description;
		this.image = image;	
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
	
	public void setTitle(String name) {
		this.name = name;
	}
	
	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}
	
	public void setYear (int year) {
		this.year = year;
	}
	
	public void setMonth (int month) {
		this.month = month;
	}
	
	public void setDay (int day) {
		this.day = day;
	}
	
	public int getYear () {
		return year;
	}
	
	public int getMonth () {
		return month;
	}
	
	public int getDay () {
		return day;
	}
	
	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	@Override
	public String toString() {
		return "Event [id: " + id + ", name: " + name + ", place: " + place +
				", date: " + day + ", " + month + ", " + year + ", fee: " + fee + ", description: " + description + 
				", image: " + image + ", games: " + games + ", companies: " + companies + "]";
	}
}
