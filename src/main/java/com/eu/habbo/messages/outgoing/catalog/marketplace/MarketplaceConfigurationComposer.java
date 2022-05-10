package com.eu.habbo.messages.outgoing.catalog.marketplace;

import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;

public class MarketplaceConfigurationComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.marketplaceConfigurationComposer);
        this.response.appendBoolean(true);
        this.response.appendInt(1); //Commision Percentage.
        this.response.appendInt(10); //Credits
        this.response.appendInt(5); //Advertisements
        this.response.appendInt(1); //Min price
        this.response.appendInt(1000000); //Max price
        this.response.appendInt(48); //Hours in marketplace
        this.response.appendInt(7); //Days to display
        this.response.appendInt(0); //Selling fee percentage | unknown atm 2 by default
        this.response.appendInt(100000); // Revenue limit
        this.response.appendInt(40000); // Half Tax Limit
        return this.response;
    }
}
