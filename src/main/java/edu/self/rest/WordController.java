package edu.self.rest;

import edu.self.services.TranslationService;
import edu.self.services.UserPreferenceService;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@Component
@Path("/word")
public class WordController {
    private UserPreferenceService userPreferenceService = new UserPreferenceService();
    private TranslationService translationService = new TranslationService();

    @POST
    @Path("/translate")
    //@Produces(MediaType.TEXT_PLAIN) //APPLICATION_JSON)
    public void setTranslation(@FormParam("text") String text, @FormParam("translation") String translation) {
        userPreferenceService.setTranslation(text, translation);
    }

    @POST
    @Path("/ignore")
    //@Produces(MediaType.APPLICATION_JSON)
    public void ignore(@FormParam("text") String text, @FormParam("ignorable") Boolean ignorable) {
        userPreferenceService.setIgnorable(text, ignorable);
    }

    @GET
    @Path("/translate")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getTranslation(@QueryParam("text") String text) {
        String translation = translationService.translate(text);
        return translation.isEmpty() ? emptyList() : singletonList(translation);
    }

    @GET
    @Path("/test")
    @Produces("text/plain")
    public String test() {
        return "Word Test";
    }
}
