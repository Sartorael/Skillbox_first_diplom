    package search_engine.settings;
    import lombok.Getter;
    import lombok.Setter;
    import org.springframework.boot.context.properties.ConfigurationProperties;
    import org.springframework.stereotype.Component;

    import java.util.List;

    @Getter
    @Setter
    @Component
    @ConfigurationProperties(prefix = "config")
    public class SearchSettings {

        private List<Site> sites;
        private String userAgent;
        private String referrer;
        private String webinterfaceLogin;
        private String webinterfacePassword;
        private String webinterface;
    }
