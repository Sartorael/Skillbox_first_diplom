    package search_engine.service;
    import org.springframework.http.ResponseEntity;

    public interface SearchService {

        ResponseEntity searchPages(String query, String site, int offset, int limit);
    }
