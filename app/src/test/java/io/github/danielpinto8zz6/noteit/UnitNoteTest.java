package io.github.danielpinto8zz6.noteit;

import android.widget.TextView;
import org.junit.Before;
import io.github.danielpinto8zz6.noteit.model.Note;
import io.github.danielpinto8zz6.noteit.CreateNoteActivity;
import io.github.danielpinto8zz6.noteit.NoteIt;
import org.junit.Test;

import static io.github.danielpinto8zz6.noteit.model.Note.Status.ACTIVE;
import static org.junit.Assert.*;

public class UnitNoteTest {

    Note note1, note2, note3, note4 = null;
    //CreateNoteActivity.addNoteTest("Note-1", "");

    @Before
    public void setUpNote() {
        note1 = new Note("Note-1", "Hello world!");
        note2 = new Note("Empty body","");
        note3 = new Note("", "Empty title");
        note4 = new Note();
    }

    @Test
    public void NoteStatus_isCorrect() {
        assertEquals(ACTIVE, note1.getStatus());
    }

    @Test
    public void writeTitle_isCorrect() {
        assertEquals("Note-1", note1.getTitle());
    }

    @Test
    public void writeBody_isCorrect() {
        assertEquals("Hello world!", note1.getDescription());
    }

    @Test
    public void NoteStatusIfBodyEmpty_isCorrect() {
        assertEquals(ACTIVE, note2.getStatus());
    }

    @Test
    public void TitleInBodyEmpty_isCorrect() {
        assertEquals("Empty body", note2.getTitle());
    }

    @Test
    public void BodyIsEmpty_isCorrect() {
        assertEquals("", note2.getDescription());
    }

    @Test
    public void NoteStatusIfTitleEmpty_isCorrect() {
        assertEquals(ACTIVE, note3.getStatus());
    }

    @Test
    public void writeTitleEmoty_isCorrect() {
        assertEquals("", note3.getTitle());
    }

    @Test
    public void NoteEmoty_isCorrect() {
        assertEquals("Empty title", note3.getDescription());
    }
}