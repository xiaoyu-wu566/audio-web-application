package com.csce31529.AudioWebpagebackend.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;

@ToString
public class TranscriptionResponse {


    public TranscriptionResponse(){
        documents = new ArrayList<>();
    }

    @Getter
    @Setter
    @JsonProperty("text")
    private ArrayList<String> documents;


}
