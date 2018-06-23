package edu.self.web.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import edu.self.model.user.UserWord;
import edu.self.services.google.translate.GoogleTranslate;
import edu.self.services.google.translate.GoogleTranslateLocal;
import edu.self.services.user.UserWordsService;

import static edu.self.utils.Config.CONFIG_ROOT;

@Path("/word")
public class WordController {

    public static void main(String[] args) throws Exception {
        new WordController().getTranslation("name");
    }

    private UserWordsService userWordsService;
    private GoogleTranslate googleTranslate;

    private UserWordsService getUserWordsService() {
        if (userWordsService == null) {
            userWordsService = new UserWordsService(CONFIG_ROOT + "\\user\\words.xml");
        }
        return userWordsService;
    }

    private GoogleTranslate getGoogleTranslate() {
        if (googleTranslate == null) {
            try {
                googleTranslate = new GoogleTranslateLocal(CONFIG_ROOT + "\\google\\translations.xml");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }
        }
        return googleTranslate;
    }

    public WordController() throws FileNotFoundException, JAXBException {
    }

    @POST
    @Path("/translate")
    //@Produces(MediaType.TEXT_PLAIN) //APPLICATION_JSON)
    public void setTranslation(@FormParam("text") String text, @FormParam("translation") String translation) {
        getUserWordsService().setTranslation(text, translation);
    }

    @POST
    @Path("/ignore")
    //@Produces(MediaType.APPLICATION_JSON)
    public void ignore(@FormParam("text") String text, @FormParam("ignorable") Boolean ignorable) {
        getUserWordsService().setIgnorable(text, ignorable);
    }

    @GET
    @Path("/translate")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getTranslation(@QueryParam("text") String text) throws Exception {
        List<String> translations = new ArrayList<String>();
        String translation = getGoogleTranslate().translate(text);
        if (!translation.isEmpty()) {
            translations.add((String) translation);
        }
        return translations;
    }

    @GET
    @Path("/test")
    @Produces("text/plain")
    public String test() {
        String output = "Word Test";
        return output;
    }
}
