package com.eu.habbo.messages.incoming.quests;

import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.habboway.QuizDataMessageComposer;

public class GetQuestsMessageEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        /*
            this._questEngine = _arg_1;
            this._questTracker = new QuestTracker(this._questEngine);
            this._questsList = new QuestsList(this._questEngine);
            this._questDetails = new QuestDetails(this._questEngine);
            this._SafeStr_3131 = new QuestCompleted(this._questEngine);
            this._SafeStr_3132 = new NextQuestTimer(this._questEngine);
            this._seasonalCalendarWindow = new MainWindow(this._questEngine);
         */
        //client.send(new QuestListMessageComposer(QuestManager.getInstance().getQuests(), client.getPlayer(), true));
        //this.client.sendResponse(new QuestsListMessageComposer(this.packet.readString(), ));
    }
}
