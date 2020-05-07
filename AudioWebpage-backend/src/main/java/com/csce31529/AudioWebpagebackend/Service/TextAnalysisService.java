package com.csce31529.AudioWebpagebackend.Service;

import com.csce31529.AudioWebpagebackend.Config.FileConfig;
import com.csce31529.AudioWebpagebackend.Models.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Scanner;

@Service
public class TextAnalysisService {
    private static final String TEXT_API_KEY = "49117c78cf5f45ce9257afdbab103153";
    private static final String TEXT_API_ENDPOINT = "https://csce315-29-text.cognitiveservices.azure.com/text/analytics/v2.1/sentiment";

    public TextAnalysisService() {
        File f = new File(FileConfig.UPLOAD_DIR + "sentiment/");
        if(!f.exists()) f.mkdir();
    }

    public TextAnalysisResponse getDocumentsScores(TextAnalysisDocuments documents){
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type","application/json");
            headers.add("Accept","application/json");
            headers.add("Ocp-Apim-Subscription-Key",TEXT_API_KEY);
            HttpEntity<String> request = new HttpEntity<String>(objectMapper.writeValueAsString(documents), headers);
            return restTemplate.postForObject(TEXT_API_ENDPOINT,request, TextAnalysisResponse.class);
        }
        catch(JsonProcessingException ex){
            System.err.println("Could not parse to json");
            System.err.println(documents.toString());

        }
        return null;
    }

    public TextAnalysisResponse getFileScores(String fileId){
        if(exists(fileId)) return fetch(fileId);
        String textPath = FileConfig.UPLOAD_DIR + "text/" + fileId;
        File file = new File(textPath);
        TextAnalysisDocuments documents=new TextAnalysisDocuments();
        try {

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String language = "en"; //ONLY WORKS FOR ENGLISH
            String line = "";
	    ObjectMapper objectMapper = new ObjectMapper();
            RestTemplate restTemplate = new RestTemplate();
            int documentId = 0;
            while ((line = reader.readLine()) != null) {
                TextAnalysisDocument doc = new TextAnalysisDocument();
                doc.setDocumentID( String.valueOf(documentId++));
                doc.setLanguage(language);
                doc.setText(line);
                documents.getDocuments().add(doc);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type","application/json");
            headers.add("Accept","application/json");
            headers.add("Ocp-Apim-Subscription-Key",TEXT_API_KEY);
            HttpEntity<String> request = new HttpEntity<String>(objectMapper.writeValueAsString(documents), headers);
            TextAnalysisResponse ret = restTemplate.postForObject(TEXT_API_ENDPOINT,request, TextAnalysisResponse.class);
            store(fileId, ret);
            return ret;
//            ArrayList<Double> ret = new ArrayList<>();
//            for(TextAnalysisResponseDocument doc : response.getDocuments()){
//                ret.add(doc.getScore());
//            }
//            return ret;
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
            return null;
        }


    }

    public ArrayList<ArrayList<String>> getKeywordsFromFile(String fileId){
        String textPath = FileConfig.UPLOAD_DIR + "text/" + fileId;
        File file = new File(textPath);

        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String language = "en"; //ONLY WORKS FOR ENGLISH
            String line = reader.readLine();
            ObjectMapper objectMapper = new ObjectMapper();
            RestTemplate restTemplate = new RestTemplate();
            int documentId = 0;
            while ((line = reader.readLine()) != null) {

            }

        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
            return null;
        }
            return null;
    }

    void store(String fileId, TextAnalysisResponse resp) {
        File f = new File(FileConfig.UPLOAD_DIR + "sentiment/" + fileId);
        try {
            BufferedWriter writer = Files.newBufferedWriter(f.toPath(), StandardOpenOption.CREATE);
            for (TextAnalysisResponseDocument document : resp.getDocuments()) {
                writer.write(document.getScore().toString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    TextAnalysisResponse fetch(String fileId) {
        File f = new File(FileConfig.UPLOAD_DIR + "sentiment/" + fileId);
        TextAnalysisResponse response = new TextAnalysisResponse();
        response.setDocuments(new ArrayList<>());
        try {
            Scanner scanner = new Scanner(f);
            Integer count = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                response.getDocuments().add(new TextAnalysisResponseDocument((count++).toString(), Double.parseDouble(line)));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

    boolean exists(String fileId) {
        return new File(FileConfig.UPLOAD_DIR + "sentiment/"  + fileId).exists();
    }

}
