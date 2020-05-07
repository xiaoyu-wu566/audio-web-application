package com.csce31529.AudioWebpagebackend.Service;

import com.csce31529.AudioWebpagebackend.Config.FileConfig;
import com.csce31529.AudioWebpagebackend.Models.TranscriptionResponse;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.stream.Stream;

import static java.security.MessageDigest.getInstance;

@Service
public class FileStorageService {

    public FileStorageService() {
        File audio = new File("/aafiles/audio");
        File text = new File("/aafiles/text");
        if (!audio.exists()) audio.mkdirs();
        if (!text.exists()) text.mkdirs();
    }

    String storeAudio(byte[] data) {
        try {
            MessageDigest hasher = getInstance("MD5");
            hasher.update(data);
            String hash = HexUtils.toHexString(hasher.digest());
            File audioFile = new File(FileConfig.UPLOAD_DIR + "audio/" + hash);
            if(audioFile.exists()) return hash;
            FileOutputStream outputStream = new FileOutputStream(audioFile);
            outputStream.write(data);
            return hash;
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    boolean transcriptionExists(String hash) {
        File text = new File(FileConfig.UPLOAD_DIR + "text/" + hash);
        return text.exists();
    }

    TranscriptionResponse fetchTranscription(String hash) {
        File text = new File(FileConfig.UPLOAD_DIR + "text/" + hash);
        StringBuilder contentBuilder = new StringBuilder();
        TranscriptionResponse response = new TranscriptionResponse();
        try {
            Scanner scanner = new Scanner(text);
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                System.out.println( line);
                response.getDocuments().add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

    void storeTranscription(String hash, String text) {
        File textPath = new File(FileConfig.UPLOAD_DIR + "text/" + hash);
        try {
            BufferedWriter writer = Files.newBufferedWriter(textPath.toPath(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            writer.write(text);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
