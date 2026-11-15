package ng.safebanking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("app/Testing")
public class AccountController {
    @GetMapping
    public Map<String, String> testing(){
        return Map.of("status", "I see you", "app", "safeBanking");
    }
}
