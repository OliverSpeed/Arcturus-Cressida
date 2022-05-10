package com.eu.habbo.messages.incoming.rooms;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.rooms.GetGuestRoomResultComposer;

public class GetGuestRoomEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        Room room = Emulator.getGameEnvironment().getRoomManager().loadRoom(this.packet.readInt());

        int roomForward = this.packet.readInt();
        int enterRoom = this.packet.readInt();
        if (room != null) {

            if (enterRoom == 1 && roomForward == 0) {
                this.client.sendResponse(new GetGuestRoomResultComposer(room, this.client.getHabbo(), true, false));
            } else {
                 this.client.sendResponse(new GetGuestRoomResultComposer(room, this.client.getHabbo(), false, false));

            }
           }
    }
}
