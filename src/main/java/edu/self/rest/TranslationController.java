package edu.self.rest;

import edu.self.services.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static org.springframework.web.cors.CorsConfiguration.ALL;

@CrossOrigin(ALL)
@RestController
@RequestMapping("/translations")
public class TranslationController {

    @Autowired
    private TranslationService translationService;

    @PostMapping
    public Map<String, List<String>> getTranslations(@RequestBody Collection<String> words) {
        Map<String, List<String>> translations = words.stream()
                .collect(toMap(Function.identity(), translationService::getTranslations));
        translations.values().removeIf(CollectionUtils::isEmpty);
        return translations;
    }
}
