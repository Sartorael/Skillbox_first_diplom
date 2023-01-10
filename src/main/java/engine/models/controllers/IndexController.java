package engine.models.controllers;

import engine.service.IndexingServ;
import engine.service.response.RespServ;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class IndexController {

    private final IndexingServ index;

    public IndexController(IndexingServ index) {
        this.index = index;
    }

    @GetMapping("/startIndexing")
    public ResponseEntity<Object> startIndexingAll() {
        RespServ response = index.startIndexingAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stopIndexing")
    public ResponseEntity<Object> stopIndexingAll() {
        RespServ response = index.stopIndexing();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/indexPage")
    public ResponseEntity<Object> startIndexingOne(
            @RequestParam(name="url", required=false, defaultValue=" ") String url) {
        RespServ response = index.startIndexingOne(url);
        return ResponseEntity.ok(response);
    }
}
