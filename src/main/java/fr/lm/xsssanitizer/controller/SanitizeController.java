package fr.lm.xsssanitizer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/sanitize")
@RequiredArgsConstructor
public class SanitizeController {

    @ResponseBody
    @PostMapping
    public Mono<DemoForm> create(@RequestBody @Valid final DemoForm form) {
        return Mono.just(form);
    }
}
