/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.matrixlab.pisces.metastore;

/**
 *
 * @author alexmy
 */
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;

import org.xmlpull.v1.XmlPullParserException;

import io.minio.MinioClient;
import io.minio.errors.MinioException;
import org.matrixlab.pisces.metastore.core.Consts;
import org.matrixlab.pisces.metastore.core.Utils;

public class FileUploader {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeyException, XmlPullParserException {
        
        String bucketName = "picture-1";
        String objectName = "09-01-2016.zip";
        String objectSource = "/home/alexmy/Pictures/09-01-2016.zip";
        
        try {
            String endPoint = Utils.getProperties().getProperty(Consts.CLIENT_END_POINT);
            String accessKeyId = Utils.getProperties().getProperty(Consts.CLIENT_ACCESS_KEY_ID);
            String accessSecretKey = Utils.getProperties().getProperty(Consts.CLIENT_SECRET_ACCESS_KEY);
            
            // Create a minioClient with the Minio Server name, Port, Access key and Secret key.
            MinioClient minioClient = new MinioClient(endPoint, accessKeyId, accessSecretKey);

            // Check if the bucket already exists.
            boolean isExist = minioClient.bucketExists(bucketName);
            if (isExist) {
                System.out.println("Bucket already exists.");
            } else {
                // Make a new bucket called asiatrip to hold a zip file of photos.
                minioClient.makeBucket(bucketName);
            }

            // Upload the zip file to the bucket with putObject
            minioClient.putObject(bucketName, objectName, objectSource);
            System.out.println(objectSource + " is successfully uploaded as " 
                    + objectName + " to " + bucketName + " bucket.");
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        }
    }
}
