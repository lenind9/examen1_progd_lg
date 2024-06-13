package com.distribuida;

import com.distribuida.db.Book;
import com.distribuida.servicios.IBookService;
import com.google.gson.Gson;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import jakarta.enterprise.inject.spi.CDI;
import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.spi.ContainerLifecycle;

public class Main {
    private static ContainerLifecycle lifecycle = null;
    static IBookService service;
    static Gson gson = new Gson();

    static void findById(ServerRequest req, ServerResponse res) {
        res.send(gson.toJson(service.findById(Integer.valueOf(req.path().pathParameters().get("id")))));
    }

    static void findAll(ServerRequest req, ServerResponse res) {
        res.send(gson.toJson(service.findAll()));
    }

    static void insert(ServerRequest req, ServerResponse res) {
        res.send(gson.toJson(service.insert(gson.fromJson(req.content().as(String.class), Book.class))));
    }

    static void update(ServerRequest req, ServerResponse res) {
        Book book = gson.fromJson(req.content().as(String.class), Book.class);
        book.setId(Integer.valueOf(req.path().pathParameters().get("id")));
        res.send(gson.toJson(service.update(book)));
    }

    static void delete(ServerRequest req, ServerResponse res) {
        service.delete(Integer.valueOf(req.path().pathParameters().get("id")));
        res.send("Eliminado");
    }

    public static void main(String[] args) {
        lifecycle = WebBeansContext.currentInstance().getService(ContainerLifecycle.class);
        lifecycle.startApplication(null);

        service = CDI.current().select(IBookService.class).get();
        service.findAll().stream().forEach(System.out::println);

        WebServer server = WebServer
                .builder()
                .port(8080)
                .routing(builder -> builder
                        .get("/books/{id}", Main::findById)
                        .get("/books", Main::findAll)
                        .post("/books", Main::insert)
                        .put("/books/{id}", Main::update)
                        .delete("/books/{id}", Main::delete)
                ).build();

        server.start();
    }
}