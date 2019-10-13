package ru.vineg.editor.document;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.vineg.editor.entity.Deletion;
import ru.vineg.editor.entity.Insertion;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Insertion.class, name = "Insertion"),
        @JsonSubTypes.Type(value = Deletion.class, name = "Deletion")
})
public interface SingleChange extends Change {
}
