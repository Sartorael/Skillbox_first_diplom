    package search_engine.dto.statistics;
    import com.fasterxml.jackson.annotation.JsonIgnore;
    import lombok.Data;
    import search_engine.model.PageData;

    @Data
    public class PageInfoItem {

        private String site;
        private String siteName;
        private String uri;
        private String title;
        private String snippet;
        private float relevance;
        @JsonIgnore
        private PageData pageData;
    }
