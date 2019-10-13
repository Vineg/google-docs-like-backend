package ru.vineg.editor.entity;

import ru.vineg.editor.document.SingleChange;

import java.util.ArrayList;
import java.util.List;

public class DocumentEntity {
    private String id;
    private List<SingleChange> changeHistory;

    public DocumentEntity() {
        changeHistory = new ArrayList<>();
    }

    public DocumentEntity(String content) {
        this();
        changeHistory.add(new Insertion(null, content, 0, 1));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SingleChange> getChangeHistory() {
        return changeHistory;
    }

    public List<SingleChange> getChangeHistory(int fromRevision) {
        return changeHistory.subList(fromRevision, changeHistory.size());
    }

    public void setChangeHistory(ArrayList<SingleChange> changeHistory) {
        this.changeHistory = changeHistory;
    }
}
