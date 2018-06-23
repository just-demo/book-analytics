package edu.self.services.google;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import edu.self.utils.WebUtils;

public class GoogleAPI {
    public String query(String text) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.25) Gecko/20111212 Firefox/3.6.25 GTB7.1");
        try {
            text = URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String path = "http://translate.google.com.ua/translate_a/t?client=t&hl=en&sl=en&tl=ru&text=" + text;
        String result = WebUtils.load(path, headers);
        return result;
    }
}
