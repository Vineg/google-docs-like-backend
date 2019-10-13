package ru.vineg.editor.document;

import java.util.Iterator;
import java.util.function.Predicate;

public class RemovableCharacterNode implements Iterable<RemovableCharacterNode> {
    private char aChar;
    private PositionId positionId;
    private boolean removed = false;
    private RemovableCharacterNode nextCharacter;

    public RemovableCharacterNode(char aChar) {
        this.aChar = aChar;
    }

    public char getCharacter() {
        return aChar;
    }

    public boolean isRemoved() {
        return removed;
    }

    public RemovableCharacterNode setRemoved(boolean removed) {
        this.removed = removed;
        return this;
    }

    public RemovableCharacterNode getNextCharacter() {
        return nextCharacter;
    }

    public RemovableCharacterNode setNextCharacter(RemovableCharacterNode nextCharacter) {
        this.nextCharacter = nextCharacter;
        return this;
    }

    public RemovableCharacterNode last() {
        RemovableCharacterNode current = this;
        while(current.getNextCharacter() != null) {
            current = current.getNextCharacter();
        }
        return current;
    }

    public static RemovableCharacterNode fromString(String str) {
        RemovableCharacterNode current = null;
        for (int i = str.length() - 1; i >= 0; i--) {
            current = new RemovableCharacterNode(str.charAt(i)).setNextCharacter(current);
        }
        return current;
    }

    @Override
    public Iterator<RemovableCharacterNode> iterator() {
        return new RemovableCharacterNodeIterator();
    }

    public PositionId getPositionId() {
        return positionId;
    }

    public void setPositionId(PositionId positionId) {
        this.positionId = positionId;
    }

    public Iterable<RemovableCharacterNode> active() {
        return () -> new RemovableCharacterNodeIterator(character -> !character.isRemoved());
    }

    private class RemovableCharacterNodeIterator implements Iterator<RemovableCharacterNode> {

        private final Predicate<RemovableCharacterNode> filter;
        private RemovableCharacterNode current = RemovableCharacterNode.this;

        public RemovableCharacterNodeIterator() {
            this((character) -> true);
        }

        public RemovableCharacterNodeIterator(Predicate<RemovableCharacterNode> filter) {
            this.filter = filter;
            skipToValid();
        }

        private void skipToValid() {
            while(current != null && !filter.test(current)) {
                current = current.nextCharacter;
            }
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public RemovableCharacterNode next() {
            RemovableCharacterNode result = current;
            current = current.nextCharacter;
            skipToValid();
            return result;
        }
    }
}