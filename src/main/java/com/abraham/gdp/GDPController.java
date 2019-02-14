package com.abraham.gdp;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        return gdprepos.findAll(Sort.unsorted());
    }

    @GetMapping("/economy")
    public List<GDP> allGDP() {
        // order by gdp
        return gdprepos.findAll(Sort.unsorted().ascending());
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

    @GetMapping("/total/{name}")

    @PostMapping("/gdp")
}
