package com.csce31529.AudioWebpagebackend.Controllers;

import com.csce31529.AudioWebpagebackend.Config.FileConfig;
import com.csce31529.AudioWebpagebackend.Models.FileUploadResponse;
import com.github.kokorin.jaffree.StreamType;
import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.PipeInput;
import com.github.kokorin.jaffree.ffmpeg.PipeOutput;
import com.github.kokorin.jaffree.ffmpeg.UrlOutput;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
public class FileUploadController {

    @CrossOrigin
    @PostMapping("/upload")
    public ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            MessageDigest hasher = MessageDigest.getInstance("MD5");
            hasher.update(file.getBytes());
            String hash = HexUtils.toHexString(hasher.digest());
            if (Paths.get("ffmpeg").toFile().exists()) {
                Path outputPath = Path.of(FileConfig.UPLOAD_DIR + "audio/" + hash);
                FFmpeg.atPath().addInput(PipeInput.pumpFrom(file.getInputStream())).addOutput(UrlOutput.toPath(outputPath).setFormat("wav")).setOverwriteOutput(true).execute();
            } else {
                FileOutputStream outputStream = new FileOutputStream(FileConfig.UPLOAD_DIR + "audio/" + hash);
                outputStream.write(file.getBytes());
            }
            return ResponseEntity.status(HttpStatus.OK).body(new FileUploadResponse(file.getOriginalFilename(), hash));
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed.  ");
        }
    }
}
