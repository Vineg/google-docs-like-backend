package ru.vineg.editor;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vineg.editor.document.*;
import ru.vineg.editor.document.client.EditableTextDocument;
import ru.vineg.editor.entity.Insertion;
import ru.vineg.editor.entity.Deletion;
import ru.vineg.editor.entity.PositionRange;
import ru.vineg.editor.service.EditorSession;

import java.util.LinkedList;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ConflictResolutionTests {

	@Autowired
	private EditorSession editorSession;


	@Test
	public void InsertionWorks() {
		EditableTextDocument doc = new EditableTextDocument("testContent");
		doc.addChange(createInsertion(doc, 4, "Changed"));
		EditableTextDocument editableTextDocument = doc.checkoutLatestRevision();
		Assert.assertEquals("testChangedContent", editableTextDocument.getTextContent());
	}

	@Test
	public void deletionWorks() {
		EditableTextDocument doc = new EditableTextDocument("testChangedContent");
		doc.addChange(createDeletion(doc, 4, 4 + "Changed".length()));
		doc.checkoutLatestRevision();
		Assert.assertEquals("testContent", doc.getTextContent());
	}

	@Test
	public void InsertionConflictResolves() {
		EditableTextDocument doc = new EditableTextDocument("testContent");
		doc.addChange(createInsertion(doc, 4, "1"));
		doc.addChange(createInsertion(doc, 4, "2"));
		EditableTextDocument editableTextDocument = doc.checkoutLatestRevision();
		Assert.assertTrue(
				"test12Content".equals(editableTextDocument.getTextContent()) ||
						"test21Content".equals(editableTextDocument.getTextContent())
				);
	}

	@Test
	public void distantInsertionConflictResolves() {
		EditableTextDocument doc = new EditableTextDocument("testContent");
		doc.addChange(createInsertion(doc, 4, "1"));
		doc.addChange(createInsertion(doc, 0, "2"));
		doc = doc.checkoutLatestRevision();
		Assert.assertEquals(
				"2test1Content", doc.getTextContent()
		);
	}

	@Test
	public void deletionConflictResolves() {
		EditableTextDocument doc = new EditableTextDocument("testChangedContent");
		doc.addChange(createDeletion(doc, 4, 4 + "ChangedContent".length()));
		doc.addChange(createDeletion(doc, 4, 4 + "Changed".length()));
		Assert.assertEquals("test", doc.checkoutLatestRevision().getTextContent());
	}

	@Test
	public void distantDeletionConflictResolves() {
		EditableTextDocument doc = new EditableTextDocument("testChangedContent");
		doc.addChange(createDeletion(doc, 0, 0 + "test".length()));
		doc.addChange(createDeletion(doc, 11, 11 + "Content".length()));
		EditableTextDocument editableTextDocument = doc.checkoutLatestRevision();
		Assert.assertEquals("Changed", editableTextDocument.getTextContent());
	}

	@Test
	public void multipleDeletionConflictResolves() {
		EditableTextDocument doc = new EditableTextDocument("testChangedContent");
		doc.addChange(createDeletion(doc, 4, 4 + "ChangedContent".length()));
		doc.addChange(createDeletion(doc, 4, 4 + "Changed".length()));
		doc.addChange(createDeletion(doc, 0, 0 + "testChanged".length()));
		EditableTextDocument editableTextDocument = doc.checkoutLatestRevision();
		Assert.assertEquals("", editableTextDocument.getTextContent());
	}

	@Test
	public void mixedNestedConflictResolves() {
		EditableTextDocument doc = new EditableTextDocument("testContent");
		doc.addChange(createInsertion(doc, 4, "sameContent"));
		doc.addChange(createDeletion(doc, 1, 1 + "estConten".length()));
		doc.addChange(createDeletion(doc, 0, 0 + "testContent".length()));
		Assert.assertEquals("sameContent", doc.checkoutLatestRevision().getTextContent());
	}

	@Test
	public void mixedDistantConflictResolves() {
		EditableTextDocument doc = new EditableTextDocument("testContent");
		doc.checkoutLatestRevision();

		doc.addChange(createDeletion(doc, 0, 0 + "test".length()));
		doc.addChange(createInsertion(doc, "testContent".length(), "Insertion"));
		Assert.assertEquals("ContentInsertion", doc.checkoutLatestRevision().getTextContent());

		doc.addChange(createInsertion(doc, "ContentInsertion".length(), "Insertion"));
		doc.addChange(createDeletion(doc, 0, 0 + "Content".length()));
		Assert.assertEquals("InsertionInsertion", doc.checkoutLatestRevision().getTextContent());
	}

	private SingleChange createDeletion(EditableTextDocument document, int startIndex, int endIndex) {
		int i = 0;
		LinkedList<PositionRange> positionRanges = new LinkedList<>();
		PositionId previousPosition = null;
		for(RemovableCharacterNode aChar : document.getFirstCharacter().active()) {
			if (i >= endIndex) {
				break;
			} else if (i >= startIndex) {
				PositionId currentPosition = aChar.getPositionId();
				if (PositionRange.follows(previousPosition, currentPosition)) {
					PositionRange lastRange = positionRanges.getLast();
					lastRange.setEndPosition(lastRange.getEndPosition() + 1);
				} else {
					positionRanges.add(new PositionRange(currentPosition));
				}
				previousPosition = currentPosition;
			}
			i++;
		}
		if (i < endIndex) {
			throw new IllegalArgumentException("document is shorter then endIndex");
		}
		return new Deletion(positionRanges);
	}

	private SingleChange createInsertion(EditableTextDocument document, int startIndex, String data) {
		if (startIndex == 0) {
			return new Insertion(null, data, editorSession);
		}
		int i = 1;
		for(RemovableCharacterNode aChar : document.getFirstCharacter().active()) {
			if (i >= startIndex) {
				return new Insertion(aChar.getPositionId(), data, editorSession);
			}
			i++;
		}
		throw new IllegalArgumentException("startIndex not found in document");
	}
}
