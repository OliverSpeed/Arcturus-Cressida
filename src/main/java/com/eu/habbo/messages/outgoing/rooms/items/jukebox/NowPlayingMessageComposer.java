package com.eu.habbo.messages.outgoing.rooms.items.jukebox;

import com.eu.habbo.habbohotel.items.SoundTrack;
import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;

public class NowPlayingMessageComposer extends MessageComposer {
    private final SoundTrack track;
    private final int playListId;
    private final int msPlayed;

    public NowPlayingMessageComposer(SoundTrack track, int playListId, int msPlayed) {
        this.track = track;
        this.playListId = playListId;
        this.msPlayed = msPlayed;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.nowPlayingMessageComposer);

        if (this.track != null) {
            this.response.appendInt(this.track.getId()); // _currentSongId
            this.response.appendInt(this.playListId); // _currentPosition
            this.response.appendInt(this.track.getId()); // _nextSongId
            this.response.appendInt(this.track.getLength()); // _nextPosition
            this.response.appendInt(this.msPlayed); // _syncCount
        } else {
            this.response.appendInt(-1);
            this.response.appendInt(-1);
            this.response.appendInt(-1);
            this.response.appendInt(-1);
            this.response.appendInt(-1);
        }
        return this.response;
    }
}
