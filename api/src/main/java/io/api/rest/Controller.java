package io.api.rest;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
public class Controller {

    @RequestMapping(value = "/hello")
    public Hello start(@RequestParam String name) {
        return new Hello(666, name.toUpperCase());
    }

}