package com.csce31529.AudioWebpagebackend.Models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
public class TextAnalysisDocument {
    @Getter @Setter
    @JsonProperty("language")
    private String language;

    @Getter @Setter
    @JsonProperty("id")
    private String documentID;

    @Getter @Setter
    @JsonProperty("text")
    private String text;

}
