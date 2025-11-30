package br.org.oficinadasmeninas.infra.objectstorage.rollback;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MinIoRollbackContext {

    private final ThreadLocal<List<String>> uploadedFiles = ThreadLocal.withInitial(ArrayList::new);

    public void register(String... files){
        uploadedFiles.get().addAll(List.of(files));
    }

    public List<String> getAndClear(){

        var current = new ArrayList<String>(uploadedFiles.get());
        uploadedFiles.remove();
        return current;
    }
}