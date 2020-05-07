package com.csce31529.AudioWebpagebackend.Models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class DetectedLanguage {
    @Getter
    @Setter
    private String language;
    @Getter @Setter
    private double score;
}
