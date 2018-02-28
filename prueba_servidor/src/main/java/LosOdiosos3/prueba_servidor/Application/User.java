package LosOdiosos3.prueba_servidor.Application;
import LosOdiosos3.prueba_servidor.*;
import LosOdiosos3.prueba_servidor.Application.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.mysql.fabric.xmlrpc.base.Array;

// se indica que es un componente
@Entity
public class User {	
	// id de la entidad user	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	// roles de usuario
	@ElementCollection (fetch = FetchType.EAGER)
	private List <String> roles;
	
	// atributos del usuario
	private String name = "...";
	private String password = "...";
	@Column(unique = true)
	private String email;
	private String date = "...";
	private String icon = null;
	
	@OneToMany(mappedBy="user")
	private List<MyList> lists = new ArrayList<MyList>();
	
	@OneToMany(mappedBy="user", cascade=CascadeType.REMOVE)
	private List<Comment> comments = new ArrayList<Comment>();
	
	@OneToMany(mappedBy="user", cascade=CascadeType.REMOVE)
	private List<Article> articles = new ArrayList<Article>();
	
	// constructor
	protected User () {}
	
	// contructor
	public User (String name, String password, String date, String email, String... roles) {
		this.name = name;
		this.password = new BCryptPasswordEncoder().encode(password);
		this.date = date;
		this.email = email;
		this.roles = new ArrayList<String>(Arrays.asList(roles));
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public void addList (MyList list) {
		lists.add(list);
	}
	
	public void removeList (MyList list) {
		lists.remove(list);
	}
	public List<MyList> getList (){
		return lists;
	}
	
	// setters
	public void setName (String name) {
		this.name = name;
	}
	
	public void setPassword (String password) {
		this.password = password;
	}
	
	public void setDate (String date) {
		this.date = date;
	}
	
	public void setEmail (String email) {
		this.email = email;
	}
	
	public void setIcon (String url) {
		icon = url;
	}
	
	// getters
	public String getName () {
		return name;
	}
	
	public String getPassword () {
		return password;
	}
	
	public String getDate () {
		return date;
	}
	
	public String getEmail () {
		return email;
	}
	
	public String getIcon () {
		return icon;
	}
	
	//Roles y comments
	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	public void addRole(String role) {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        roles.add(role);
    }
	
	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}	
	
	// toString
	public String toString () {
		return "User [name: " + name + "/npassword: " + password + "/nbirthday: " + date + "/nemail" + email + "]";
	}
}
