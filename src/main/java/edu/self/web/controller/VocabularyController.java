package edu.self.web.controller;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.self.model.user.UserWord;
import edu.self.services.user.UserWordsService;

import static edu.self.utils.Config.CONFIG_ROOT;

@Path("vocabulary")
public class VocabularyController {
    private UserWordsService userWordsService;

    public VocabularyController() {
        userWordsService = new UserWordsService(CONFIG_ROOT + "\\user\\words.xml");
    }

    @GET
    @Path("data.txt")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getVocabulary() {
        Map<String, UserWord> words = userWordsService.getWords();
        StringBuffer buffer = new StringBuffer();
        for (String text : words.keySet()) {
            UserWord word = words.get(text);
            if (!word.isIgnorable() && word.getTranslation() != null && !word.getTranslation().isEmpty()) {
                buffer.append(text + "=" + word.getTranslation() + "\n");
            }
        }
        return Response.status(200).entity(buffer.toString()).header("Content-type", "text/plain; charset=utf-8").build();
    }
}
