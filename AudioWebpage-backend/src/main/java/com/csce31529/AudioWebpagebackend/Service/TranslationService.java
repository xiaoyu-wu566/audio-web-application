package com.csce31529.AudioWebpagebackend.Service;

import com.csce31529.AudioWebpagebackend.Config.FileConfig;
import com.csce31529.AudioWebpagebackend.Models.MicrosoftTranslationResponse;
import com.csce31529.AudioWebpagebackend.Models.TranscriptionResponse;
import com.csce31529.AudioWebpagebackend.Models.TranslationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class TranslationService {

    public final String TRANSLATION_API_KEY = "f82123fa612c411a8b8b5377bbbc4b9f";
    public final String TRANSLATION_API_BASE_URL = "https://api.cognitive.microsofttranslator.com/translate?api-version=3.0";


    public TranslationService() {
        File translations = new File(FileConfig.UPLOAD_DIR + "translations/");
        if (!translations.exists()) translations.mkdir();

    }

    public ArrayList<MicrosoftTranslationResponse> translateText(String destLang, String text) {
        String endpoint = TRANSLATION_API_BASE_URL + "&To=" + destLang;
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        String textJsonArr = "[{\"text\" : \"" + text + "\"}]";// Wrap it in json array syntax
        System.out.println(textJsonArr);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Ocp-Apim-Subscription-Key", TRANSLATION_API_KEY);
        HttpEntity<String> request = new HttpEntity<String>(textJsonArr, headers);
        String responseStr = restTemplate.postForObject(endpoint, request, String.class);
        System.out.println(responseStr);
        try {

            TypeFactory typeFactory = objectMapper.getTypeFactory();
            return objectMapper.readValue(responseStr, typeFactory.constructCollectionType(List.class, MicrosoftTranslationResponse.class));

        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private String getNextJsonStr(BufferedReader reader, int startLine) {

        try {
            for (int i = 0; i < startLine; i++) { //Skips until right line
                reader.readLine();
            }

            int total_chars = 0;
            String jsonStr = "[";
            String line = reader.readLine();
            total_chars += line.length();
            jsonStr += "{\"text\" : \"" + line + "\"}";
            while ((line = reader.readLine()) != null) {
                if (total_chars > 5000) {
                    break;
                }
                total_chars += line.length();
                jsonStr += ",{\"text\" : \"" + line + "\"}";

            }
            jsonStr += "]";
            return jsonStr;
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            System.err.println("Could not open file");
        }
        return null;
    }

    public String translateFile(String fileId, String destLang) {
        String textFilePath = FileConfig.UPLOAD_DIR + "text/" + fileId;
        if (exists(destLang, fileId)) return fetch(destLang, fileId).getText();
        try {
            File file = new File(textFilePath);
            BufferedReader reader = new BufferedReader(new FileReader(file));

            ArrayList<String> jsonStrings = new ArrayList<>();
            String jsonStr = getNextJsonStr(reader, 0);
            String endpoint = TRANSLATION_API_BASE_URL + "&To=" + destLang;
            ObjectMapper objectMapper = new ObjectMapper();
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Ocp-Apim-Subscription-Key", TRANSLATION_API_KEY);
            HttpEntity<String> request = new HttpEntity<String>(jsonStr, headers);
            String responseStr = restTemplate.postForObject(endpoint, request, String.class);

            try {

                TypeFactory typeFactory = objectMapper.getTypeFactory();
                List<MicrosoftTranslationResponse> rawTranslatedList = objectMapper.readValue(responseStr, typeFactory.constructCollectionType(List.class, MicrosoftTranslationResponse.class));
                String ret = new String();
                for (MicrosoftTranslationResponse response : rawTranslatedList) {
                    ret += response.getTranslations().get(0).getText();
                    ret += "\n";
                }
                store(destLang, fileId, ret);
                return ret;

            } catch (JsonProcessingException e) {
                System.out.println(e.getMessage());
                return null;
            }

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            return "File not found";
        }

    }


    void store(String code, String fileId, String text) {
        File f = new File(FileConfig.UPLOAD_DIR + "translations/" + code + "/" + fileId);
        File dir = new File(FileConfig.UPLOAD_DIR + "translations/" + code);
        if(!dir.exists()) dir.mkdir();
        try {
            BufferedWriter writer = Files.newBufferedWriter(f.toPath(), StandardOpenOption.CREATE);
            writer.write(text);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    TranslationResponse fetch(String code, String fileId) {
        File f = new File(FileConfig.UPLOAD_DIR + "translations/" + code + "/" + fileId);
        StringBuilder contentBuilder = new StringBuilder();
        TranslationResponse response;
        try {
            Scanner scanner = new Scanner(f);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                contentBuilder.append(line);
            }
            response = new TranslationResponse(contentBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

    boolean exists(String code, String fileId) {
        return new File(FileConfig.UPLOAD_DIR + "translations/" + code + "/" + fileId).exists();
    }


}
