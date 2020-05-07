package com.csce31529.AudioWebpagebackend.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TextAnalysisResponseDocument {

    @Getter @Setter
    @JsonProperty("id")
    private String documentID;

    @Getter @Setter
    @JsonProperty("score")
    private Double score;
}
