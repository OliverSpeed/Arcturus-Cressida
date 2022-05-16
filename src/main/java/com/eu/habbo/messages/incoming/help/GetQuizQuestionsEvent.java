package com.eu.habbo.messages.incoming.help;

import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.habboway.QuizDataMessageComposer;
import com.eu.habbo.messages.outgoing.unknown.QuizResultsMessageComposer;

import java.util.Collections;

public class GetQuizQuestionsEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        /* what the fuck is this shit again i hate networking

            new QuizDataMessageEvent(this.onQuizData)
            new QuizResultsMessageEvent(this.onQuizResults)
         */
        this.client.sendResponse(new QuizDataMessageComposer(this.packet.readString(), new int[]{1,2,3,4,5}));
        this.client.sendResponse(new QuizResultsMessageComposer(this.packet.readString(), Collections.singletonList(this.packet.readInt())));
    }
}
