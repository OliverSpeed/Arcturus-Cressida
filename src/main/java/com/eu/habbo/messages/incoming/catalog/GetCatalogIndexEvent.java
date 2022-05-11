package com.eu.habbo.messages.incoming.catalog;

import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.catalog.BuildersClubFurniCountMessageComposer;
import com.eu.habbo.messages.outgoing.catalog.CatalogIndexMessageComposer;

public class GetCatalogIndexEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {

        String MODE = this.packet.readString();
        if (MODE.equalsIgnoreCase("NORMAL")) {
            this.client.sendResponse(new BuildersClubFurniCountMessageComposer(0));
            this.client.sendResponse(new CatalogIndexMessageComposer(this.client.getHabbo(), MODE));
        } else {
            this.client.sendResponse(new BuildersClubFurniCountMessageComposer(1));
            this.client.sendResponse(new CatalogIndexMessageComposer(this.client.getHabbo(), MODE));
        }

    }
}
