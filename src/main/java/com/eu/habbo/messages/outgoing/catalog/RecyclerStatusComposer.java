package com.eu.habbo.messages.outgoing.catalog;

import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;

public class RecyclerStatusComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        //tthis.response.init(Outgoing.recyclerStatusComposer);
        this.response.init(0);
        this.response.appendInt(1);
        this.response.appendInt(0);
        return this.response;
    }
}
