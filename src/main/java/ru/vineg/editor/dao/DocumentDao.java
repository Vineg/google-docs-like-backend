package ru.vineg.editor.dao;

import ru.vineg.editor.document.SingleChange;
import ru.vineg.editor.entity.DocumentEntity;

public interface DocumentDao {

    /**
     * @param id Document id
     */
    DocumentEntity get(String id);

    /**
     * @param document Document to save
     * @return generated ID
     */
    String save(DocumentEntity document);

    void update(SingleChange change);
}
