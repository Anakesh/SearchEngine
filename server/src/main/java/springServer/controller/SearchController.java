package springServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ranker.WebPageWeight;
import springServer.entity.Search;

@Controller
public class SearchController {
    @GetMapping("/")
    public String searchForm(Model model) {
        model.addAttribute("search", new Search());
        model.addAttribute("webPageWeight",new WebPageWeight());
        return "index";
    }

    @PostMapping("/")
    public String searchSubmit(@ModelAttribute Search search) {
        search.findResult();
        if(search.isEmpty())
            return "empty";
        else
            return "results";
    }

}
