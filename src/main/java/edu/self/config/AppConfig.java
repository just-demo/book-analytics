package edu.self.config;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static edu.self.utils.FileUtils.readResourceFile;
import static edu.self.utils.JsonUtils.fromJson;

@Configuration
public class AppConfig {
    @Bean
    public Set<Set<String>> words() {
        return fromJson(readResourceFile("words.json"), new TypeReference<Set<Set<String>>>() {
        });
    }

    @Bean
    public Map<String, List<String>> translations() {
        return fromJson(readResourceFile("translations.json"), new TypeReference<Map<String, List<String>>>() {
        });
    }
}
