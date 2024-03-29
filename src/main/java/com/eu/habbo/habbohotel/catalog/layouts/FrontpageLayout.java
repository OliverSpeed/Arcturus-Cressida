package com.eu.habbo.habbohotel.catalog.layouts;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.catalog.CatalogFeaturedPage;
import com.eu.habbo.habbohotel.catalog.CatalogPage;
import com.eu.habbo.messages.ServerMessage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FrontpageLayout extends CatalogPage {

    public FrontpageLayout(ResultSet set) throws SQLException {
        super(set);
    }

    @Override
    public void serialize(ServerMessage message) {
        message.appendString("frontpage4");
        message.appendInt(2);
        message.appendString(super.getHeaderImage());
        message.appendString(super.getTeaserImage());
        message.appendInt(3);
        message.appendString(super.getTextOne());
        message.appendString(super.getTextTwo());
        message.appendString(super.getTextTeaser());
    }

    public void serializeExtra(ServerMessage message) {
        message.appendInt(Emulator.getGameEnvironment().getCatalogManager().getCatalogFeaturedPages().size());

        for (CatalogFeaturedPage page : Emulator.getGameEnvironment().getCatalogManager().getCatalogFeaturedPages().valueCollection()) {
            page.serialize(message);
        }
    }
}
