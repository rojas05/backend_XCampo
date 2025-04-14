package com.rojas.dev.XCampo.config;

import org.springframework.stereotype.Component;
import java.util.function.Function;

@Component
public class MyLambdaHandler implements Function<String, String> {
    @Override
    public String apply(String input) {
        return "Hola desde Lambda, recibiste: " + input;
    }
}

