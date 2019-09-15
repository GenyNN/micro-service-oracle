package com.tallink.clientmatching.web.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tallink.clientmatching.service.ClientMatcher;
import com.tallink.clientmatching.service.ClientSearchFactory;
import org.omg.PortableInterceptor.ObjectReferenceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("api")
public class ClientMatchingController {

    private final ClientSearchFactory clientSearchFactory;

    @Autowired
    public ClientMatchingController(ClientSearchFactory factory) {
        this.clientSearchFactory = factory;
    }

    @InitBinder // To treat empty strings as nulls
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(method = GET, path = "list")
    public Map<String, Object> list(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phoneNumber,
            @RequestParam(value = "countryCode", required = false) String countryCode,
            @RequestParam(value = "clubOne", required = false) String clubOneNumber,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName) {
        return new ClientMatcher(clientSearchFactory, email, phoneNumber,
                countryCode, clubOneNumber, firstName, lastName).match();
    }

    @RequestMapping(method = GET, path = "best")
    public Object best(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phoneNumber,
            @RequestParam(value = "countryCode", required = false) String countryCode,
            @RequestParam(value = "clubOne", required = false) String clubOneNumber,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName) {
        Map<String, Object> matсh = new ClientMatcher(clientSearchFactory, email, phoneNumber,
                countryCode, clubOneNumber, firstName, lastName).match();
        Gson gson = new Gson();
        String res = gson.toJson(matсh);
        return matсh.get(ClientMatcher.MAP_BEST_MATCH);
    }
}
