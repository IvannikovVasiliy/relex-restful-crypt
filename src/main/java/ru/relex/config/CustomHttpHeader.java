package ru.relex.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class CustomHttpHeader {

    static HttpHeaders httpHeaders = new HttpHeaders();

    public static HttpHeaders getHttpHeader() {
        return httpHeaders;
    }

    public static void setHttpHeaders(MediaType mediaType) {
        httpHeaders.setContentType(mediaType);
    }
}
