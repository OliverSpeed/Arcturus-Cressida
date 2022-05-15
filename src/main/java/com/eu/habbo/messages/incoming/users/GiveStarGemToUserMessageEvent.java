package com.eu.habbo.messages.incoming.users;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.achievements.AchievementManager;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.messages.incoming.MessageHandler;

public class GiveStarGemToUserMessageEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int targetId = this.packet.readInt();
        int count = this.packet.readInt();
        count = Emulator.getConfig().getInt("stargem.amount", 1);

        if(count != Emulator.getConfig().getInt("stargem.amount", 1)) {
            // ur a scripter fk u
            return;
        }

        if(this.client.getHabbo().getHabboInfo().getCurrencyAmount(Emulator.getConfig().getInt("stargem.currency.type", 0)) < count) {
            this.client.getHabbo().whisper(Emulator.getTexts().getValue("stargem.not.enough"));
            return;
        }

        Habbo targetHabbo = Emulator.getGameEnvironment().getHabboManager().getHabbo(targetId);

        if(targetHabbo == null || targetHabbo == this.client.getHabbo()) {
            this.client.getHabbo().whisper(Emulator.getTexts().getValue("stargem.invalid.target"));
            return;
        }

        // add stuff
        this.client.getHabbo().givePoints(Emulator.getConfig().getInt("stargem.currency.type", 0), - (Emulator.getConfig().getInt("stargem.amount", 1)));

        if (Emulator.getConfig().getBoolean("stargem.give.currency", false)) {
            targetHabbo.givePoints(Emulator.getConfig().getInt("stargem.currency.type", 0), (Emulator.getConfig().getInt("stargem.amount", 1)));
        }

        targetHabbo.getHabboStats().addStarGems(count);
        targetHabbo.whisper(Emulator.getTexts().getValue("stargem.received.from").replace("%username%", this.client.getHabbo().getHabboInfo().getUsername()));
        AchievementManager.progressAchievement(this.client.getHabbo(), Emulator.getGameEnvironment().getAchievementManager().getAchievement("FriendsMaker"));
        AchievementManager.progressAchievement(targetHabbo, Emulator.getGameEnvironment().getAchievementManager().getAchievement("FriendsMaker_SGCollect_"));
    }
}
