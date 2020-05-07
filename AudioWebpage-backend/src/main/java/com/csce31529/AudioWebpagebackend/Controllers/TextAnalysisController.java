package com.csce31529.AudioWebpagebackend.Controllers;

import com.csce31529.AudioWebpagebackend.Models.TextAnalysisDocument;
import com.csce31529.AudioWebpagebackend.Models.TextAnalysisDocuments;
import com.csce31529.AudioWebpagebackend.Models.TextAnalysisResponse;
import com.csce31529.AudioWebpagebackend.Service.TextAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@RestController
@RequestMapping("/text")
public class TextAnalysisController {

    @Autowired
    TextAnalysisService textAnalysisService;

    @PostMapping("/sentiment")
    @ResponseBody
    public TextAnalysisResponse stub(@RequestBody TextAnalysisDocuments docs){
        System.out.println(docs.getDocuments());
        return textAnalysisService.getDocumentsScores(docs);
    }

    @PostMapping("/sentimentforfile")
    @ResponseBody
    @CrossOrigin
    public TextAnalysisResponse getFileSentimentScores(@RequestParam String fileId){
        System.out.println(fileId);
        return textAnalysisService.getFileScores(fileId);
    }
}
