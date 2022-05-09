package com.eu.habbo.messages.incoming.rooms.items;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.rooms.RoomTile;
import com.eu.habbo.habbohotel.users.HabboItem;
import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.rooms.HeightMapUpdateMessageComposer;
import com.eu.habbo.messages.outgoing.rooms.items.RemoveFloorItemComposer;
import com.eu.habbo.messages.outgoing.users.CreditBalanceComposer;
import com.eu.habbo.messages.outgoing.users.ActivityPointsMessageComposer;
import com.eu.habbo.plugin.events.furniture.FurnitureRedeemedEvent;
import com.eu.habbo.threading.runnables.QueryDeleteHabboItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreditFurniRedeemEvent extends MessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreditFurniRedeemEvent.class);

    @Override
    public void handle() throws Exception {
        int itemId = this.packet.readInt();

        Room room = this.client.getHabbo().getHabboInfo().getCurrentRoom();

        if (room != null) {
            HabboItem item = room.getHabboItem(itemId);

            if (item != null && this.client.getHabbo().getHabboInfo().getId() == item.getUserId()) {
                boolean furnitureRedeemEventRegistered = Emulator.getPluginManager().isRegistered(FurnitureRedeemedEvent.class, true);
                FurnitureRedeemedEvent furniRedeemEvent = new FurnitureRedeemedEvent(item, this.client.getHabbo(), 0, FurnitureRedeemedEvent.CREDITS);

                if (item.getBaseItem().getName().startsWith("CF_") || item.getBaseItem().getName().startsWith("CFC_") || item.getBaseItem().getName().startsWith("DF_") || item.getBaseItem().getName().startsWith("PF_")) {
                    if ((item.getBaseItem().getName().startsWith("CF_") || item.getBaseItem().getName().startsWith("CFC_")) && !item.getBaseItem().getName().contains("_diamond_")) {
                        int credits;
                        try {
                            credits = Integer.valueOf(item.getBaseItem().getName().split("_")[1]);
                        } catch (Exception e) {
                            LOGGER.error("Failed to parse redeemable furniture: " + item.getBaseItem().getName() + ". Must be in format of CF_<amount>");
                            return;
                        }

                        furniRedeemEvent = new FurnitureRedeemedEvent(item, this.client.getHabbo(), credits, FurnitureRedeemedEvent.CREDITS);
                    } else if (item.getBaseItem().getName().startsWith("PF_")) {
                        int pixels;

                        try {
                            pixels = Integer.valueOf(item.getBaseItem().getName().split("_")[1]);
                        } catch (Exception e) {
                            LOGGER.error("Failed to parse redeemable pixel furniture: " + item.getBaseItem().getName() + ". Must be in format of PF_<amount>");
                            return;
                        }

                        furniRedeemEvent = new FurnitureRedeemedEvent(item, this.client.getHabbo(), pixels, FurnitureRedeemedEvent.PIXELS);
                    } else if (item.getBaseItem().getName().startsWith("DF_")) {
                        int pointsType;
                        int points;

                        try {
                            pointsType = Integer.valueOf(item.getBaseItem().getName().split("_")[1]);
                        } catch (Exception e) {
                            LOGGER.error("Failed to parse redeemable points furniture: " + item.getBaseItem().getName() + ". Must be in format of DF_<pointstype>_<amount> where <pointstype> equals integer representation of seasonal currency.");
                            return;
                        }

                        try {
                            points = Integer.valueOf(item.getBaseItem().getName().split("_")[2]);
                        } catch (Exception e) {
                            LOGGER.error("Failed to parse redeemable points furniture: " + item.getBaseItem().getName() + ". Must be in format of DF_<pointstype>_<amount> where <pointstype> equals integer representation of seasonal currency.");
                            return;
                        }

                        furniRedeemEvent = new FurnitureRedeemedEvent(item, this.client.getHabbo(), points, pointsType);
                    } else if (item.getBaseItem().getName().startsWith("CF_diamond_")) {
                        int points;

                        try {
                            points = Integer.valueOf(item.getBaseItem().getName().split("_")[2]);
                        } catch (Exception e) {
                            LOGGER.error("Failed to parse redeemable diamonds furniture: " + item.getBaseItem().getName() + ". Must be in format of CF_diamond_<amount>");
                            return;
                        }

                        furniRedeemEvent = new FurnitureRedeemedEvent(item, this.client.getHabbo(), points, FurnitureRedeemedEvent.DIAMONDS);
                    }

                    if (furnitureRedeemEventRegistered) {
                        Emulator.getPluginManager().fireEvent(furniRedeemEvent);

                        if (furniRedeemEvent.isCancelled())
                            return;
                    }

                    if (furniRedeemEvent.amount < 1)
                        return;

                    if (room.getHabboItem(item.getId()) == null) // plugins may cause a lag between which time the item can be removed from the room
                        return;

                    room.removeHabboItem(item);
                    room.sendComposer(new RemoveFloorItemComposer(item).compose());
                    RoomTile t = room.getLayout().getTile(item.getX(), item.getY());
                    t.setStackHeight(room.getStackHeight(item.getX(), item.getY(), false));
                    room.updateTile(t);
                    room.sendComposer(new HeightMapUpdateMessageComposer(item.getX(), item.getY(), t.z, t.relativeHeight()).compose());
                    Emulator.getThreading().run(new QueryDeleteHabboItem(item.getId()));

                    switch (furniRedeemEvent.currencyID) {
                        case FurnitureRedeemedEvent.CREDITS:
                            this.client.getHabbo().getHabboInfo().addCredits(furniRedeemEvent.amount);
                            this.client.sendResponse(new CreditBalanceComposer(this.client.getHabbo()));
                            break;

                        case FurnitureRedeemedEvent.DIAMONDS:
                            this.client.getHabbo().givePoints(furniRedeemEvent.amount);
                            break;

                        case FurnitureRedeemedEvent.PIXELS:
                            this.client.getHabbo().getHabboInfo().addPixels(furniRedeemEvent.amount);
                            this.client.sendResponse(new ActivityPointsMessageComposer(this.client.getHabbo()));
                            break;

                        default:
                            this.client.getHabbo().givePoints(furniRedeemEvent.currencyID, furniRedeemEvent.amount);
                            break;
                    }
                }
            }
        }
    }
}