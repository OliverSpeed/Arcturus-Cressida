package com.eu.habbo.messages.outgoing.rooms;

import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;

public class FlatAccessibleMessageComposer extends MessageComposer {
    private final String username;
    private final Integer roomId;
    public FlatAccessibleMessageComposer(int roomId, String username) {
        this.username = username;
        this.roomId = roomId;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.flatAccessibleMessageComposer);
        this.response.appendInt(this.roomId);
        this.response.appendString(this.username);
        return this.response;
    }
}