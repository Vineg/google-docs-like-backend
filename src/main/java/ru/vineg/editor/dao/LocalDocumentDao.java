package ru.vineg.editor.dao;

import org.springframework.stereotype.Repository;
import ru.vineg.editor.document.SingleChange;
import ru.vineg.editor.entity.DocumentEntity;

import java.util.HashMap;

@Repository
public class LocalDocumentDao implements DocumentDao {
    private long idSequence = 1;

    private HashMap<String, DocumentEntity> storage = new HashMap<>();

    @Override
    public DocumentEntity get(String id) {
        return storage.get(id);
    }

    @Override
    public String save(DocumentEntity document) {
        String newId = String.valueOf(idSequence);
        storage.put(newId, document);
        document.setId(newId);
        idSequence++;
        return newId;
    }

    @Override
    public void update(SingleChange change) {
    }
}
