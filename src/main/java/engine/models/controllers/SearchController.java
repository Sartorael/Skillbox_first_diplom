package engine.models.controllers;

import engine.models.Request;
import engine.service.SearchServ;
import engine.service.response.RespServ;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class SearchController {

    private final SearchServ search;

    public SearchController(SearchServ search) {
        this.search = search;
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(
            @RequestParam(name="query", required=false, defaultValue="") String query,
            @RequestParam(name="site", required=false, defaultValue="") String site,
            @RequestParam(name="offset", required=false, defaultValue="0") int offset,
            @RequestParam(name="limit", required=false, defaultValue="0") int limit) throws IOException {
        RespServ service = search.getResponse(new Request(query), site, offset, limit);
        return ResponseEntity.ok (service);
    }
}
