package br.org.oficinadasmeninas.infra.ObjectStorage;

import br.org.oficinadasmeninas.domain.objectStorage.IObjectStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;

@Service
public class MinIoImplementation implements IObjectStorage {

    private static final String DOCUMENT_PATH = "transparency/documents/";
    private static final String IMAGE_PATH = "transparency/images/";

    private final S3Client s3Client;
    private final String bucketName;

    public MinIoImplementation(S3Client s3Client, @Value("${storage.s3.bucket}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    @Override
    public void upload(MultipartFile file, Boolean isPublic) throws IOException {
        String originalName = file.getOriginalFilename();
        if (originalName != null) {
            originalName = sanitizeFileName(originalName);
        }

        upload(file, originalName, isPublic);
    }

    private String sanitizeFileName(String fileName) {
        String normalized = java.text.Normalizer.normalize(fileName, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return normalized.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
    }

    @Override
    public void upload(MultipartFile file, String fileName,  Boolean isPublic) {
        try {
            if (isSmallFile(file)) {
                simpleUpload(file, fileName, isPublic);
            } else {
                multipartUpload(file, fileName, isPublic);
            }
        } catch (IOException e) {
            throw new RuntimeException("NAO FOI POSSIVEL ARMAZENAR ARQUIVO", e);
        }
    }

    @Override
    public String uploadTransparencyFile(MultipartFile file, boolean isImage) throws IOException {
        var title = file.getOriginalFilename();

        if (title != null) {
            title = generateTitle(sanitizeFileName(title));
        }

        try {
            var objectKey = (isImage ? IMAGE_PATH : DOCUMENT_PATH ) + title;

            if (isSmallFile(file)) {
                simpleUpload(file, objectKey, true);
            } else {
                multipartUpload(file, objectKey, true);
            }

            return String.format("http://%s/%s", bucketName, objectKey);

        } catch (IOException e) {
            throw new RuntimeException("NAO FOI POSSIVEL ARMAZENAR ARQUIVO", e);
        }
    }

    private boolean isSmallFile(MultipartFile file) {
        return file.getSize() < 5 * 1024 * 1024; // < 5MB
    }

    private void simpleUpload(MultipartFile file, String fileName, Boolean isPublic) throws IOException {

        String objectKey = isPublic ? "pub/" + fileName : fileName;
        String contentType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .contentType(contentType)
                        .key(objectKey)
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );
    }

    private void multipartUpload(MultipartFile file, String fileName, Boolean isPublic) throws IOException {
        String objectKey = isPublic ? "pub/" + fileName : fileName;
        String contentType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";

        CreateMultipartUploadResponse createResponse = s3Client.createMultipartUpload(
                CreateMultipartUploadRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .contentType(contentType)
                        .build()
        );

        String uploadId = createResponse.uploadId();

        UploadPartResponse partResponse = s3Client.uploadPart(
                UploadPartRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .uploadId(uploadId)
                        .partNumber(1)
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );

        CompletedPart part = CompletedPart.builder()
                .partNumber(1)
                .eTag(partResponse.eTag())
                .build();

        CompletedMultipartUpload multipartUpload = CompletedMultipartUpload.builder()
                .parts(part)
                .build();

        s3Client.completeMultipartUpload(
                CompleteMultipartUploadRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .uploadId(uploadId)
                        .multipartUpload(multipartUpload)
                        .build()
        );
    }

    public static String generateTitle(String filename) {

        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex == -1) {
            return filename + "_" + System.currentTimeMillis();
        }

        String name = filename.substring(0, dotIndex);
        String extension = filename.substring(dotIndex);

        return name + "_" + System.currentTimeMillis() + extension;
    }
}