package ru.vineg.editor.document;

import ru.vineg.editor.document.client.EditableTextDocument;

public interface Change {

    void applyTo(EditableTextDocument document);

}
