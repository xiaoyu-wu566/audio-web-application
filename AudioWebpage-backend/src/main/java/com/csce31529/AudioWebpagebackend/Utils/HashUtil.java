package com.csce31529.AudioWebpagebackend.Utils;

import org.apache.logging.log4j.message.Message;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

public class HashUtil {

    public static String getCheckSum(byte[] fileBytes){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            DigestInputStream dis = new DigestInputStream( new ByteArrayInputStream(fileBytes), md);
            while (dis.read() != -1){
                md = dis.getMessageDigest();
            }

            StringBuilder result = new StringBuilder();
            for(byte b : md.digest()){
                result.append( String.format("%02x",b) );
            }
            return result.toString();
        }
        catch (Exception ex){
            return null;
        }
    }
}
