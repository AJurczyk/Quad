package io.api.rest;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String start(@RequestParam String name) {
        return name;
    }

}