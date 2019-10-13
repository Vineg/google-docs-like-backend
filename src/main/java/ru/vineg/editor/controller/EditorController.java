package ru.vineg.editor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import ru.vineg.editor.document.SingleChange;
import ru.vineg.editor.dto.CreateEntityResponseDto;
import ru.vineg.editor.entity.DocumentEntity;
import ru.vineg.editor.service.DocumentService;

import java.util.List;

@RestController
public class EditorController {

    private final DocumentService documentService;

    public EditorController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/document/{documentId}/edits")
    public List<SingleChange> documentHistory(@PathVariable String documentId, @RequestParam int fromRevision) {
        DocumentEntity document = getDocument(documentId);
        return document.getChangeHistory(fromRevision);
    }

    @PostMapping("/document/{documentId}/edits")
    public List<SingleChange> editDocument(@PathVariable String documentId, @RequestBody SingleChange edit, @RequestParam int fromRevision) {
        DocumentEntity document = getDocument(documentId);
        documentService.addChange(document, edit);
        return document.getChangeHistory(fromRevision);
    }

    @PostMapping("/document/create")
    public CreateEntityResponseDto createDocument() {
        return new CreateEntityResponseDto(documentService.save(new DocumentEntity()));
    }

    private DocumentEntity getDocument(@PathVariable String documentId) {
        DocumentEntity document = documentService.getDocument(documentId);
        if (document == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        return document;
    }
}
