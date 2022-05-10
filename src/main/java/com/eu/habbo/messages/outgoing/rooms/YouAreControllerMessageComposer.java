package com.eu.habbo.messages.outgoing.rooms;

import com.eu.habbo.habbohotel.rooms.RoomRightLevels;
import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;

public class YouAreControllerMessageComposer extends MessageComposer {
    private final RoomRightLevels type;
    private final Integer roomId;
    public YouAreControllerMessageComposer(Integer roomId, RoomRightLevels type) {
        this.type = type;
        this.roomId = roomId;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.youAreControllerMessageComposer);
        this.response.appendInt(this.roomId);
        this.response.appendInt(this.type.level);
        return this.response;
    }
}
