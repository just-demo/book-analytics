package self.edu.rest;

import self.edu.services.TranslationService;
import self.edu.services.UserPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController
@RequestMapping("/rest/word")
public class WordController {
    @Autowired
    private TranslationService translationService;

    @Autowired
    private UserPreferenceService userPreferenceService;

    @GetMapping("/translate")
    public List<String> getTranslation(@RequestParam("text") String text) {
        String translation = translationService.translate(text);
        return translation.isEmpty() ? emptyList() : singletonList(translation);
    }

    @PostMapping("/translate")
    public void setTranslation(@RequestParam("text") String text, @RequestParam("translation") String translation) {
        userPreferenceService.setTranslation(text, translation);
    }

    @PostMapping("/ignore")
    public void ignore(@RequestParam("text") String text, @RequestParam("ignorable") Boolean ignorable) {
        userPreferenceService.setIgnorable(text, ignorable);
    }

    @GetMapping(path = "/test", produces = TEXT_PLAIN_VALUE)
    public String test() {
        return "Word Test";
    }
}
