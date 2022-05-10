package com.eu.habbo.messages.outgoing.gamecenter;

import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;

public class Game2GameDirectoryStatusMessageComposer extends MessageComposer {
    private final int status;
    private final int blockLength;
    private final int gamePlayed;
    private final int freeGamesLeft;

    public Game2GameDirectoryStatusMessageComposer(int status, int blockLength, int gamePlayed, int freeGamesLeft) {
        this.status = status;
        this.blockLength = blockLength;
        this.gamePlayed = gamePlayed;
        this.freeGamesLeft = freeGamesLeft;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.game2GameDirectoryStatusMessageComposer);
        this.response.appendInt(this.status);
        this.response.appendInt(this.blockLength);
        this.response.appendInt(this.gamePlayed);
        this.response.appendInt(this.freeGamesLeft);
        return this.response;
    }
}