package com.csce31529.AudioWebpagebackend.Models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@ToString
@NoArgsConstructor
public class TextAnalysisResponse {

    @Getter @Setter
    ArrayList<TextAnalysisResponseDocument> documents;
}
