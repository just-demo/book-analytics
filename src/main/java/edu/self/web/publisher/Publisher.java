package edu.self.web.publisher;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;
import javax.xml.bind.JAXBException;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.ApplicationAdapter;
import com.sun.jersey.api.core.ClassNamesResourceConfig;
import com.sun.net.httpserver.HttpServer;

import edu.self.web.controller.BookController;
import edu.self.web.controller.VocabularyController;
import edu.self.web.controller.WordController;

public class Publisher {
    public static void main(String[] args) throws JAXBException, IOException {
        //publishAsApplication();
        publishAsClasses();
    }

    private static final String URI = "http://localhost:8080/";
    private static final Class<?>[] classes = {ViewResolver.class, BookController.class, WordController.class, VocabularyController.class};

    public static void publishAsClasses() throws IllegalArgumentException, IOException {
        HttpServer server = HttpServerFactory.create(URI, new ClassNamesResourceConfig(classes));
        server.start();
    }

    public static void publishAsApplication() throws IllegalArgumentException, IOException {
        HttpServer server = HttpServerFactory.create(URI, new ApplicationAdapter(new Application() {
            @Override
            public Set<Class<?>> getClasses() {
                return new HashSet<Class<?>>(Arrays.asList(classes));
            }
        }));
        server.start();
    }
}
