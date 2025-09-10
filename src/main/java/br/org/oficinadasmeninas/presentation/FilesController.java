package br.org.oficinadasmeninas.presentation;

import br.org.oficinadasmeninas.domain.objectStorage.IObjectStorage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FilesController {
    private final IObjectStorage objectStorage;

    public FilesController(IObjectStorage objectStorage) {
        this.objectStorage = objectStorage;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        objectStorage.upload(file, true);
        return ResponseEntity.ok("Arquivo enviado com sucesso!");
    }

    @PostMapping("/upload/{fileName}")
    public ResponseEntity<String> uploadWithName(
            @PathVariable String fileName,
            @RequestParam("file") MultipartFile file) throws IOException {
        objectStorage.upload(file, fileName, true);
        return ResponseEntity.ok("Arquivo '" + fileName + "' enviado com sucesso!");
    }

}
