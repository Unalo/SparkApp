import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;
public class HelloWorld {

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

        public static void main(String[] args) {
            staticFiles.location("/public");

            port(getHerokuAssignedPort());
             Map<String, Integer> greetedUsers = new HashMap<>();
            Map<String, Object> dataMap = new HashMap<>();

            post("/hello", (req, res) -> {
                // get form data values
                String name = req.queryParams("firstName").substring(0,1).toUpperCase() + req.queryParams("firstName").substring(1).toLowerCase();
                String lang = req.queryParams("language");
                Map<String, Integer> counterMap = new HashMap<>();

//                if (greetedUsers.containsKey(name)) {
//                    greetedUsers.put(name, greetedUsers.get(name) + 1);
//                }

                if (lang.equals("English")) {
                    dataMap.put("message", "Hello ");
                    greetedUsers.put(name, 1);
                } else if (lang.equals("Isixhosa")) {
                    dataMap.put("message", "Molo ");
                    greetedUsers.put(name, 1);
                } else if (lang.equals("Afrikaans")) {
                    dataMap.put("message", "Goer MOre " );
                    greetedUsers.put(name, 1);
                }

                dataMap.put("name", name);

                // put the values from the form for Handlebars to use

                return new ModelAndView(dataMap, "hello.hbs");

            }, new HandlebarsTemplateEngine());

            get("/", (req, res) -> {
                int counter = greetedUsers.size();
                dataMap.put("greeted", counter);
                return new ModelAndView(dataMap, "index.hbs");
            }, new HandlebarsTemplateEngine());

            get("/users", (req, res) -> {
                List<String> greet = new ArrayList();
                for (String name : greetedUsers.keySet()) {
                    greet.add(name);
                }
                dataMap.put("users", greet);
                return new ModelAndView(dataMap, "greeted.hbs");
            }, new HandlebarsTemplateEngine());
        }
    }

