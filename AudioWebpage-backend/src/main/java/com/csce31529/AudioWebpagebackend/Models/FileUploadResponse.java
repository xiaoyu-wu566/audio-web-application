package com.csce31529.AudioWebpagebackend.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@AllArgsConstructor
public class FileUploadResponse {



    @Getter
    @Setter
    @JsonProperty("filename")
    private String filename;

    @Getter
    @Setter
    @JsonProperty("hash")
    private String hash;
}

