package LosOdiosos3.prueba_servidor.Controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import LosOdiosos3.prueba_servidor.Entities.Article;
import LosOdiosos3.prueba_servidor.Entities.ArticleRepository;
import LosOdiosos3.prueba_servidor.Entities.Comment;
import LosOdiosos3.prueba_servidor.Entities.CommentRepository;
import LosOdiosos3.prueba_servidor.Entities.Company;
import LosOdiosos3.prueba_servidor.Entities.CompanyRepository;
import LosOdiosos3.prueba_servidor.Entities.EventRepository;
import LosOdiosos3.prueba_servidor.Entities.Game;
import LosOdiosos3.prueba_servidor.Entities.GameRepository;
import LosOdiosos3.prueba_servidor.Entities.MyList;
import LosOdiosos3.prueba_servidor.Entities.MyListRepository;
import LosOdiosos3.prueba_servidor.Entities.User;
import LosOdiosos3.prueba_servidor.Entities.UserRepository;

@Controller
public class gameController {
	// ----------------------------- VARIABLES DEL SERVIDOR ---------------------------
		// iconos de usuario
		private List<String> icons = Arrays.asList("https://mir-s3-cdn-cf.behance.net/project_modules/disp/bb3a8833850498.56ba69ac33f26.png", "https://mir-s3-cdn-cf.behance.net/project_modules/disp/1bdc9a33850498.56ba69ac2ba5b.png", "https://mir-s3-cdn-cf.behance.net/project_modules/disp/bf6e4a33850498.56ba69ac3064f.png", "https://mir-s3-cdn-cf.behance.net/project_modules/disp/64623a33850498.56ba69ac2a6f7.png", "https://mir-s3-cdn-cf.behance.net/project_modules/disp/e70b1333850498.56ba69ac32ae3.png", "https://mir-s3-cdn-cf.behance.net/project_modules/disp/84c20033850498.56ba69ac290ea.png", "http://blogs.studentlife.utoronto.ca/lifeatuoft/files/2015/02/FullSizeRender.jpg", "https://i.pinimg.com/474x/c3/53/7f/c3537f7ba5a6d09a4621a77046ca926d--soccer-quotes-lineman.jpg");
		// variable de inicio de controlador
		boolean comienzo = false;
	// ----------------------------- FIN VARIABLES DEL SERVIDOR -----------------------
	
	// ----------------------------- INYECCIONES --------------------------------------	
	// repositorio de la tabla usuarios 
	@Autowired
	private UserRepository userRepository;	
	
	// repositorio de la tabla compañias
	@Autowired
	private CompanyRepository companyRepository;
	
	// repositorio de la tabla eventos
	@Autowired
	private EventRepository eventRepository;
	
	// repositorio de la tabla juegos
	@Autowired
	private GameRepository gameRepository;
	
	// repositorio de la tabla comentarios
	@Autowired
	private CommentRepository commentRepository;
	
	// repositorio de la tabla anuncios
	@Autowired
	private ArticleRepository articleRepository;
	
	//repositorio de la tabla listas
	@Autowired
	private MyListRepository mylistRepository;
	// ----------------------------- FIN INYECCIONES ----------------------------------

	// ----------------------------- LISTA DE JUEGOS ----------------------------------	
	@RequestMapping("/game_list/{modo}")
	public String game_list (Model model, HttpSession usuario, @PathVariable String modo) {
		List<String> list=new ArrayList<String>();		
		String div="<div class=\"col-md-3\">\r\n" + "<div class=\"Game\">\r\n" + "<img src=\"%s\" class=\"imagen\">\r\n" + "      <a href=\"%s\" style=\"text-align:center;display:block; color:  rgb(33, 73, 138);\">%s</a>\r\n" + "     \r\n" + "    </div>\r\n" + "  </div>";
		//List<Game> list_games_2 = gameRepository.findAll();
		//List<Game> list_games = gameRepository.findByNameContainingOrderByScoreAsc("");
		
		List<Game> list_games = null;
		switch(modo) {
			case "n":
				list_games = gameRepository.findAll();
				break;
			case "score_up":
				list_games = gameRepository.findByScoreAsc();
				break;
			case "score_down":
				list_games = gameRepository.findByScoreDesc();
				break;
			case "letter_up":
				list_games = gameRepository.findByNameAsc();
				break;
			case "letter_down":
				list_games = gameRepository.findByNameDesc();
				break;
			case "year_up":
				list_games = gameRepository.findByYearDesc();
				break;
			case "year_down":
				list_games = gameRepository.findByYearAsc();
				break;
			case "companies":
				list_games = gameRepository.findByCompanyAsc();
				break;
			
		}
		for(int i=0;i<list_games.size();i++) {
			//Aqui accederiamos a la base de datos para cambiar en cada iteracion las variables
			String Url=list_games.get(i).getImage();
			String Titulo=list_games.get(i).getName();
			String link="/game/" + Titulo;			
			//Copiamos el div que queremos poner en el documento html en una variable auxiliar
			//Le damos formato a la variable auxiliar y la añadimos a la lista
			String aux=String.format(div, Url, link, Titulo);
			list.add(aux);			
		}
		
		// se pasa la lista de juegos a la plantilla
		model.addAttribute("games", list);
		
		// se muestra el link de iniciar/registrar usuario si es false
		model.addAttribute("registered", usuario.getAttribute("registered"));
		boolean aux = !(Boolean) usuario.getAttribute("registered");
		model.addAttribute("unregistered", aux);
		model.addAttribute("name", usuario.getAttribute("name"));
		model.addAttribute("profile_img",String.format("<img src=\"%s\" class=\"profile_img\">",(String) usuario.getAttribute("icon")));

		
		return "game_list";
	}
	// ----------------------------- FIN LISTA DE JUEGOS ------------------------------
	
	// ----------------------------- JUEGO --------------------------------------------
	@RequestMapping("/game/{game_name}")
	public String Game (Model model, HttpSession usuario, @PathVariable String game_name) {
		// se coge de la BBDD la lista de juegos con dicho nombre recibido por url
		List <Game> games = gameRepository.findByName(game_name);		
		// se adquieren los atributos del juego
		String name = games.get(0).getName();
		Company company = games.get(0).getCompany();
		String genre = games.get(0).getGenre();
		String description = games.get(0).getDescription();
		int year = games.get(0).getYear();
		int pts = (int)games.get(0).getScore();
		String image = games.get(0).getImage();
		String url = games.get(0).getUrl();
	
		// se carga la puntuacion
		String score = "";
		switch(pts/2) {
			case 0: score = "<span class=\"fa fa-star\"></span>" + "<span class=\"fa fa-star\"></span>" + "<span class=\"fa fa-star\"></span>" + "<span class=\"fa fa-star\"></span>" + "<span class=\"fa fa-star\"></span>"; break;
			case 1:	score = "<span class=\"fa fa-star checked\"></span>" + "<span class=\"fa fa-star\"></span>" + "<span class=\"fa fa-star\"></span>" + "<span class=\"fa fa-star\"></span>" + "<span class=\"fa fa-star\"></span>"; break;
			case 2: score = "<span class=\"fa fa-star checked\"></span>" + "<span class=\"fa fa-star checked\"></span>" + "<span class=\"fa fa-star\"></span>" + "<span class=\"fa fa-star\"></span>" + "<span class=\"fa fa-star\"></span>"; break;
			case 3:	score = "<span class=\"fa fa-star checked\"></span>" + "<span class=\"fa fa-star checked\"></span>" + "<span class=\"fa fa-star checked\"></span>" + "<span class=\"fa fa-star\"></span>" + "<span class=\"fa fa-star\"></span>"; break;
			case 4:	score = "<span class=\"fa fa-star checked\"></span>" + "<span class=\"fa fa-star checked\"></span>" + "<span class=\"fa fa-star checked\"></span>" + "<span class=\"fa fa-star checked\"></span>" + "<span class=\"fa fa-star\"></span>"; break;
			case 5:	score = "<span class=\"fa fa-star checked\"></span>" + "<span class=\"fa fa-star checked\"></span>" + "<span class=\"fa fa-star checked\"></span>" + "<span class=\"fa fa-star checked\"></span>" + "<span class=\"fa fa-star checked\"></span>"; break;
		}
		model.addAttribute("options", " ");
		// gestion de commentarios del juego
		List<String> list=new ArrayList<String>();
		String div="<div class=\"com\"><div class=\"commentsUser \"><img class=\"comment_img\" src=\"%s\"></img>%s</div><div class=\"Date\">%s</div></div>\r\n" +  "     <div class=\"comments \">%s</div>"	+ "</div><br>";
		
		// si hay comentarios en el juego
		if(games.get(0).getComment().size()>0) {
			List<Comment> list_comments=games.get(0).getComment();			

			for(int i=0;i<list_comments.size();i++) {
				//Aqui accederiamos a la base de datos para cambiar en cada iteracion las variables
				String User=list_comments.get(i).getUser().getName();
				String Text=list_comments.get(i).getText();					
				Date d=list_comments.get(i).getDate();	
				String img=list_comments.get(i).getUser().getIcon();
				//Copiamos el div que queremos poner en el documento html en una variable auxiliar
				//Le damos formato a la variable auxiliar y la añadimos a la lista
				String aux=String.format(div, img,User,d.toString(), Text);				
				list.add(aux);				
			}
			Collections.reverse(list);
			model.addAttribute("comments", list);
		}else {
			model.addAttribute("comments"," ");
		}
		
		// añadir a la lista
		if(usuario.getAttribute("name") != null) {
			User user = userRepository.findByName((String)usuario.getAttribute("name")).get(0);
			List<MyList> lists = user.getList();
			String option = "<option value=\"%s \">%s</option>";
			String options = "";
			if(lists.size() > 0) {
				for(int i = 0; i < lists.size(); i++) {
					String Titulo = lists.get(i).getName();
					String aux = String.format(option, Titulo, Titulo);
					options += aux;
				}
				model.addAttribute("options", options);
			}
		}else {
			model.addAttribute("options", " ");
		}
		
		// se pasan los datos a la plantilla
		model.addAttribute("name_g", name);
		model.addAttribute("company", company.getName());
		model.addAttribute("genre", genre);
		model.addAttribute("description", description);
		model.addAttribute("year", year);
		model.addAttribute("score", score);
		model.addAttribute("image", image);
		model.addAttribute("url", url);

		// datos referentes a la barra de navegacion
		model.addAttribute("registered", usuario.getAttribute("registered"));
		boolean aux = !(Boolean) usuario.getAttribute("registered");
		model.addAttribute("unregistered", aux);
		model.addAttribute("name", usuario.getAttribute("name"));
		model.addAttribute("profile_img",String.format("<img src=\"%s\" class=\"profile_img\">",(String) usuario.getAttribute("icon")));
		
		return "game";
	}
	// ----------------------------- FIN JUEGO ----------------------------------------
	
	// ----------------------------- ADD GAME  ----------------------------------------
	@RequestMapping("/addList/{page}")
	public String addList (Model model, HttpSession usuario, @RequestParam String list, @RequestParam String name, @PathVariable String page) {	
		String ret = null;
		boolean repetido = false;
		if(page.equals("game")) {
			//Obtengo el nombre del usuario
			String user_name = (String) usuario.getAttribute("name");
			//Accedo al repositorio de usuarios con el nombre obtenido
			User user = userRepository.findByName(user_name).get(0);
			// se coge el juego donde me encuentro
			Game game = gameRepository.findByName(name).get(0);
			// se coge las listas de juegos
			List<MyList> mylists = user.getList();
			MyList mylist = null;
			// aceedo a la lista seleccionada
			for(int i = 0; i < mylists.size(); i++) {
				if(list.equals(mylists.get(i).getName() + " ")) {
					mylist = mylists.get(i);
					break;
				}
			}
			List<Game> games = mylist.getList();
			//Coge la lista de juegos del usuario en caso de que sea repetido lo convierte en true
			for(int i = 0; i < games.size(); i++) {
				System.out.println(game.getName() +" vs "+games.get(i).getName());
				if(game == games.get(i)) {
					repetido = true;
					break;
				}				
			}
			//En caso de que no este en la lista lo mete en la lista
			if(repetido == false) {
				mylist.addGame(game);
				game.addMyList(mylist);
				gameRepository.save(game);
				mylistRepository.save(mylist);
				userRepository.save(user);
			}
			
			// atributos de la pagina
			model.addAttribute("registered", usuario.getAttribute("registered"));
			boolean aux = !(Boolean) usuario.getAttribute("registered");
			model.addAttribute("unregistered", aux);
			model.addAttribute("name", usuario.getAttribute("name"));
			model.addAttribute("profile_img",String.format("<img src=\"%s\" class=\"profile_img\">",(String) usuario.getAttribute("icon")));

			model.addAttribute("alert"," ");
			model.addAttribute("hello", " ");
			model.addAttribute("Titulo", " ");
			model.addAttribute("Cuerpo", " ");	

			// añadir a la lista
			if(usuario.getAttribute("name") != null) {
				User user_aux = userRepository.findByName((String)usuario.getAttribute("name")).get(0);
				List<MyList> lists = user_aux.getList();
				String option = "<option value=\"%s \">%s</option>";
				String options = "";
				if(lists.size() > 0) {
					for(int i = 0; i < lists.size(); i++) {
						String Titulo = lists.get(i).getName();
						String aux_ = String.format(option, Titulo, Titulo);
						options += aux_;
					}
					model.addAttribute("options", options);
				}
			}else {
				model.addAttribute("options", " ");
			}
			
			// se retorna al juego
			ret= "/game/"+game.getName();			
			return ret;
		}
		// atributos de la pagina
		model.addAttribute("registered", usuario.getAttribute("registered"));
		boolean aux = !(Boolean) usuario.getAttribute("registered");
		model.addAttribute("unregistered", aux);
		model.addAttribute("name", usuario.getAttribute("name"));
		model.addAttribute("profile_img",String.format("<img src=\"%s\" class=\"profile_img\">",(String) usuario.getAttribute("icon")));

		model.addAttribute("alert"," ");
		model.addAttribute("hello", " ");
		model.addAttribute("Titulo", " ");
		model.addAttribute("Cuerpo", " ");	
		

		// articulos relevantes
		List<Article> articles = articleRepository.findAll();
		String news = "";
		if(articles.size() > 0) {
			String div ="<div class=\"card p-3 col-12 col-md-6 col-lg-4\">\r\n" + 	"<div class=\"card-wrapper\">\r\n" + 	"                <div class=\"card-img\">\r\n" + "                    <img src=\"  %s  \" alt=\"Mobirise\" title=\"\" media-simple=\"true\">\r\n" + "                </div>\r\n" + 	"                <div class=\"card-box\">\r\n" + 	"                    <h4 class=\"card-title pb-3 mbr-fonts-style display-7\">  %s  </h4>\r\n" + 	"                    <p class=\"mbr-text mbr-fonts-style display-7\">\r\n" + 	"                        %s  <a href=\"  %s  \">   Learn more...</a>\r\n" + 	"                    </p>\r\n" + 		"                </div>\r\n" + 		"            </div>\r\n" + 		"        </div>";			
			for(int i = 0; i < articles.size(); i++) {
				String Url= articles.get(i).getImage();
				String Titulo = articles.get(i).getTitle();	
				String Head = articles.get(i).getHead();
				String link="/article/" + Titulo;
				String aux_5 = String.format(div, Url, Titulo, Head, link);			
				news += aux_5;			
			}	
		}
		model.addAttribute("news", news);
		
		return "index";
	}
	// ----------------------------- FIN ADD GAME  ------------------------------------
	
}
