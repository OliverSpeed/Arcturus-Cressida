package com.eu.habbo.messages.outgoing.achievements;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.achievements.Achievement;
import com.eu.habbo.habbohotel.achievements.AchievementLevel;
import com.eu.habbo.habbohotel.achievements.AchievementManager;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AchievementsComposer extends MessageComposer {

    private final Habbo habbo;

    public AchievementsComposer(Habbo habbo) {
        this.habbo = habbo;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.achievementsComposer);

        this.response.appendInt(Emulator.getGameEnvironment().getAchievementManager().getAchievements().size());

        for(Achievement achievement : Emulator.getGameEnvironment().getAchievementManager().getAchievements().values())
        {
            int achievementProgress = this.habbo.getHabboStats().getAchievementProgress(achievement);
            AchievementLevel currentLevel = achievement.getLevelForProgress(achievementProgress);
            AchievementLevel nextLevel = achievement.getNextLevel(currentLevel != null ? currentLevel.level : 0);

            if(currentLevel == null) {
                currentLevel = achievement.firstLevel();
            }

            if(nextLevel == null) {
                nextLevel = currentLevel;
            }

            this.response.appendInt(achievement.id); //ID
            this.response.appendInt(nextLevel.level); // next level
            this.response.appendString("ACH_" + achievement.name + nextLevel.level); //Target badge code
            this.response.appendInt(currentLevel.progress); //Last level progress needed
            this.response.appendInt(nextLevel.progress); //Progress needed
            this.response.appendInt(nextLevel.rewardAmount); //Reward amount
            this.response.appendInt(nextLevel.rewardType); //Reward currency ID
            this.response.appendInt(Math.max(achievementProgress, 0)); //Current progress
            this.response.appendBoolean(currentLevel == nextLevel && achievementProgress >= nextLevel.progress); //Achieved? (Current Progress == MaxLevel.Progress)
            this.response.appendString(achievement.category.name()); //Category
            this.response.appendString(""); //Sub category
            this.response.appendInt(achievement.levels.size()); //Count of total levels in this achievement
            this.response.appendInt(currentLevel == nextLevel && achievementProgress >= nextLevel.progress ? 1 : 0); //1 = Progressbar visible if the achievement is completed
            if (achievement.category.name() == "archive") {
                this.response.appendShort(2); //state - 0 - disabled, 1 = enabled, 2 = archive
            } else {
                this.response.appendShort(1); //state - 0 - disabled, 1 = enabled, 2 = archive
            }

        }

        this.response.appendString("");

        return this.response;
    }
}