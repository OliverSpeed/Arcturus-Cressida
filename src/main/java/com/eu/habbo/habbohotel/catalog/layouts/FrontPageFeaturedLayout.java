package com.eu.habbo.habbohotel.catalog.layouts;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.catalog.CatalogFeaturedPage;
import com.eu.habbo.habbohotel.catalog.CatalogPage;
import com.eu.habbo.messages.ServerMessage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.stream.Collectors;

public class FrontPageFeaturedLayout extends CatalogPage {
    public FrontPageFeaturedLayout(ResultSet set) throws SQLException {
        super(set);
    }

    @Override
    public void serialize(ServerMessage message) {
        message.appendString("frontpage4");
        String[] teaserImages = super.getTeaserImage().split(";");
        String[] specialImages = super.getSpecialImage().split(";");

        message.appendInt(2 ); // + teaserImages.length + specialImages.length
        message.appendString(super.getHeaderImage());
        message.appendString("frontpage_teaser");

        /*
        for (String s : teaserImages) {
            message.appendString(s);
        }

        for (String s : specialImages) {
            message.appendString(s);
        }
         */


        message.appendInt(2);
        message.appendString(super.getTextOne());
        message.appendString(super.getTextDetails());
        //message.appendString(super.getTextTeaser());
    }

    public void serializeExtra(ServerMessage message) {

        message.appendInt(Emulator.getGameEnvironment().getCatalogManager().getCatalogFeaturedPages().size());

        for (CatalogFeaturedPage page : Emulator.getGameEnvironment().getCatalogManager().getCatalogFeaturedPages().valueCollection().stream().sorted(Comparator.comparingInt(CatalogFeaturedPage::getSlotId)).collect(Collectors.toList())) {
            page.serialize(message);
        }

    }
}