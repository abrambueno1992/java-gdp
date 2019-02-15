package com.abraham.gdp;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<GDP> allName() {
        // return in alphabetical order
        return gdprepos.findAll().stream().sorted((e1, e2) -> e1.getCountry().compareToIgnoreCase(e2.getCountry())).collect(Collectors.toList());
//        return newList;

    }

    @GetMapping("/economy")
    public List<GDP> allGDP() {
        // order by gdp
        return gdprepos.findAll().stream().sorted((e1, e2) -> (e2.getGdp().compareTo(e1.getGdp()))).collect(Collectors.toList());
    }

    @GetMapping("/total")
    public ObjectNode sumGDPs() {
        List<GDP> gdps = gdprepos.findAll();
        Long total = 0L;
        for (GDP g : gdps) {
            total += g.getGdp();
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode totalGDP = mapper.createObjectNode();
        totalGDP.put("id", 0);
        totalGDP.put("country", "Total");
        totalGDP.put("gdp", total);
        totalGDP.put("comment", "GDP Countries");

        GDPLog message = new GDPLog("Checked Total GDP");
        rt.convertAndSend(GdpApplication.QUEUE_NAME, message.toString());
        log.info("Message sent");
        return totalGDP;
    }

    @GetMapping("/total/{country}")
    public GDP findOne(@PathVariable String country) {
        for (GDP g : gdprepos.findAll()) {
            System.out.println( (g.getCountry().toLowerCase() == country.toLowerCase()));
            if (g.getCountry().toLowerCase().equals(country.toLowerCase())) {
                return g;
            }
        }

        return new GDP("NONE", 0L);
    }

    @PostMapping("/gdp")
    public List<GDP> newGDP(@RequestBody List<GDP> newGDPs) {
        return gdprepos.saveAll(newGDPs);
    }
}
