        package search_engine.dto.response;
        import lombok.Data;
        import search_engine.dto.statistics.PageInfoItem;

        import java.util.List;

        @Data
        public class SearchResponse {

            private boolean result;
            private int count;
            private List<PageInfoItem> data;
        }
