package com.eu.habbo.messages.incoming.users;

import com.eu.habbo.Emulator;
import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.users.HotLooksMessageComposer;

public class GetHotLooksMessageEvent extends MessageHandler {
    @Override
    public void handle() {
        this.client.sendResponse(new HotLooksMessageComposer(Emulator.getGameEnvironment().getHabboManager().getHotlooks()));
    }
}