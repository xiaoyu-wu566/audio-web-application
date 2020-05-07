package com.csce31529.AudioWebpagebackend.Service;

import com.csce31529.AudioWebpagebackend.Config.FileConfig;
import com.csce31529.AudioWebpagebackend.Models.TranscriptionResponse;
import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TranscriptionService {
    private static final String SPEECH_API_KEY = "5afe14c5e76d42068354a099d9d1fc7c";
    private static final String SPEECH_API_REGION = "southcentralus";

    public static final Short TRANSCRIBED = 0;
    public static final Short TRANSCRIBING = 1;

    private static HashMap<String, Short> transcribedFileStatus = new HashMap<>();
    private static ReentrantLock statusLock =  new ReentrantLock();
    private final ReentrantLock blockingLock =  new ReentrantLock();
    private String transcribed_text = new String();
    private boolean isBlocked = false;

    private final
    FileStorageService fileStorageService;

    public TranscriptionService(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }


    public TranscriptionResponse transcribeFile(String fileId){
        String audioPath = FileConfig.UPLOAD_DIR + "audio/" + fileId;
        String textPath = FileConfig.UPLOAD_DIR + "text/" + fileId;
        System.out.println(audioPath);
        TranscriptionResponse response = new TranscriptionResponse();

        if(fileStorageService.transcriptionExists(fileId)){
            return fileStorageService.fetchTranscription(fileId);
        }
        try {

            SpeechConfig speechConfig = SpeechConfig.fromSubscription(SPEECH_API_KEY, SPEECH_API_REGION);
            try{
                AudioConfig audioInput = AudioConfig.fromWavFileInput(audioPath);

                SpeechRecognizer reco = new SpeechRecognizer(speechConfig, audioInput);
                reco.recognized.addEventListener( (i,event)->{
                    try{
                        fileStorageService.storeTranscription(fileId, event.getResult().getText());
                        transcribed_text += event.getResult().getText();
                        transcribed_text += "\n";
                        response.getDocuments().add(event.getResult().getText());

                        new File(FileConfig.UPLOAD_DIR + "audio/" + fileId).delete();
                    }
                    catch (Exception e){
                        System.err.println(e.getMessage());
                        System.err.println("Unable to open file ID: " + fileId);
                    }

                });

                reco.speechEndDetected.addEventListener( (o, recognitionEventArgs) -> {
                    statusLock.lock();
                    try{
                        transcribedFileStatus.put(fileId,TRANSCRIBED);
                    }
                    finally {
                        statusLock.unlock();
                    }
                    synchronized (blockingLock){
                        isBlocked = false;
                        blockingLock.notifyAll();
                    }
                });

                reco.speechStartDetected.addEventListener( (o, recognitionEventArgs) -> {
                    statusLock.lock();
                    try{
                        transcribedFileStatus.put(fileId,TRANSCRIBING);
                    }
                    finally {
                        statusLock.unlock();
                    }
                });

                 reco.startContinuousRecognitionAsync();
                 isBlocked =  true;
                 synchronized (blockingLock){
                     while(isBlocked){
                         blockingLock.wait();
                     }
                 }
            }
            catch (Exception e){
                System.err.println(e.getMessage());
                return null;
            }


        }
        catch (Exception e){
            System.out.println("Unexpected Exception: " + e.getMessage());
            assert(false);
            System.exit(1);
        }
        return response;
    }
}
