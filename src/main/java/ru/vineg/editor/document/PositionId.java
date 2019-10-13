package ru.vineg.editor.document;

import java.util.Objects;

public class PositionId {
    private int sessionId;
    private int characterId;

    public PositionId(int sessionId, int characterId) {
        this.sessionId = sessionId;
        this.characterId = characterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PositionId that = (PositionId) o;
        return sessionId == that.sessionId &&
                characterId == that.characterId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, characterId);
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }
}
