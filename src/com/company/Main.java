package com.company;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    static User user;
    static HashMap<String, User> users = new HashMap<>();

    public static void main(String[] args) {

        Spark.get(
                "/",
                (request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("userName");

                    return new ModelAndView(name, "home.html");
                },
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/login",
                (request, response) -> {
                    String name = request.queryParams("userName");
                    User user = users.get(name);
                    if(user == null){
                        user = new User(name, null);
                        users.put(name, user);
                    }

                    Session session = request.session();
                    session.attribute("userName", name);
                    response.redirect("/");
                    return null;
                }
        );
        Spark.post(
                "/create-message",
                (request, response) -> {
                    String message = request.queryParams("text");
                    Session session = request.session();
                    String name = session.attribute("userName");
                    //users.put(name, message);
                    response.redirect("/");
                    return null;
                }
        );
        Spark.post(
                "/logout",
                (request, response) -> {
                    user = null;
                    response.redirect("/");
                    return null;
                }
        );
    }
}
