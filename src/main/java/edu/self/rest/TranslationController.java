package edu.self.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.springframework.web.cors.CorsConfiguration.ALL;

@CrossOrigin(ALL)
@RestController
@RequestMapping("/translations")
public class TranslationController {
    @Autowired
    @Qualifier("translations")
    private Map<String, List<String>> translations;

    @GetMapping
    public Map<String, List<String>> getTranslations() {
        return translations;
    }
}
