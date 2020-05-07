package com.csce31529.AudioWebpagebackend.Models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class Translation {
    @Getter @Setter
    private String text;
    @Getter @Setter
    private String to;
}
