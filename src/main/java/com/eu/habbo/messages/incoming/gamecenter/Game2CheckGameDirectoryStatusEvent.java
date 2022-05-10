package com.eu.habbo.messages.incoming.gamecenter;

import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.gamecenter.Game2GameDirectoryStatusMessageComposer;

public class Game2CheckGameDirectoryStatusEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.sendResponse(new Game2GameDirectoryStatusMessageComposer(0, 0, 2, 3));
    }
}
