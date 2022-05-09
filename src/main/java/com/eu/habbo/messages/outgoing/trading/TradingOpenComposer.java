package com.eu.habbo.messages.outgoing.trading;

import com.eu.habbo.habbohotel.rooms.RoomTrade;
import com.eu.habbo.habbohotel.rooms.RoomTradeUser;
import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;

public class TradingOpenComposer extends MessageComposer {
    private final RoomTrade roomTrade;
    private final int state;

    public TradingOpenComposer(RoomTrade roomTrade) {
        this.roomTrade = roomTrade;
        this.state = 1;
    }

    public TradingOpenComposer(RoomTrade roomTrade, int state) {
        this.roomTrade = roomTrade;
        this.state = state;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.tradingOpenComposer);
        for (RoomTradeUser tradeUser : this.roomTrade.getRoomTradeUsers()) {
            this.response.appendInt(tradeUser.getHabbo().getHabboInfo().getId());
            this.response.appendInt(this.state);
        }
        return this.response;
    }
}
