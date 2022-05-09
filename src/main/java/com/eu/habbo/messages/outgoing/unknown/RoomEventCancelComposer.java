package com.eu.habbo.messages.outgoing.unknown;

import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;

public class RoomEventCancelComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(0); //todo idk
        //Empty Body
        return this.response;
    }
}