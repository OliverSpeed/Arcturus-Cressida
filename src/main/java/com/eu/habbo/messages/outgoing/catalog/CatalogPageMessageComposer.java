package com.eu.habbo.messages.outgoing.catalog;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.catalog.CatalogFeaturedPage;
import com.eu.habbo.habbohotel.catalog.CatalogItem;
import com.eu.habbo.habbohotel.catalog.CatalogPage;
import com.eu.habbo.habbohotel.catalog.layouts.FrontPageFeaturedLayout;
import com.eu.habbo.habbohotel.catalog.layouts.FrontpageLayout;
import com.eu.habbo.habbohotel.catalog.layouts.RecentPurchasesLayout;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CatalogPageMessageComposer extends MessageComposer {
    private final CatalogPage page;
    private final Habbo habbo;
    private final int offerId;
    private final String mode;

    public CatalogPageMessageComposer(CatalogPage page, Habbo habbo, int offerId, String mode) {
        this.page = page;
        this.habbo = habbo;
        this.offerId = offerId;
        this.mode = mode;
    }

    @Override
    protected ServerMessage composeInternal() {
/* use this to 1:1 test
        if(this.page.getId() == 1227857) {

            this.response.init(Outgoing.catalogPageMessageComposer);
            this.response.appendInt(this.page.getId());
            this.response.appendString(this.mode);
            this.response.appendString("frontpage4");

            this.response.appendInt(2);
            this.response.appendString("catalog_frontpage_headline_shop_GENERAL");
            this.response.appendString("frontpage_teaser");

            this.response.appendInt(2);
            this.response.appendString("<i><b><font color=\\\"#0E668C\\\" size=\\\"16\\\">What can I find in the shop?</font></b></i><br><br>Upgrade your clothing with Habbo Club, adopt a pet or decorate your room the way you like it. Whatever you like to do most in Habbo, you'll find a way to make that experience even better by browsing our Shop.<br><br><li>Join <a href=\\\"event:catalog/open/habbo_club\\\">Habbo Club</a></li><li><a href=\\\"event:catalog/open/set_pixelnew\\\">Decorate </a>your room</li><li>Adopt a <a href=\\\"event:catalog/open/pet_horse\\\">Pet</a>, <a href=\\\"event:catalog/open/bots\\\">Bot</a> or <a href=\\\"event:catalog/open/monster_plants_info\\\">Monsterplants</a></li><li>Make your own <a href=\\\"event:catalog/open/category_wired\\\">Wired</a> games </li><li>Trade in our <a href=\\\"event:catalog/open/marketplace_offers\\\">Marketplace</a></li><br><i><b><font color=\\\"#0E668C\\\" size=\\\"16\\\">Where can I get credits?</font></b></i><br><br>We have many methods of payment such as SMS, Home Phone, Prepaid Cards, and Credit Cards.<br><br><a href=\\\"event:habblet/open/credits\\\">Visit our credits page for more info.</a>");
            this.response.appendString("Redeem a voucher code here:");

            this.response.appendInt(0);
            this.response.appendInt(-1);
            this.response.appendBoolean(false);

            this.response.appendInt(1);

            this.response.appendInt(1);
            this.response.appendString("Haunted Library Bundle");
            this.response.appendString("catalogue/feature_cata_vert_hween20_bun5.png");
            this.response.appendInt(0);
            this.response.appendString("house20bundc");
            this.response.appendInt(563506);

            this.response.appendInt(4);

            this.response.appendInt(1);
            this.response.appendString("Haunted Library Bundle");
            this.response.appendString("catalogue/feature_cata_vert_hween20_bun5.png\"");
            this.response.appendInt(0);
            this.response.appendString("house20bundc");
            this.response.appendInt(563506);

            this.response.appendInt(2);
            this.response.appendString("Gallery Bundle XI");
            this.response.appendString("catalogue/feature_cata_hort_GalleryXI_May22.png\"");
            this.response.appendInt(0);
            this.response.appendString("may22gal1");
            this.response.appendInt(476926);

            this.response.appendInt(3);
            this.response.appendString("Celestial Bundle");
            this.response.appendString("catalogue/feature_cata_vert_hween20_bun5.png\"");
            this.response.appendInt(0);
            this.response.appendString("pride19celestial");
            this.response.appendInt(131506);

            this.response.appendInt(4);
            this.response.appendString("Diamond Paintings!");
            this.response.appendString("catalogue/feature_cata_hort_Diamond_FoggyFrank_May22.png\"");
            this.response.appendInt(0);
            this.response.appendString("set_posters");
            this.response.appendInt(-1);

            return this.response;
        }
*/
        this.response.init(Outgoing.catalogPageMessageComposer);
        this.response.appendInt(this.page.getId());
        this.response.appendString(this.mode);
        this.page.serialize(this.response);

        if (this.page instanceof RecentPurchasesLayout) {
            this.response.appendInt(this.habbo.getHabboStats().getRecentPurchases().size());

            for (Map.Entry<Integer, CatalogItem> item : this.habbo.getHabboStats().getRecentPurchases().entrySet()) {
                item.getValue().serialize(this.response);
            }
        } else {
            this.response.appendInt(this.page.getCatalogItems().size());
            List<CatalogItem> items = new ArrayList<>(this.page.getCatalogItems().valueCollection());
            Collections.sort(items);
            for (CatalogItem item : items) {
                item.serialize(this.response);
            }
        }
        this.response.appendInt(this.offerId);
        this.response.appendBoolean(false); //acceptSeasonCurrencyAsCredits

        if (this.page instanceof FrontPageFeaturedLayout) {
            ((FrontPageFeaturedLayout)this.page).serializeExtra(this.response);
        }

        if (this.page instanceof FrontpageLayout) {
            ((FrontpageLayout)this.page).serializeExtra(this.response);
        }

        return this.response;
    }
}
