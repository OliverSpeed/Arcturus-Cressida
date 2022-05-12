package com.eu.habbo.messages.outgoing.users;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.habbohotel.guilds.Guild;
import com.eu.habbo.habbohotel.messenger.Messenger;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.habbohotel.users.HabboInfo;
import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExtendedProfileMessageComposer extends MessageComposer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExtendedProfileMessageComposer.class);

    private final HabboInfo habboInfo;
    private Habbo habbo;
    private GameClient viewer;

    public ExtendedProfileMessageComposer(HabboInfo habboInfo, GameClient viewer) {
        this.habboInfo = habboInfo;
        this.viewer = viewer;
    }

    public ExtendedProfileMessageComposer(Habbo habbo, GameClient viewer) {
        this.habbo = habbo;
        this.habboInfo = habbo.getHabboInfo();
        this.viewer = viewer;
    }

    @Override
    protected ServerMessage composeInternal() {
        if (this.habboInfo == null)
            return null;

        this.response.init(Outgoing.extendedProfileMessageComposer);

        this.response.appendInt(this.habboInfo.getId());
        this.response.appendString(this.habboInfo.getUsername());
        this.response.appendString(this.habboInfo.getLook());
        this.response.appendString(this.habboInfo.getMotto());
        this.response.appendString(new SimpleDateFormat("dd-MM-yyyy").format(new Date(this.habboInfo.getAccountCreated() * 1000L)));

        int achievementScore = 0;
        if (this.habbo != null) {
            achievementScore = this.habbo.getHabboStats().getAchievementScore();
        } else {
            try (Connection connection = Emulator.getDatabase().getDataSource().getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT achievement_score FROM users_settings WHERE user_id = ? LIMIT 1")) {
                statement.setInt(1, this.habboInfo.getId());
                try (ResultSet set = statement.executeQuery()) {
                    if (set.next()) {
                        achievementScore = set.getInt("achievement_score");
                    }
                }
            } catch (SQLException e) {
                LOGGER.error("Caught SQL exception", e);
            }
        }
        this.response.appendInt(achievementScore);
        this.response.appendInt(Messenger.getFriendCount(this.habboInfo.getId()));
        this.response.appendBoolean(this.viewer.getHabbo().getMessenger().getFriends().containsKey(this.habboInfo.getId())); //Friend
        this.response.appendBoolean(Messenger.friendRequested(this.viewer.getHabbo().getHabboInfo().getId(), this.habboInfo.getId())); //Friend Request Send
        this.response.appendBoolean(this.habboInfo.isOnline()); // is online | true or false

        List<Guild> guilds = new ArrayList<>();
        if (this.habbo != null) {
            List<Integer> toRemove = new ArrayList<>();
            for (int index = this.habbo.getHabboStats().guilds.size(); index > 0; index--) {
                int i = this.habbo.getHabboStats().guilds.get(index - 1);
                if (i == 0)
                    continue;

                Guild guild = Emulator.getGameEnvironment().getGuildManager().getGuild(i);

                if (guild != null) {
                    guilds.add(guild);
                } else {
                    toRemove.add(i);
                }
            }

            for (int i : toRemove) {
                this.habbo.getHabboStats().removeGuild(i);
            }
        } else {
            guilds = Emulator.getGameEnvironment().getGuildManager().getGuilds(this.habboInfo.getId());
        }

        this.response.appendInt(guilds.size());
        for (Guild guild : guilds) {
            this.response.appendInt(guild.getId());
            this.response.appendString(guild.getName());
            this.response.appendString(guild.getBadge());
            this.response.appendString(Emulator.getGameEnvironment().getGuildManager().getSymbolColor(guild.getColorOne()).valueA);
            this.response.appendString(Emulator.getGameEnvironment().getGuildManager().getSymbolColor(guild.getColorTwo()).valueA);
            this.response.appendBoolean(this.habbo != null && guild.getId() == this.habbo.getHabboStats().guild);
            this.response.appendInt(guild.getOwnerId());
            this.response.appendBoolean(guild.getOwnerId() == this.habboInfo.getId());
        }

        this.response.appendInt(Emulator.getIntUnixTimestamp() - this.habboInfo.getLastOnline()); // last online
        this.response.appendBoolean(true); // profile visible
        this.response.appendBoolean(false); // _SafeStr_1848
        this.response.appendInt(0); // level
        this.response.appendInt(8); // _SafeStr_1849

        int starGems = 0;
        if (this.habbo != null) {
            starGems = this.habbo.getHabboStats().getStarGems(); // your users profile works
        } else {
            try (Connection connection = Emulator.getDatabase().getDataSource().getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT star_gems FROM users_settings WHERE user_id = ? LIMIT 1")) {
                statement.setInt(1, this.habboInfo.getId());
                try (ResultSet set = statement.executeQuery()) {
                    if (set.next()) {
                        starGems = set.getInt("star_gems");
                    }
                }
            } catch (SQLException e) {
                LOGGER.error("Caught SQL exception", e);
            }
        }

        this.response.appendInt(starGems); // star gems
        this.response.appendBoolean(true); // _SafeStr_1850
        this.response.appendBoolean(false); // _SafeStr_1851

        return this.response;
    }
}
