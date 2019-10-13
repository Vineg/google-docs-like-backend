package ru.vineg.editor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.vineg.editor.dao.SessionDao;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EditorSession {

    private final int sessionId;

    @Autowired
    public EditorSession(SessionDao sessionDao) {
        this.sessionId = sessionDao.getSessionSequenceAndIncrease();
    }

    public int getSessionId() {
        return sessionId;
    }

    private int characterSequence = 1;
    public int getCharacterSequenceAndAdd(int add) {
        int result = characterSequence;
        characterSequence += add;
        return result;
    }

    public void setCharacterSequence(int value) {
        this.characterSequence = value;
    }
}
