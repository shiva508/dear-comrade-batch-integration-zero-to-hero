package com.comrade.service;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class CurrentAffairsService {

    public List<String> names(){
        return IntStream.range(1,8).mapToObj(value -> String.format("Shiva_%s",value) ).collect(Collectors.toList());
    }
}
