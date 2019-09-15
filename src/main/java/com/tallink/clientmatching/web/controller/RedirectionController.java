package com.tallink.clientmatching.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@ApiIgnore
public class RedirectionController{

    @RequestMapping(method = GET, path = "swagger")
    public String getApi() {
        return "redirect:swagger-ui.html";
    }

    @RequestMapping(method = GET, path = "/")
    public String getRoot() {
        return getApi();
    }

}
