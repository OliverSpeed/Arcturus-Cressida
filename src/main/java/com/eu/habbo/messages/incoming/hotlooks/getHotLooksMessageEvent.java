package com.eu.habbo.messages.incoming.hotlooks;

import com.eu.habbo.Emulator;
import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.hotlooks.hotLooksMessageComposer;
import com.eu.habbo.messages.outgoing.users.HotLooksMessageComposer;

public class getHotLooksMessageEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.sendResponse(new HotLooksMessageComposer(Emulator.getGameEnvironment().getHabboManager().getHotlooks()));
    }
}
