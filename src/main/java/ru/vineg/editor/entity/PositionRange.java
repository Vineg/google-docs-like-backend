package ru.vineg.editor.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import ru.vineg.editor.document.PositionId;

import java.util.Iterator;

@JsonAutoDetect
public class PositionRange implements Iterable<PositionId> {
    private int sessionId;
    private int startPosition;
    private int endPosition;

    public PositionRange(PositionId positionId) {
        this.sessionId = positionId.getSessionId();
        this.startPosition = positionId.getCharacterId();
        this.endPosition = startPosition + 1;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }

    @Override
    public Iterator<PositionId> iterator() {
        return new PositionIterator();
    }


    public static boolean follows(PositionId previous, PositionId next) {
        if (previous == null) {
            return false;
        }
        return previous.getSessionId() == next.getSessionId() && next.getCharacterId() - previous.getCharacterId() == 1;
    }

    private class PositionIterator implements Iterator<PositionId> {

        private PositionId position = new PositionId(PositionRange.this.sessionId, PositionRange.this.startPosition - 1);


        @Override
        public boolean hasNext() {
            return position.getCharacterId() + 1 < PositionRange.this.endPosition;
        }

        @Override
        public PositionId next() {
            position.setCharacterId(position.getCharacterId() + 1);
            return position;
        }
    }
}
