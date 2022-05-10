package com.eu.habbo.messages.outgoing.rooms;

import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;

public class OpenConnectionMessageComposer extends MessageComposer {
    private final Integer roomId;
    public OpenConnectionMessageComposer(int roomId) {
        this.roomId = roomId;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.openConnectionMessageComposer);
        this.response.appendInt(roomId);
        return this.response;
    }
}
