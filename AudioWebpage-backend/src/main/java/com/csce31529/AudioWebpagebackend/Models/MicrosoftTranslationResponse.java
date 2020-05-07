package com.csce31529.AudioWebpagebackend.Models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class MicrosoftTranslationResponse {
    @Getter
    @Setter
    private DetectedLanguage detectedLanguage;
    @Getter @Setter
    private ArrayList<Translation> translations;


    MicrosoftTranslationResponse(){
        detectedLanguage = new DetectedLanguage();
        translations =  new ArrayList<>();
    }
}
