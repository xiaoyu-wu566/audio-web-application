package com.csce31529.AudioWebpagebackend.Models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@ToString
public class TextAnalysisDocuments {

    @Getter @Setter
    ArrayList<TextAnalysisDocument> documents  ;
    public TextAnalysisDocuments(){
        documents =  new ArrayList<>();
    }
}
