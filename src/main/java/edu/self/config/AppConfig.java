package edu.self.config;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

import static edu.self.utils.FileUtils.readResourceFile;
import static edu.self.utils.JsonUtils.fromJson;
import static java.util.stream.Collectors.toList;

@Configuration
public class AppConfig {
    @Bean
    public Set<String> words() {
        return fromJson(readResourceFile("words.json"), new TypeReference<Set<String>>() {
        });
    }

    @Bean
    public Set<Set<String>> groups() {
        return fromJson(readResourceFile("groups.json"), new TypeReference<Set<Set<String>>>() {
        });
    }

    @Bean
    public Map<String, Set<String>> wordGroups(@Qualifier("groups") Set<Set<String>> groups) {
        Map<String, Set<String>> wordGroups = new HashMap<>();
        groups.forEach(group -> group.forEach(word -> wordGroups.put(word, group)));
        return wordGroups;
    }

    @Bean
    public Map<String, List<String>> translations() {
        return fromJson(readResourceFile("translations.json"), new TypeReference<Map<String, List<String>>>() {
        });
    }

    public static void main(String[] args) {
        System.out.println(new AppConfig().translations().entrySet().stream()
                .map(e -> Pair.of(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(AppConfig::pairSize))
                .collect(toList()).get(0));
    }

    static long pairSize(Pair<String, List<String>> pair) {
        return -pair.getValue().size();
    }
}
