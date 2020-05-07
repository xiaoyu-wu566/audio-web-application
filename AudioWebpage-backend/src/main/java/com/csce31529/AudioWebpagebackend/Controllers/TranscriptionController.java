package com.csce31529.AudioWebpagebackend.Controllers;

import com.csce31529.AudioWebpagebackend.Models.TranscriptionResponse;
import com.csce31529.AudioWebpagebackend.Service.TranscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/transcription")
public class TranscriptionController {

    @Autowired
    TranscriptionService transcriptionService;


	@CrossOrigin
    @PostMapping("/stub")
    @ResponseBody
    public ResponseEntity transcriptionStub(@RequestParam String fileId){
        return ResponseEntity.status(HttpStatus.OK).body(transcriptionService.transcribeFile(fileId));
    }


}
