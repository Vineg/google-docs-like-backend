package ru.vineg.editor.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import ru.vineg.editor.document.client.EditableTextDocument;
import ru.vineg.editor.document.PositionId;
import ru.vineg.editor.document.RemovableCharacterNode;
import ru.vineg.editor.document.SingleChange;

import java.util.HashMap;
import java.util.List;

@JsonAutoDetect
public class Deletion implements SingleChange {
    private List<PositionRange> positionRanges;

    public Deletion() {}

    public Deletion(List<PositionRange> positionRanges) {
        this.positionRanges = positionRanges;
    }

    @Override
    public void applyTo(EditableTextDocument document) {
        HashMap<PositionId, RemovableCharacterNode> charactersMap = document.getCharactersMap();
        positionRanges.forEach(positionRange -> {
            positionRange.forEach(position -> charactersMap.get(position).setRemoved(true));
        });
    }

    public List<PositionRange> getPositionRanges() {
        return positionRanges;
    }

    public void setPositionRanges(List<PositionRange> positionRanges) {
        this.positionRanges = positionRanges;
    }
}
