package io.proglib;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/greet")
public class GreetingController {

    @GetMapping
    public String greet(@RequestParam("name") String name) {
        return "Hello, " + name;
    }

    @GetMapping("/full")
    public String fullGreeting(@RequestParam("name") String name, @RequestParam("surname") String surname) {
        return "Nice to meet you, " + name + " " + surname;
    }

    @GetMapping("/{name}")
    public String greetWithPathVariable(@PathVariable("name") String name) {
        return "Hello, " + name;
    }

    @GetMapping("/{name}/{surname}")
    public String greetWithPathSeveralPathVariables(@PathVariable("name") String name, @PathVariable("surname") String surname) {
        return "Nice to meet you, " + surname + " " + name;
    }

}
