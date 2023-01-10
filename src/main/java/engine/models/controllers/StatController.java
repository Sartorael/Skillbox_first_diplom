package engine.models.controllers;

import engine.service.StatisticServ;
import engine.service.response.StatRespServ;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatController {

    private final StatisticServ statistic;

    public StatController(StatisticServ statistic) {
        this.statistic = statistic;
    }

    @GetMapping("/statistics")
    public ResponseEntity<Object> getStatistics(){
        StatRespServ stat = statistic.getStatistic();
        return ResponseEntity.ok (stat);
    }
}
