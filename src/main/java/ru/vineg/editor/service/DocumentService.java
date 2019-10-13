package ru.vineg.editor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vineg.editor.dao.DocumentDao;
import ru.vineg.editor.document.SingleChange;
import ru.vineg.editor.entity.DocumentEntity;

import java.util.List;

@Service
public class DocumentService {

    private final DocumentDao documentDao;

    public DocumentService(DocumentDao documentDao) {
        this.documentDao = documentDao;
    }

    public DocumentEntity getDocument(String documentId) {
        return documentDao.get(documentId);
    }

    public String save(DocumentEntity document) {
        return documentDao.save(document);
    }

    public void addChange(DocumentEntity document, SingleChange change) {
        List<SingleChange> changes = document.getChangeHistory();
        changes.add(change);
        documentDao.update(change);
    }
}
