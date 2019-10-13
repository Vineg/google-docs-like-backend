package ru.vineg.editor.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import ru.vineg.editor.document.client.EditableTextDocument;
import ru.vineg.editor.document.PositionId;
import ru.vineg.editor.document.SingleChange;
import ru.vineg.editor.service.EditorSession;

@JsonAutoDetect
public class Insertion implements SingleChange {
    private PositionId positionId;
    private int sessionId;
    private int insertId;
    private String value;

    public Insertion() {}

    public Insertion(PositionId positionId, String value, EditorSession editorSession) {
        this(positionId, value, 0,0);
        registerTo(editorSession);
    }

    public Insertion(PositionId positionId, String value, int sessionId, int insertId) {
        this.positionId = positionId;
        this.value = value;
        this.sessionId = sessionId;
        this.insertId = insertId;
    }

    @Override
    public void applyTo(EditableTextDocument document) {
        document.insertAfter(positionId, sessionId, insertId, value);
    }

    private SingleChange registerTo(EditorSession editorSession) {
        if (insertId == 0) {
            this.insertId = editorSession.getCharacterSequenceAndAdd(value.length());
        } else {
            editorSession.setCharacterSequence(insertId + value.length());
        }
        if (this.sessionId == 0) {
            this.sessionId = editorSession.getSessionId();
        }
        return this;
    }

    public PositionId getPositionId() {
        return positionId;
    }

    public void setPositionId(PositionId positionId) {
        this.positionId = positionId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getInsertId() {
        return insertId;
    }

    public void setInsertId(int insertId) {
        this.insertId = insertId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
