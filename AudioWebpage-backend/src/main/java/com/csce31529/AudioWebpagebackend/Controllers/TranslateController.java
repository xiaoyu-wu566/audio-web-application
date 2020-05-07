package com.csce31529.AudioWebpagebackend.Controllers;

import com.csce31529.AudioWebpagebackend.Models.TranslateRequestBody;
import com.csce31529.AudioWebpagebackend.Models.MicrosoftTranslationResponse;
import com.csce31529.AudioWebpagebackend.Models.TranslationResponse;
import com.csce31529.AudioWebpagebackend.Service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/translate")
public class TranslateController {

    @Autowired
    TranslationService translationService;

    @ResponseBody
    @RequestMapping("/stub")
    public ArrayList<MicrosoftTranslationResponse> translateText(@RequestBody TranslateRequestBody body){
        System.out.println("destLang: " + body.getDestLang() + " | text: " +body.getText());
        return translationService.translateText(body.getDestLang(),body.getText());
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/filebyid")
    public ResponseEntity translateFileByID(@RequestParam String fileId, @RequestParam String to){
        return ResponseEntity.status(HttpStatus.OK).body(new TranslationResponse(translationService.translateFile(fileId,to)));
    }
}
