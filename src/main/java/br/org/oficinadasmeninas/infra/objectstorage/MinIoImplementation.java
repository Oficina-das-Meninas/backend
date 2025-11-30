package br.org.oficinadasmeninas.infra.objectstorage;

import br.org.oficinadasmeninas.domain.objectstorage.IObjectStorage;
import br.org.oficinadasmeninas.infra.shared.exception.ObjectStorageException;

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

    public String uploadFile(MultipartFile file, Boolean isPublic) {

        var title = generateTitle(file);

        upload(file, title, isPublic);
        return title;
    }

    @Override
    public String uploadTransparencyFile(MultipartFile file, boolean isImage) {

        var title = generateTitle(file);
        var objectKey = (isImage ? IMAGE_PATH : DOCUMENT_PATH) + title;

        upload(file, objectKey, true);
        return "/pub/" + objectKey;
    }

    private void upload(MultipartFile file, String objectKey, boolean isPublic){

        try {
            if (isSmallFile(file)) {
                simpleUpload(file, objectKey, isPublic);
            } else {
                multipartUpload(file, objectKey, isPublic);
            }

        } catch (IOException e) {
            throw new ObjectStorageException(e);
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileUrl)
                    .build());

        } catch (S3Exception e) {
            throw new ObjectStorageException(e);
        }
    }

    private String sanitizeFileName(String fileName) {
        String normalized = java.text.Normalizer.normalize(fileName, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return normalized.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
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

    private String generateTitle(MultipartFile file) {

        var sanitizedFileName = sanitizeFileName(file.getOriginalFilename());

        int dotIndex = sanitizedFileName.lastIndexOf(".");
        if (dotIndex == -1) {
            return sanitizedFileName + "_" + System.currentTimeMillis();
        }

        var name = sanitizedFileName.substring(0, dotIndex);
        var extension = sanitizedFileName.substring(dotIndex);

        return name + "_" + System.currentTimeMillis() + extension;
    }
}