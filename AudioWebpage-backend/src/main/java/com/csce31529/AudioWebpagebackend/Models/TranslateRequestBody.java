package com.csce31529.AudioWebpagebackend.Models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class TranslateRequestBody {
    @Getter @Setter
    private String text;

    @Getter @Setter
    private String destLang;

}
