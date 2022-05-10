package com.eu.habbo.messages.incoming.inventory;

import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.habbohotel.users.HabboBadge;
import com.eu.habbo.habbohotel.users.HabboItem;
import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.inventory.BadgesComposer;
import com.eu.habbo.messages.outgoing.inventory.FurniListComposer;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.THashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class GetBadgesEvent extends MessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetBadgesEvent.class);

    @Override
    public void handle() throws Exception {
        int totalBadges = this.client.getHabbo().getInventory().getBadgesComponent().getBadges().size();

        if (totalBadges == 0) {
            this.client.sendResponse(new FurniListComposer(0, 1, new TIntObjectHashMap<>()));
            return;
        }

        int totalFragments = (int) Math.ceil((double) totalBadges / 1000.0);

        if (totalFragments == 0) {
            totalFragments = 1;
        }

        synchronized (this.client.getHabbo().getInventory().getItemsComponent().getItems()) {
            THashSet<HabboBadge> badges = new THashSet<>();

            Iterator<HabboBadge> iterator = this.client.getHabbo().getInventory().getBadgesComponent().getBadges().stream().iterator();

            int count = 0;
            int fragmentNumber = 0;

            for (int i = this.client.getHabbo().getInventory().getBadgesComponent().getBadges().size(); i-- > 0; ) {

                if (count == 0) {
                    fragmentNumber++;
                }

                try {
                    badges.add(iterator.next());
                    count++;
                } catch (NoSuchElementException e) {
                    LOGGER.error("Caught exception", e);
                    break;
                }

                if (count == 1000) {
                    this.client.sendResponse(new BadgesComposer(fragmentNumber, totalFragments, badges));
                    count = 0;
                    badges.clear();
                }
            }
            if(count > 0 && badges.size() > 0) this.client.sendResponse(new BadgesComposer(fragmentNumber, totalFragments, badges));
        }
    }
}
