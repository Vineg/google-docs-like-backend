package ru.vineg.editor.dao;

import org.springframework.stereotype.Repository;

@Repository
public class LocalSessionDao implements SessionDao{

    private int sessionSequence = 1;

    @Override
    public int getSessionSequenceAndIncrease() {
        return sessionSequence++;
    }
}
