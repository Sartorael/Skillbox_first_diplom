    package search_engine.controller;

    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import search_engine.dto.statistics.StatisticsResponse;
    import search_engine.service.IndexService;
    import search_engine.service.SearchService;
    import search_engine.service.StatisticsService;

    @RestController
    @RequestMapping("/api")
    public class Controller {

        private final StatisticsService statisticsService;
        private final IndexService indexService;
        private final SearchService searchService;

        public Controller(StatisticsService statisticsService, IndexService indexService, SearchService searchService) {

            this.statisticsService = statisticsService;
            this.indexService = indexService;
            this.searchService = searchService;
        }
        @GetMapping("/statistics")
        public ResponseEntity<StatisticsResponse> statistics() {

            return ResponseEntity.ok(statisticsService.getStatistics());
        }
        @GetMapping("/startIndexing")
        public ResponseEntity startIndexing() {

            return indexService.startIndexing();
        }

        @GetMapping("/stopIndexing")
        public ResponseEntity stopIndexing() {

            return indexService.stopIndexing();
        }

        @PostMapping("/indexPage")
        public ResponseEntity indexPage(@RequestParam String url) {

            return indexService.indexPage(url);
        }

        @GetMapping("/search")
        public ResponseEntity search(@RequestParam String query, String site, int offset, int limit) {

            return searchService.searchPages(query, site, offset, limit);
        }
    }
