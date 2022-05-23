package com.eu.habbo.messages.outgoing.rooms.items.jukebox;

import com.eu.habbo.habbohotel.items.SoundTrack;
import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;

public class OfficialSongIdMessageComposer extends MessageComposer {
    private final SoundTrack track;

    public OfficialSongIdMessageComposer(SoundTrack track) {
        this.track = track;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.officialSongIdMessageComposer);
        this.response.appendInt(this.track.getId()); // _songId
        this.response.appendString(this.track.getCode()); // _officialSongId
        return this.response;
    }
}
