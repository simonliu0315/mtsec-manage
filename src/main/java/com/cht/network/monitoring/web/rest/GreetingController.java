package com.cht.network.monitoring.web.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Tag(name = "ODS302W", description = "主題列表")
@Controller
public class GreetingController {

    private static final Logger log = LoggerFactory.getLogger(GreetingController.class);

    @CrossOrigin(origins = "*")
    @Operation(summary = "依條件查詢主題")
    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model, HttpServletResponse response) {
        model.addAttribute("name", name);
        response.addHeader("Access-Control-Allow-Origin", "*");
        log.info("AAAAAAAAAAAAAAAAAAAAAA");
        return "greeting";
    }
}
