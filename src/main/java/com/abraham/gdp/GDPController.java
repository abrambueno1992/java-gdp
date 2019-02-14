package com.abraham.gdp;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class GDPController {
    private final GDPRepository gdprepos;
    private final RabbitTemplate rt;

    public GDPController(GDPRepository gdprepos, RabbitTemplate rt) {
        this.gdprepos = gdprepos;
        this.rt = rt;
    }

    @GetMapping("/names")

    @GetMapping("/economy")

    @GetMapping("/total")

    @GetMapping("/total/{name}")
}
