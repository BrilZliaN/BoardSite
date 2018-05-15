package ru.school38dz52.board;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import spark.ModelAndView;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

public class Server {
	
	public static Logger log;
	
	private JsonKVStore global;
	private JsonObjectStore<User> users;
	private JsonObjectStore<Page> pages;
	private JsonObjectStore<Category> categories;
	private JsonObjectStore<Task> tasks;
	private JsonObjectStore<Request> requests;
	private JsonObjectStore<Review> reviews;
	
	private HashMap<String, Object> globalVars = new HashMap<>();
	
	public Server() {
		loadLogger();
		
		initStores();
		if (global == null) {
			log.info("Can't start server");
			return;
		}
		setupServer();
		Spark.awaitInitialization();
		
		log.info("Server has successfully started");
	}
	
	private void loadLogger() {
	    log = Logger.getLogger("BoardServer");  
	    FileHandler fh;

	    try {  
	        fh = new FileHandler("server.log", true);
	        fh.setLevel(Level.ALL);
	        fh.setFormatter(new Formatter() {
	            @Override
	            public String format(LogRecord record) {
	                SimpleDateFormat logTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	                Calendar cal = new GregorianCalendar();
	                cal.setTimeInMillis(record.getMillis());
	                return logTime.format(cal.getTime())
	                		+ " <" + record.getLevel().getName() + "> "
	                        + record.getMessage() + "\n";
	            }
	        });
	        log.addHandler(fh);
	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	}
	
	private void initStores() {
		try {
			global = new JsonKVStore(new File("db/global.json"));
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
		
		try {
			users = new JsonObjectStore<>(new File("db/users.json"));
		} catch (Exception e1) {
			users = new JsonObjectStore<>();
			e1.printStackTrace();
		}
		try {
			pages = new JsonObjectStore<>(new File("db/pages.json"));
		} catch (Exception e1) {
			pages = new JsonObjectStore<>();
			e1.printStackTrace();
		}
		
		try {
			categories = new JsonObjectStore<>(new File("db/categories.json"));
		} catch (Exception e1) {
			categories = new JsonObjectStore<>();
			e1.printStackTrace();
		}
		try {
			tasks = new JsonObjectStore<>(new File("db/tasks.json"));
		} catch (Exception e1) {
			tasks = new JsonObjectStore<>();
			e1.printStackTrace();
		}
		try {
			requests = new JsonObjectStore<>(new File("db/requests.json"));
		} catch (Exception e1) {
			requests = new JsonObjectStore<>();
			e1.printStackTrace();
		}
		
		try {
			reviews = new JsonObjectStore<>(new File("db/reviews.json"));
		} catch (Exception e1) {
			reviews = new JsonObjectStore<>();
			e1.printStackTrace();
		}
		
		Thread t = new Thread() {
			public void run() {
				while(true) {
					log.info("Saving database...");
					users.saveFile();
					pages.saveFile();
					categories.saveFile();
					tasks.saveFile();
					requests.saveFile();
					log.info("Saved to disk");
					try {
						Thread.sleep(Long.parseLong(global.get("settings.saveDelay")));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
	}
	
	private void setupServer() {
		globalVars.put("projecttitle", global.get("project.title"));
		globalVars.put("projectauthor", global.get("project.author"));
		globalVars.put("projectmottosmall", global.get("motto.small"));
		globalVars.put("projectmottolarge", global.get("motto.large"));
		globalVars.put("bottombuttons", global.get("project.bottomButtons"));
		
		List<Category> clist = new ArrayList<Category>();
		Category c = new Category();
		c.setTitle("Все услуги");
		clist.add(c);
		clist.addAll(categories.getStore());
		globalVars.put("projectcategories", clist);
		globalVars.put("projectmenu", pages.getStore());
		
		Spark.port(8081);
		
		setupRoutes();
	}
	
	private void setupRoutes() {
		Spark.staticFiles.externalLocation(System.getProperty("user.dir") + "/public/");
		
		Spark.get("/", (request, response) -> {
			HashMap<String, Object> a = new HashMap<>();
			
			a.putAll(globalVars);
			a.put("pagetitle", "Главная");
			a.put("pageurl", "");
			
			request.session().attribute("goto", "/");
			
			boolean loggedin = request.session().attributes().contains("uid");
			a.put("userloggedin", loggedin);
			if (loggedin) {
				User user = users.get(Integer.parseInt(request.session().attribute("uid")));
				a.put("username", user.getShortUserFullName());
				a.put("useradmin", user.isAdmin());
				a.put("isPerformer", user.isPerformer());
			}
			
			return new ModelAndView(a, "index.ftl");
		}, new FreeMarkerEngine());
		
		Spark.get("/stop/" + global.get("settings.password"), (request, response) -> {
			return "Server is stopping...";
		});
		
		Spark.after("/stop/" + global.get("settings.password"), (request, response) -> {
			users.saveFile();
			pages.saveFile();
			categories.saveFile();
			tasks.saveFile();
			requests.saveFile();
			reviews.saveFile();
			log.info("Stopping server (signal by " + request.ip() + ")");
			
			System.exit(0);
		});
		
		Spark.get("/feedback", (request, response) -> {
			HashMap<String, Object> a = new HashMap<>();
			
			a.putAll(globalVars);
			a.put("pagetitle", "Отзывы");
			a.put("pageurl", "feedback");
			
			boolean loggedin = request.session().attributes().contains("uid");
			a.put("userloggedin", loggedin);
			if (loggedin) {
				User user = users.get(Integer.parseInt(request.session().attribute("uid")));
				a.put("username", user.getShortUserFullName());
				a.put("useradmin", user.isAdmin());
				a.put("isPerformer", user.isPerformer());
			}
			
			List<ItemReviewInfo> reviewlist = new ArrayList<ItemReviewInfo>();
			for (int i = reviews.size() - 1; i >= 0; i--) {
				Review review = reviews.get(i);
				Request req = requests.get(review.getRequestid());
				Task task = tasks.get(req.getItemId());
				User user = users.get(review.getUserid());
				
				ItemReviewInfo reviewinfo = new ItemReviewInfo();
				reviewinfo.setReview(review.getReview());
				reviewinfo.setTask(task.getTitle());
				reviewinfo.setUser(user.getShortUserFullName());
				reviewlist.add(reviewinfo);
			}
			a.put("reviewlist", reviewlist);
			
			return new ModelAndView(a, "feedback.ftl");
		}, new FreeMarkerEngine());
		
		Spark.get("/feedback/add", (request, response) -> {
			HashMap<String, Object> a = new HashMap<>();
			
			a.putAll(globalVars);
			a.put("pagetitle", "Оставить отзыв");
			a.put("pageurl", "feedback/add");
			
			a.put("success", false);
			a.put("error", false);
			
			boolean loggedin = request.session().attributes().contains("uid");
			a.put("userloggedin", loggedin);
			int userid = -1;
			if (loggedin) {
				User user = users.get(Integer.parseInt(request.session().attribute("uid")));
				a.put("username", user.getShortUserFullName());
				a.put("useradmin", user.isAdmin());
				a.put("isPerformer", user.isPerformer());
				userid = user.getId();
			} else {
				response.redirect("/");
			}
			
			List<ItemRequestInfo> userrequests = new ArrayList<ItemRequestInfo>();
			for (int id = 0; id < requests.size(); id++) {
				Request req = requests.get(id);
				if (req.getRequesterId() == userid) {
					Task task = tasks.get(req.getItemId());
					User customer = users.get(req.getRequesterId());
					User performer = users.get(req.getPerformerId());
					ItemRequestInfo item = ItemRequestInfo.create(id, req, task, customer, performer);
					userrequests.add(item);
				}
			}
			
			a.put("userrequests", userrequests);
			a.put("hasrequests", userrequests.size() > 0);
			
			return new ModelAndView(a, "review_new.ftl");
		}, new FreeMarkerEngine());
		
		Spark.post("/feedback/add", (request, response) -> {
			HashMap<String, Object> a = new HashMap<>();
			
			a.putAll(globalVars);
			a.put("pagetitle", "Оставить отзыв");
			a.put("pageurl", "feedback/add");
			
			boolean loggedin = request.session().attributes().contains("uid");
			a.put("userloggedin", loggedin);
			int userid = 0;
			if (loggedin) {
				User user = users.get(Integer.parseInt(request.session().attribute("uid")));
				a.put("username", user.getShortUserFullName());
				a.put("useradmin", user.isAdmin());
				a.put("isPerformer", user.isPerformer());
				userid = user.getId();
			} else {
				response.redirect("/");
			}
			
			int requestid = Integer.parseInt(request.queryParams("request"));
			String reviewt = request.queryParams("review");
			
			a.put("success", false);
			a.put("error", false);
			
			if (!reviewt.isEmpty()) {
				Review review = new Review();
				review.setUserid(userid);
				review.setRequestid(requestid);
				review.setReview(reviewt);
				
				int rid = reviews.size();
				reviews.set(rid, review);
				
				a.put("success", true);
				a.put("error", false);
				
			} else {
				a.put("success", false);
				a.put("error", true);
			}
			
			List<ItemRequestInfo> userrequests = new ArrayList<ItemRequestInfo>();
			for (int id = 0; id < requests.size(); id++) {
				Request req = requests.get(id);
				if (req.getRequesterId() == userid) {
					Task task = tasks.get(req.getItemId());
					User customer = users.get(req.getRequesterId());
					User performer = users.get(req.getPerformerId());
					ItemRequestInfo item = ItemRequestInfo.create(id, req, task, customer, performer);
					userrequests.add(item);
				}
			}
			
			a.put("userrequests", userrequests);
			a.put("hasrequests", userrequests.size() > 0);

			return new ModelAndView(a, "review_new.ftl");
		}, new FreeMarkerEngine());
		
		Spark.get("/page/:url", (request, response) -> {
			HashMap<String, Object> a = new HashMap<>();
			
			a.putAll(globalVars);
			
			String url = "page/" + request.params(":url");
			
			a.put("pagetitle", "404");
			a.put("pageurl", url);
			a.put("content", "<h2>Страница не найдена</h2>");
			for (Page page : pages.getStore()) {
				if (page.getUrl().equalsIgnoreCase(url)) {
					a.put("pagetitle", page.getTitle());
					a.put("content", page.getContent());
					break;
				}
			}
			
			boolean loggedin = request.session().attributes().contains("uid");
			a.put("userloggedin", loggedin);
			if (loggedin) {
				User user = users.get(Integer.parseInt(request.session().attribute("uid")));
				a.put("username", user.getShortUserFullName());
				a.put("useradmin", user.isAdmin());
				a.put("isPerformer", user.isPerformer());
			}
			
			return new ModelAndView(a, "page.ftl");
		}, new FreeMarkerEngine());
		
		Spark.get("/server-status", (request, response) -> {
			return "ok";
		});
		
		// ===========================================================
		// ------------------------- Задания -------------------------
		// ===========================================================
		
		Spark.get("/tasks", (request, response) -> {
			HashMap<String, Object> a = new HashMap<>();
			
			a.putAll(globalVars);
			a.put("pagetitle", "Услуги");
			a.put("pageurl", "tasks");
			
			boolean loggedin = request.session().attributes().contains("uid");
			a.put("userloggedin", loggedin);
			if (loggedin) {
				User user = users.get(Integer.parseInt(request.session().attribute("uid")));
				a.put("username", user.getShortUserFullName());
				a.put("useradmin", user.isAdmin());
				a.put("isPerformer", user.isPerformer());
			}
			
			List<ItemTaskCategory> tasklist = new ArrayList<ItemTaskCategory>();
			for (Task task : tasks.getStore()) {
				if (!task.isEnabled()) continue;
				Category category = null;
				for (Category cat : categories.getStore()) {
					if (task.getCategoryId() == cat.getId()) {
						category = cat;
						break;
					}
				}
				ItemTaskCategory i = new ItemTaskCategory();
				i.setCategory(category);
				Task copy = task.copy();
				copy.setBudget(task.getBudget().replace("рублей", "<i class=\"fa fa-rub\"></i>").replace("руб.", "<i class=\"fa fa-rub\"></i>").replace("руб", "<i class=\"fa fa-rub\"></i>"));
				i.setTask(copy);
				tasklist.add(i);
			}
			
			a.put("tasks", tasklist);
			
			return new ModelAndView(a, "tasks.ftl");
		}, new FreeMarkerEngine());
		
		Spark.get("/tasks/", (request, response) -> {
			HashMap<String, Object> a = new HashMap<>();
			
			a.putAll(globalVars);
			a.put("pagetitle", "Услуги");
			a.put("pageurl", "tasks");
			
			boolean loggedin = request.session().attributes().contains("uid");
			a.put("userloggedin", loggedin);
			if (loggedin) {
				User user = users.get(Integer.parseInt(request.session().attribute("uid")));
				a.put("username", user.getShortUserFullName());
				a.put("useradmin", user.isAdmin());
				a.put("isPerformer", user.isPerformer());
			}
			
			List<ItemTaskCategory> tasklist = new ArrayList<ItemTaskCategory>();
			for (Task task : tasks.getStore()) {
				if (!task.isEnabled()) continue;
				Category category = null;
				for (Category cat : categories.getStore()) {
					if (task.getCategoryId() == cat.getId()) {
						category = cat;
						break;
					}
				}
				ItemTaskCategory i = new ItemTaskCategory();
				i.setCategory(category);
				Task copy = task.copy();
				copy.setBudget(task.getBudget().replace("рублей", "<i class=\"fa fa-rub\"></i>").replace("руб.", "<i class=\"fa fa-rub\"></i>").replace("руб", "<i class=\"fa fa-rub\"></i>"));
				i.setTask(copy);
				tasklist.add(i);
			}
			
			a.put("tasks", tasklist);
			
			return new ModelAndView(a, "tasks.ftl");
		}, new FreeMarkerEngine());
		
		Spark.get("/tasks/:id", (request, response) -> {
			HashMap<String, Object> a = new HashMap<>();
			
			int id = -1;
			try {
				id = ((int)Integer.parseInt(request.params(":id")));
			} catch (Exception e) {
				log.info("Someone tried to access /tasks/" + request.params(":id") + " but failed! (" + e + ")");
				response.redirect("/tasks");
				return null;
			}
			
			a.putAll(globalVars);
			Category category = categories.get(id);
			List<ItemTaskCategory> tasklist = new ArrayList<ItemTaskCategory>();
			if (category != null) {
				a.put("pagetitle", category.getTitle());
				for (Task task : tasks.getStore()) {
					if (!task.isEnabled()) continue;
					if (task.getCategoryId() == id) {
						ItemTaskCategory i = new ItemTaskCategory();
						i.setCategory(category);
						Task copy = task.copy();
						copy.setBudget(task.getBudget().replace("рублей", "<i class=\"fa fa-rub\"></i>").replace("руб.", "<i class=\"fa fa-rub\"></i>").replace("руб", "<i class=\"fa fa-rub\"></i>"));
						i.setTask(copy);
						tasklist.add(i);
					}
				}
			} else {
				a.put("pagetitle", "Услуги");
			}
			a.put("pageurl", "tasks/" + id);
			
			boolean loggedin = request.session().attributes().contains("uid");
			a.put("userloggedin", loggedin);
			if (loggedin) {
				User user = users.get(Integer.parseInt(request.session().attribute("uid")));
				a.put("username", user.getShortUserFullName());
				a.put("useradmin", user.isAdmin());
				a.put("isPerformer", user.isPerformer());
			}
			
			a.put("tasks", tasklist);
			
			return new ModelAndView(a, "tasks.ftl");
		}, new FreeMarkerEngine());
		
		Spark.get("/book/:id", (request, response) -> {
			HashMap<String, Object> a = new HashMap<>();
			
			int id = -1;
			try {
				id = ((int)Integer.parseInt(request.params(":id")));
			} catch (Exception e) {
				log.info("Someone tried to access /book/" + request.params(":id") + " but failed! (" + e + ")");
				response.redirect("/tasks");
				return null;
			}
			
			a.putAll(globalVars);
			Task task = tasks.get(id);
			if (task == null || !task.isEnabled()) {
				response.redirect("/tasks");
			}
			a.put("pagetitle", "Подтверждение заказа");
			a.put("pageurl", "book/" + id);
			a.put("success", false);
			a.put("task", task);
			
			boolean loggedin = request.session().attributes().contains("uid");
			a.put("userloggedin", loggedin);
			if (loggedin) {
				User user = users.get(Integer.parseInt(request.session().attribute("uid")));
				a.put("username", user.getShortUserFullName());
				a.put("useradmin", user.isAdmin());
				a.put("isPerformer", user.isPerformer());
			} else {
				request.session().attribute("goto", "/book/" + id);
				response.redirect("/login");
			}
			
			return new ModelAndView(a, "book.ftl");
		}, new FreeMarkerEngine());
		
		Spark.post("/book/:id", (request, response) -> {
			HashMap<String, Object> a = new HashMap<>();
			
			int id = -1;
			try {
				id = ((int)Integer.parseInt(request.params(":id")));
			} catch (Exception e) {
				log.info("Someone tried to access POST /book/" + request.params(":id") + " but failed! (" + e + ")");
				response.redirect("/tasks");
				return null;
			}
			
			a.putAll(globalVars);
			Task task = tasks.get(id);
			if (task == null || !task.isEnabled()) {
				response.redirect("/tasks");
			}
			a.put("pagetitle", "Подтверждение заказа");
			a.put("pageurl", "book/" + id);
			a.put("success", false);
			a.put("task", task);
			
			boolean loggedin = request.session().attributes().contains("uid");
			a.put("userloggedin", loggedin);
			if (loggedin) {
				User user = users.get(Integer.parseInt(request.session().attribute("uid")));
				a.put("username", user.getShortUserFullName());
				a.put("useradmin", user.isAdmin());
				a.put("isPerformer", user.isPerformer());
				
				Request req = new Request();
				req.setItemId(task.getId());
				req.setRequesterId(user.getId());
				req.setTime(System.currentTimeMillis());
				try {
					requests.set(requests.size(), req);
					a.put("success", true);
				} catch (Exception e) {
					log.severe("Error while creating request");
					log.log(Level.SEVERE, e.getMessage(), e);
				}
			} else {
				request.session().attribute("goto", "/book/" + id);
				response.redirect("/login");
			}
			
			return new ModelAndView(a, "book.ftl");
		}, new FreeMarkerEngine());
		
		// ===========================================================
		// --------------------- Редактирование ----------------------
		// ===========================================================
		
		Spark.get("/requests", (request, response) -> {
			HashMap<String, Object> a = new HashMap<>();
			
			a.putAll(globalVars);
			a.put("pagetitle", "Заказы");
			a.put("pageurl", "requests");
			
			boolean loggedin = request.session().attributes().contains("uid");
			a.put("userloggedin", loggedin);
			if (loggedin) {
				User user = users.get(Integer.parseInt(request.session().attribute("uid")));
				a.put("username", user.getShortUserFullName());
				a.put("useradmin", user.isAdmin());
				a.put("isPerformer", user.isPerformer());
				if (!user.isPerformer()) response.redirect("/");
			} else {
				response.redirect("/");
			}
			
			ArrayList<ItemRequestInfo> requestlist = new ArrayList<ItemRequestInfo>();
			for (int id = requests.size() - 1; id >= 0; id--) {
				Request req = requests.get(id);
				Task task = tasks.get(req.getItemId());
				User customer = users.get(req.getRequesterId());
				User performer = users.get(req.getPerformerId());
				ItemRequestInfo item = ItemRequestInfo.create(id, req, task, customer, performer);
				requestlist.add(item);
			}
			a.put("requestlist", requestlist);
			
			List<ItemTaskCategory> tasklist = new ArrayList<ItemTaskCategory>();
			for (Task task : tasks.getStore()) {
				if (!task.isEnabled()) continue;
				Category category = null;
				for (Category cat : categories.getStore()) {
					if (task.getCategoryId() == cat.getId()) {
						category = cat;
						break;
					}
				}
				ItemTaskCategory i = new ItemTaskCategory();
				i.setCategory(category);
				User user = users.get(task.getUserId());
				i.setUser(user);
				Task copy = task.copy();
				copy.setBudget(task.getBudget().replace("рублей", "<i class=\"fa fa-rub\"></i>").replace("руб.", "<i class=\"fa fa-rub\"></i>").replace("руб", "<i class=\"fa fa-rub\"></i>"));
				i.setTask(copy);
				tasklist.add(i);
			}
			a.put("tasklist", tasklist);
			
			return new ModelAndView(a, "requests.ftl");
		}, new FreeMarkerEngine());
		
		Spark.get("/requests/assign/:id", (request, response) -> {
			int id;
			try {
				id = ((int)Integer.parseInt(request.params(":id")));
			} catch (Exception e) {
				log.info("Someone tried to access POST /requests/assign/" + request.params(":id") + " but failed! (" + e + ")");
				response.redirect("/requests");
				return null;
			}
			
			boolean loggedin = request.session().attributes().contains("uid");
			if (loggedin) {
				User user = users.get(Integer.parseInt(request.session().attribute("uid")));
				if (!user.isPerformer()) response.redirect("/");
				Request req = requests.get(id);
				req.setPerformerId(user.getId());
				response.redirect("/requests");
			} else {
				response.redirect("/login");
			}
			
			return "<script>window.location.replace(\"/requests\");</script>";
		});
		
		Spark.get("/requests/add", (request, response) -> {
			HashMap<String, Object> a = new HashMap<>();
			
			a.putAll(globalVars);
			a.put("pagetitle", "Добавление задания");
			a.put("pageurl", "requests/add");
			
			a.put("success", false);
			a.put("error", false);
			
			boolean loggedin = request.session().attributes().contains("uid");
			a.put("userloggedin", loggedin);
			if (loggedin) {
				User user = users.get(Integer.parseInt(request.session().attribute("uid")));
				a.put("username", user.getShortUserFullName());
				a.put("useradmin", user.isAdmin());
				a.put("isPerformer", user.isPerformer());
				if (!user.isPerformer()) response.redirect("/");
			} else {
				response.redirect("/");
			}
			
			return new ModelAndView(a, "task_new.ftl");
		}, new FreeMarkerEngine());
		
		Spark.post("/requests/add", (request, response) -> {
			HashMap<String, Object> a = new HashMap<>();
			
			a.putAll(globalVars);
			a.put("pagetitle", "Добавление задания");
			a.put("pageurl", "requests/add");
			
			boolean loggedin = request.session().attributes().contains("uid");
			a.put("userloggedin", loggedin);
			int userid = 0;
			if (loggedin) {
				User user = users.get(Integer.parseInt(request.session().attribute("uid")));
				a.put("username", user.getShortUserFullName());
				a.put("useradmin", user.isAdmin());
				a.put("isPerformer", user.isPerformer());
				userid = user.getId();
				if (!user.isPerformer()) response.redirect("/");
			} else {
				response.redirect("/");
			}
			
			int category = Integer.parseInt(request.queryParams("category"));
			String title = request.queryParams("title");
			String budget = request.queryParams("budget");
			String description = request.queryParams("description");
			String photo = "";
			if (request.queryParams().contains("photo")) photo = request.queryParams("photo");
			
			if (!(title.isEmpty() || budget.isEmpty() || description.isEmpty())) {
				Task task = new Task();
				task.setUserId(userid);
				task.setCategoryId(category);
				task.setTitle(title);
				task.setBudget(budget);
				task.setDescription(description);
				task.setPhoto(photo);
				int tid = tasks.size();
				task.setId(tid);
				tasks.set(tid, task);
				
				a.put("success", true);
				a.put("error", false);
			} else {
				a.put("success", false);
				a.put("error", true);
			}

			return new ModelAndView(a, "task_new.ftl");
		}, new FreeMarkerEngine());
		
		Spark.get("/adminka", (request, response) -> {
			HashMap<String, Object> a = new HashMap<>();
			
			a.putAll(globalVars);
			a.put("pagetitle", "Админ-панель");
			a.put("pageurl", "adminka");
			
			boolean loggedin = request.session().attributes().contains("uid");
			a.put("userloggedin", loggedin);
			if (loggedin) {
				User user = users.get(Integer.parseInt(request.session().attribute("uid")));
				a.put("username", user.getShortUserFullName());
				a.put("useradmin", user.isAdmin());
				a.put("isPerformer", user.isPerformer());
				if (!user.isAdmin()) response.redirect("/");
			} else {
				response.redirect("/");
			}
			
			List<User> requesterlist = new ArrayList<User>();
			List<User> performerlist = new ArrayList<User>();
			for (User user : users.getStore()) {
				if (user.isPerformer()) {
					performerlist.add(user);
				} else {
					requesterlist.add(user);
				}
			}
			a.put("requesterlist", requesterlist);
			a.put("performerlist", performerlist);
			
			return new ModelAndView(a, "adminpanel.ftl");
		}, new FreeMarkerEngine());
		
		Spark.get("/adminka/promote/:id", (request, response) -> {
			int id;
			try {
				id = ((int)Integer.parseInt(request.params(":id")));
			} catch (Exception e) {
				log.info("Someone tried to access POST /adminka/promote/" + request.params(":id") + " but failed! (" + e + ")");
				response.redirect("/");
				return null;
			}
			
			boolean loggedin = request.session().attributes().contains("uid");
			if (loggedin) {
				User user = users.get(Integer.parseInt(request.session().attribute("uid")));
				if (!user.isAdmin()) response.redirect("/");
				User promoting = users.get(id);
				promoting.setAdmin(true);
				response.redirect("/adminka");
			} else {
				response.redirect("/login");
			}
			
			return "<script>window.location.replace(\"/adminka\");</script>";
		});
		
		// ===========================================================
		// ----------------------- Авторизация -----------------------
		// ===========================================================
		
		Spark.get("/login", (request, response) -> {
			HashMap<String, Object> a = new HashMap<>();
			
			a.putAll(globalVars);
			a.put("pagetitle", "Вход");
			a.put("pageurl", "login");
			
			boolean loggedin = request.session().attributes().contains("uid");
			a.put("userloggedin", loggedin);
			if (loggedin) {
				User user = users.get(Integer.parseInt(request.session().attribute("uid")));
				a.put("username", user.getShortUserFullName());
				a.put("useradmin", user.isAdmin());
				a.put("isPerformer", user.isPerformer());
			}
			
			a.put("error", false);
			
			return new ModelAndView(a, "login.ftl");
		}, new FreeMarkerEngine());
		
		Spark.get("/register", (request, response) -> {
			HashMap<String, Object> a = new HashMap<>();
			
			a.putAll(globalVars);
			a.put("pagetitle", "Регистрация");
			a.put("pageurl", "register");
			
			boolean loggedin = request.session().attributes().contains("uid");
			a.put("userloggedin", loggedin);
			if (loggedin) {
				User user = users.get(Integer.parseInt(request.session().attribute("uid")));
				a.put("username", user.getShortUserFullName());
				a.put("useradmin", user.isAdmin());
				a.put("isPerformer", user.isPerformer());
			}
			
			a.put("error", false);
			
			return new ModelAndView(a, "register.ftl");
		}, new FreeMarkerEngine());
		
		Spark.post("/login", (request, response) -> {
			HashMap<String, Object> a = new HashMap<>();

			a.putAll(globalVars);
			a.put("pagetitle", "Вход");
			a.put("pageurl", "login");
			
			String login = request.queryParams("login");
			String password = request.queryParams("password");
			
			boolean loggedin = false;
			a.put("error", true);
			for (User user : users.getStore()) {
				if (user.getLogin().equalsIgnoreCase(login)) {
					if (Password.check(password, user.getPasswordHash().replace("\u003d", "="))) {
						request.session().attribute("uid", "" + user.getId());
						if (request.session().attributes().contains("goto")) {
							response.redirect(request.session().attribute("goto"));
						} else {
							response.redirect("/");
						}
					}
				}
			}
			
			a.put("userloggedin", loggedin);
			
			return new ModelAndView(a, "login.ftl");
		}, new FreeMarkerEngine());
		
		Spark.post("/register", (request, response) -> {
			HashMap<String, Object> a = new HashMap<>();
			
			a.putAll(globalVars);
			a.put("pagetitle", "Регистрация");
			a.put("pageurl", "register");
			
			a.put("error", true);
			//
			String email = request.queryParams("email");
			String login = request.queryParams("login");
			String password = request.queryParams("password");
			String surname = request.queryParams("surname");
			String name = request.queryParams("fullname");
			String patronymic = request.queryParams("patronymic");
			String phone = request.queryParams("phone");
			String birthday = request.queryParams("birthday");
			boolean isPerformer = request.queryParams("userType").equalsIgnoreCase("performer");
			
			if (!(login.isEmpty() || password.isEmpty() || surname.isEmpty() || name.isEmpty() || patronymic.isEmpty() || phone.isEmpty() || birthday.isEmpty())) {
				boolean flag = true;
				
				for (User user : users.getStore()) {
					if (user.getLogin().equalsIgnoreCase(login)) {
						flag = false;
					}
				}
				
				if (flag) {
					User u = new User();
					u.setEmail(email);
					u.setLogin(login);
					u.setPasswordHash(Password.getSaltedHash(password));
					u.setSurname(surname);
					u.setName(name);
					u.setPatronymic(patronymic);
					u.setPhone(phone);
					u.setBirthday(birthday);
					u.setPerformer(isPerformer);
					int id = users.size();
					u.setId(id);
					users.set(id, u);
					
					a.put("error", false);
					
					request.session().attribute("uid", "" + id);
					if (request.session().attributes().contains("goto") && !(request.session().attribute("goto").equals("/"))) {
						response.redirect(request.session().attribute("goto"));
					}
				}
			}
			//
			
			boolean loggedin = request.session().attributes().contains("uid");
			a.put("userloggedin", loggedin);
			if (loggedin) {
				User user = users.get(Integer.parseInt(request.session().attribute("uid")));
				a.put("username", user.getShortUserFullName());
				a.put("useradmin", user.isAdmin());
				a.put("isPerformer", user.isPerformer());
			}
			
			return new ModelAndView(a, "register.ftl");
		}, new FreeMarkerEngine());
		
		// ===========================================================
		// ------------------------- Профиль -------------------------
		// ===========================================================
		
		Spark.get("/profile", (request, response) -> {
			HashMap<String, Object> a = new HashMap<>();
			
			a.putAll(globalVars);
			a.put("pagetitle", "Профиль");
			a.put("pageurl", "profile");
			
			boolean loggedin = request.session().attributes().contains("uid");
			a.put("userloggedin", loggedin);
			if (loggedin) {
				User user = users.get(Integer.parseInt(request.session().attribute("uid")));
				a.put("username", user.getShortUserFullName());
				a.put("useradmin", user.isAdmin());
				a.put("isPerformer", user.isPerformer());
				a.put("user", user);
			} else {
				response.redirect("/login");
			}
			
			a.put("error", false);
			a.put("success", false);
			
			return new ModelAndView(a, "profile.ftl");
		}, new FreeMarkerEngine());
		
		Spark.get("/profile/edit", (request, response) -> {
			HashMap<String, Object> a = new HashMap<>();
			
			a.putAll(globalVars);
			a.put("pagetitle", "Редактирование профиля");
			a.put("pageurl", "profile/edit");
			
			boolean loggedin = request.session().attributes().contains("uid");
			a.put("userloggedin", loggedin);
			if (loggedin) {
				User user = users.get(Integer.parseInt(request.session().attribute("uid")));
				a.put("username", user.getShortUserFullName());
				a.put("useradmin", user.isAdmin());
				a.put("isPerformer", user.isPerformer());
				a.put("user", user);
			} else {
				response.redirect("/login");
			}
			
			a.put("error", false);
			a.put("success", false);
			
			return new ModelAndView(a, "profile_edit.ftl");
		}, new FreeMarkerEngine());
		
		Spark.post("/profile", (request, response) -> {
			HashMap<String, Object> a = new HashMap<>();
			
			a.putAll(globalVars);
			a.put("pagetitle", "Профиль");
			a.put("pageurl", "profile");
			
			boolean loggedin = request.session().attributes().contains("uid");
			a.put("userloggedin", loggedin);
			if (loggedin) {
				User user = users.get(Integer.parseInt(request.session().attribute("uid")));
				a.put("username", user.getShortUserFullName());
				a.put("useradmin", user.isAdmin());
				a.put("isPerformer", user.isPerformer());
				a.put("user", user);
				
				String surname = request.queryParams("surname");
				String name = request.queryParams("fullname");
				String patronymic = request.queryParams("patronymic");
				String phone = request.queryParams("phone");
				
				if (surname.isEmpty() || name.isEmpty() || patronymic.isEmpty() || phone.isEmpty()) {
					a.put("error", true);
					a.put("success", false);
				} else {
					user.setSurname(surname);
					user.setName(name);
					user.setPatronymic(patronymic);
					user.setPhone(phone);
					
					users.set(user.getId(), user);
					
					a.put("error", false);
					a.put("success", true);
				}
			} else {
				response.redirect("/login");
				a.put("error", false);
				a.put("success", false);
			}
			
			return new ModelAndView(a, "profile.ftl");
		}, new FreeMarkerEngine());
		
		// ===========================================================
		// ----------------------- Exceptions ------------------------
		// ===========================================================
		
		Spark.exception(NotFoundException.class, (e, request, response) -> {
			response.status(404);
			response.body("<h1>404</h1>");
		});
		
		Spark.exception(NoAccessException.class, (e, request, response) -> {
			response.status(403);
			response.body("<h1>Нет доступа!</h1>");
		});
	}
	
	public Logger getLogger() {
		return log;
	}
	
	public class NotFoundException extends Exception {
		
		private static final long serialVersionUID = -2993266321114783490L;
		
	}
	
	public class NoAccessException extends Exception {
		
		private static final long serialVersionUID = -2993266321114783490L;
		
	}
}
