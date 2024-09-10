package com.cht.network.monitoring.web.util;
import com.cht.network.monitoring.statuscode.StatusCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for alert Header.
 *
 * @author mrcutejacky
 * @version 1.0
 */
public class HeaderUtils {

    public final static String HEADER_ALERT = "x-network-alert";

    public final static String HEADER_PARAMS = "x-network-params";

    /**
     * create alert
     *
     * @param statusCode StatusCode
     * @return HttpHeaders
     */
    public static HttpHeaders createAlert(StatusCode statusCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_ALERT, statusCode.getName());
        try {
            headers.add(HEADER_PARAMS, URLEncoder.encode(
                    new ObjectMapper().writeValueAsString(statusCode.getArgs()), StandardCharsets.UTF_8));
        } catch (JsonProcessingException ignore) {
        }

        return headers;
    }

    public static HttpHeaders createAlert(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_ALERT, message);

        return headers;
    }
}
