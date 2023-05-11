    package search_engine.service;
    import org.springframework.http.ResponseEntity;

    public interface IndexService {

        ResponseEntity startIndexing();
        ResponseEntity stopIndexing();
        ResponseEntity indexPage(String url);
    }
