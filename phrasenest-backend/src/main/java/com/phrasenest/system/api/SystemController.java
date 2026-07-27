package com.phrasenest.system.api;

import com.phrasenest.shared.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/public/system")
public class SystemController {

    @GetMapping
    public ApiResponse<Map<String, String>> systemInformation() {
        return ApiResponse.success(
                "PhraseNest backend is running.",
                Map.of(
                        "application", "PhraseNest",
                        "apiVersion", "v1",
                        "status", "UP"
                )
        );
    }
}
