package com.eu.habbo.messages.outgoing.achievements;

import com.eu.habbo.habbohotel.achievements.Achievement;
import com.eu.habbo.habbohotel.achievements.AchievementLevel;
import com.eu.habbo.habbohotel.achievements.AchievementManager;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;

public class AchievementComposer extends MessageComposer {
    private final Habbo habbo;
    private final Achievement achievement;

    public AchievementComposer(Habbo habbo, Achievement achievement) {
        this.habbo = habbo;
        this.achievement = achievement;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.achievementComposer);
        
            int achievementProgress = this.habbo.getHabboStats().getAchievementProgress(this.achievement);
            AchievementLevel currentLevel = this.achievement.getLevelForProgress(achievementProgress);
            AchievementLevel nextLevel = this.achievement.getNextLevel(currentLevel != null ? currentLevel.level : 0);

            if(currentLevel == null) {
                currentLevel = this.achievement.firstLevel();
            }

            if(nextLevel == null) {
                nextLevel = currentLevel;
            }

            this.response.appendInt(this.achievement.id); //ID
            this.response.appendInt(nextLevel.level); // next level
            this.response.appendString("ACH_" + this.achievement.name + nextLevel.level); //Target badge code
            this.response.appendInt(currentLevel.progress); //Last level progress needed
            this.response.appendInt(nextLevel.progress); //Progress needed
            this.response.appendInt(nextLevel.rewardAmount); //Reward amount
            this.response.appendInt(nextLevel.rewardType); //Reward currency ID
            this.response.appendInt(Math.max(achievementProgress, 0)); //Current progress
            this.response.appendBoolean(currentLevel == nextLevel && achievementProgress >= nextLevel.progress); //Achieved? (Current Progress == MaxLevel.Progress)
            this.response.appendString(this.achievement.category.name()); //Category
            this.response.appendString(""); //Sub category
            this.response.appendInt(this.achievement.levels.size()); //Count of total levels in this achievement
            this.response.appendInt(currentLevel == nextLevel && achievementProgress >= nextLevel.progress ? 1 : 0); //1 = Progressbar visible if the achievement is completed
            this.response.appendShort(this.achievement.state); //state - 0 - disabled, 1 = enabled, 2 = archive
        
        return this.response;
    }
}
