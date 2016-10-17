package com.company;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    static HashMap<String, User> usersHash = new HashMap<>();
    static ArrayList<Message> messagesArray = new ArrayList<>();

    public static void main(String[] args) {

        Spark.get(
                "/",
                (request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("userName");
                    User user = usersHash.get(name);
                    HashMap m = new HashMap();
                    if (user != null) {
                        m.put("name", user.name);
                        m.put("messages",user.messages);
                    }
                    return new ModelAndView(m, "home.html");
                },
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/login",
                (request, response) -> {
                    String name = request.queryParams("userName");
                    String pass = request.queryParams("userPass");
                    User user = usersHash.get(name);
                    if (user == null){
                        if (pass.isEmpty()) {
                            response.redirect("/");
                            return null;
                        }
                        user = new User(name, pass, null);
                        usersHash.put(name,user);
                    }
                    else if (!pass.equals(user.password)) {
                        response.redirect("/");
                        return null;
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
                    Session session = request.session();
                    String name = session.attribute("userName");
                    User user = usersHash.get(name);
                    Message message = new Message(request.queryParams("message"));
                    messagesArray.add(message);
                    //usersHash.put(name, message);
                    session.attribute("userName", name);
                    response.redirect("/");
                    return null;
                }
        );
        Spark.post(
                "/edit-message",
                (request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("userName");
                    User user = usersHash.get(name);
                    Message updatedMessage = new Message(request.queryParams("editMessage"));
                    String id = request.queryParams("messageId");
                    user.messages.remove(Integer.parseInt(id) - 1);
                    //user.messages.add(Integer.valueOf(id) - 1, updatedMessage);
                    session.attribute("userName", name);
                    response.redirect("/");
                    return null;
                }
        );
        Spark.post(
                "/delete-message",
                (request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("userName");
                    User user = usersHash.get(name);
                    String id = request.queryParams("dMessageId");
                    user.messages.remove(Integer.parseInt(id)-1);
                    session.attribute("userName", name);
                    response.redirect("/");
                    return null;
                }
        );
        Spark.post(
                "/logout",
                (request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect(request.headers("Referer"));
                    return null;
                }
        );
    }
}