package ru.vineg.editor.document.client;

import ru.vineg.editor.document.PositionId;
import ru.vineg.editor.document.RemovableCharacterNode;
import ru.vineg.editor.document.SingleChange;
import ru.vineg.editor.entity.Insertion;

import java.util.*;

public class EditableTextDocument {
    private final List<SingleChange> changesHistory;
    private int revision = 0;

    private RemovableCharacterNode firstCharacter;
    private final HashMap<PositionId, RemovableCharacterNode> charactersMap;

    public EditableTextDocument() {
        this(Collections.emptyList());
    }

    public EditableTextDocument(String content) {
        this(new ArrayList<>(Collections.singletonList(new Insertion(null, content, 0, 0))));
    }

    public EditableTextDocument(List<SingleChange> changesHistory) {
        this.changesHistory = changesHistory;
        charactersMap = new HashMap<>();
        checkoutLatestRevision();
    }

    public String getTextContent() {
        StringBuilder textContent = new StringBuilder();
        for(RemovableCharacterNode character : firstCharacter.active()) {
            textContent.append(character.getCharacter());
        }
        return textContent.toString();
    }

    public EditableTextDocument checkoutLatestRevision() {
        return checkoutRevision(changesHistory.size());
    }

    public EditableTextDocument checkoutRevision(int newRevision) {
        if (this.revision > newRevision) {
            throw new IllegalArgumentException("only incremental revisions allowed");
        }
        if(newRevision > changesHistory.size()) {
            throw new IllegalArgumentException("unknown revision");
        }
        for (int i = this.revision; i < newRevision; i++) {
            changesHistory.get(i).applyTo(this);
            revision = i + 1;
        }
        return this;
    }

    public void insertAfter(PositionId insertAfterId, int sessionId, int insertId, String stringToInsert) {
        RemovableCharacterNode insertList = RemovableCharacterNode.fromString(stringToInsert);
        for (RemovableCharacterNode character : insertList) {
            PositionId positionId = new PositionId(sessionId, insertId++);
            character.setPositionId(positionId);
            charactersMap.put(positionId, character);
        }
        RemovableCharacterNode endChar;
        if (insertAfterId != null) {
            RemovableCharacterNode startChar = charactersMap.get(insertAfterId);
            endChar = startChar.getNextCharacter();
            startChar.setNextCharacter(insertList);
        } else {
            endChar = firstCharacter;
            firstCharacter = insertList;
        }
        insertList.last().setNextCharacter(endChar);
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public List<SingleChange> getChangesHistory() {
        return changesHistory;
    }

    public HashMap<PositionId, RemovableCharacterNode> getCharactersMap() {
        return charactersMap;
    }

    public RemovableCharacterNode getFirstCharacter() {
        return firstCharacter;
    }

    public void setFirstCharacter(RemovableCharacterNode firstCharacter) {
        this.firstCharacter = firstCharacter;
    }

    public void addChange(SingleChange change) {
        changesHistory.add(change);
    }
}
