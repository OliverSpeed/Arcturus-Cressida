package com.eu.habbo.messages.outgoing.inventory;

import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.habbohotel.users.HabboBadge;
import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;
import gnu.trove.set.hash.THashSet;

public class BadgesComposer extends MessageComposer {
    private final THashSet<HabboBadge> badges;
    private final int fragmentNumber;
    private final int totalFragments;
    public BadgesComposer(int fragmentNumber, int totalFragments, THashSet<HabboBadge> badges) {
        this.fragmentNumber = fragmentNumber;
        this.totalFragments = totalFragments;
        this.badges = badges;
    }

    @Override
    protected ServerMessage composeInternal() {

        this.response.init(Outgoing.badgesComposer);
        this.response.appendInt(this.totalFragments);
        this.response.appendInt(this.fragmentNumber - 1);
        this.response.appendInt(this.badges.size());
        for (HabboBadge badge : this.badges) {
            this.response.appendInt(badge.getId());
            this.response.appendString(badge.getCode());
        }


        return this.response;
    }
}
