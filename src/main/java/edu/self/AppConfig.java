package edu.self;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static edu.self.utils.FileUtils.readResourceFile;
import static edu.self.utils.JsonUtils.fromJson;

@Configuration
public class AppConfig {
    @Bean
    public Set<String> words() {
        return fromJson(readResourceFile("words.json"), new TypeReference<Set<String>>() {
        });
    }

    @Bean
    public Map<String, Set<String>> groups() {
        Set<Set<String>> groups = fromJson(readResourceFile("groups.json"), new TypeReference<Set<Set<String>>>() {
        });
        Map<String, Set<String>> wordGroups = new HashMap<>();
        groups.forEach(group -> group.forEach(word -> wordGroups.put(word, group)));
        return wordGroups;
    }

    @Bean
    public Map<String, List<String>> translations() {
        return fromJson(readResourceFile("translations.json"), new TypeReference<Map<String, List<String>>>() {
        });
    }


}
