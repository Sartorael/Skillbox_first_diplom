    package search_engine.dto.response;
    import lombok.Data;

    @Data
    public class ErrorResponse {

        private boolean result = false;
        private String error;
        public ErrorResponse(String error) {

            this.error = error;
        }
    }
