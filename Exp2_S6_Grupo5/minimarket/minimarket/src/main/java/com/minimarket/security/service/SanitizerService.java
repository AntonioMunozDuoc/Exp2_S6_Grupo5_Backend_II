package com.minimarket.security.service;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;

@Service
public class SanitizerService {

    public String sanitize(String input) {

        if (input == null) {
            return null;
        }

        return Jsoup.clean(
                input,
                Safelist.none()
        );
    }

}