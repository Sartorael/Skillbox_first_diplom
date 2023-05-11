    package search_engine.controller;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.RequestMapping;
    import search_engine.repository.SiteRepository;

    @Controller
    public class DefaultController {

        @Autowired
        private SiteRepository siteRepository;
        @RequestMapping("/")
        public String index(Model model) {

            return "index";
        }
    }
