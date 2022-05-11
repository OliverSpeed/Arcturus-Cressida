package com.eu.habbo.messages.outgoing.users;

import com.eu.habbo.habbohotel.users.inventory.WardrobeComponent;
import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;

import java.util.ArrayList;

public class HotLooksMessageComposer extends MessageComposer {
    private final ArrayList<WardrobeComponent.WardrobeItem> looks;

    public HotLooksMessageComposer(ArrayList<WardrobeComponent.WardrobeItem> looks) {
        this.looks = looks;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.hotLooksMessageComposer);
        this.response.appendInt(this.looks.size());
        for (WardrobeComponent.WardrobeItem look : this.looks) {
            this.response.appendString(look.getGender().name());//gender
            this.response.appendString(look.getLook());//figure
        }
        return this.response;
    }
}