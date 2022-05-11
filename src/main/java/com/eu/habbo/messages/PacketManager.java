package com.eu.habbo.messages;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.messages.incoming.Incoming;
import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.incoming.achievements.RequestAchievementConfigurationEvent;
import com.eu.habbo.messages.incoming.achievements.GetAchievementsEvent;
import com.eu.habbo.messages.incoming.ambassadors.AmbassadorAlertEvent;
import com.eu.habbo.messages.incoming.ambassadors.FollowFriendEvent;
import com.eu.habbo.messages.incoming.camera.*;
import com.eu.habbo.messages.incoming.campaign.OpenCampaignCalendarDoorAsStaffEvent;
import com.eu.habbo.messages.incoming.campaign.OpenCampaignCalendarDoorEvent;
import com.eu.habbo.messages.incoming.catalog.*;
import com.eu.habbo.messages.incoming.hotlooks.*;
import com.eu.habbo.messages.incoming.catalog.marketplace.*;
import com.eu.habbo.messages.incoming.catalog.recycler.PresentOpenEvent;
import com.eu.habbo.messages.incoming.catalog.recycler.RecycleItemsEvent;
import com.eu.habbo.messages.incoming.catalog.recycler.GetRecyclerStatusEvent;
import com.eu.habbo.messages.incoming.catalog.recycler.GetRecyclerPrizesEvent;
import com.eu.habbo.messages.incoming.crafting.*;
import com.eu.habbo.messages.incoming.floorplaneditor.GetOccupiedTilesEvent;
import com.eu.habbo.messages.incoming.floorplaneditor.GetRoomEntryTileEvent;
import com.eu.habbo.messages.incoming.floorplaneditor.UpdateFloorPropertiesEvent;
import com.eu.habbo.messages.incoming.friends.*;
import com.eu.habbo.messages.incoming.gamecenter.*;
import com.eu.habbo.messages.incoming.guardians.ChatReviewGuideDecidesOnOfferEvent;
import com.eu.habbo.messages.incoming.guardians.ChatReviewGuideDetachedEvent;
import com.eu.habbo.messages.incoming.guardians.ChatReviewGuideVoteEvent;
import com.eu.habbo.messages.incoming.guides.*;
import com.eu.habbo.messages.incoming.guilds.*;
import com.eu.habbo.messages.incoming.guilds.forums.*;
import com.eu.habbo.messages.incoming.handshake.*;
import com.eu.habbo.messages.incoming.helper.GetCfhStatusEvent;
import com.eu.habbo.messages.incoming.helper.GetTalentTrackEvent;
import com.eu.habbo.messages.incoming.hotelview.*;
import com.eu.habbo.messages.incoming.inventory.GetBadgesEvent;
import com.eu.habbo.messages.incoming.inventory.GetBotInventoryEvent;
import com.eu.habbo.messages.incoming.inventory.RequestFurniInventoryWhenNotInRoomEvent;
import com.eu.habbo.messages.incoming.inventory.GetPetInventoryEvent;
import com.eu.habbo.messages.incoming.modtool.*;
import com.eu.habbo.messages.incoming.navigator.*;
import com.eu.habbo.messages.incoming.polls.AnswerPollEvent;
import com.eu.habbo.messages.incoming.polls.PollRejectEvent;
import com.eu.habbo.messages.incoming.polls.PollStartEvent;
import com.eu.habbo.messages.incoming.rooms.*;
import com.eu.habbo.messages.incoming.rooms.bots.RemoveBotFromFlatEvent;
import com.eu.habbo.messages.incoming.rooms.bots.PlaceBotEvent;
import com.eu.habbo.messages.incoming.rooms.bots.CommandBotEvent;
import com.eu.habbo.messages.incoming.rooms.bots.GetBotCommandConfigurationDataEvent;
import com.eu.habbo.messages.incoming.rooms.items.*;
import com.eu.habbo.messages.incoming.rooms.items.jukebox.*;
import com.eu.habbo.messages.incoming.rooms.items.lovelock.FriendFurniConfirmLockEvent;
import com.eu.habbo.messages.incoming.rooms.items.rentablespace.RentableSpaceCancelRentEvent;
import com.eu.habbo.messages.incoming.rooms.items.rentablespace.RentableSpaceRentEvent;
import com.eu.habbo.messages.incoming.rooms.items.youtube.SetYoutubeDisplayPlaylistEvent;
import com.eu.habbo.messages.incoming.rooms.items.youtube.GetYoutubeDisplayStatusEvent;
import com.eu.habbo.messages.incoming.rooms.items.youtube.ControlYoutubeDisplayPlaybackEvent;
import com.eu.habbo.messages.incoming.rooms.pets.*;
import com.eu.habbo.messages.incoming.rooms.promotions.PurchaseRoomAdEvent;
import com.eu.habbo.messages.incoming.rooms.promotions.GetRoomAdPurchaseInfoEvent;
import com.eu.habbo.messages.incoming.rooms.promotions.EditEventEvent;
import com.eu.habbo.messages.incoming.rooms.users.*;
import com.eu.habbo.messages.incoming.trading.*;
import com.eu.habbo.messages.incoming.unknown.GetResolutionAchievementsEvent;
import com.eu.habbo.messages.incoming.inventory.GetBadgePointLimitsEvent;
import com.eu.habbo.messages.incoming.users.*;
import com.eu.habbo.messages.incoming.wired.ApplySnapshotEvent;
import com.eu.habbo.messages.incoming.wired.UpdateConditionEvent;
import com.eu.habbo.messages.incoming.wired.UpdateActionEvent;
import com.eu.habbo.messages.incoming.wired.UpdateTriggerEvent;
import com.eu.habbo.plugin.EventHandler;
import com.eu.habbo.plugin.events.emulator.EmulatorConfigUpdatedEvent;
import gnu.trove.map.hash.THashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PacketManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketManager.class);

    private static final List<Integer> logList = new ArrayList<>();
    public static boolean DEBUG_SHOW_PACKETS = false;
    public static boolean MULTI_THREADED_PACKET_HANDLING = false;
    private final THashMap<Integer, Class<? extends MessageHandler>> incoming;
    private final THashMap<Integer, List<ICallable>> callables;
    private final PacketNames names;

    public PacketManager() throws Exception {
        this.incoming = new THashMap<>();
        this.callables = new THashMap<>();
        this.names = new PacketNames();
        this.names.initialize();

        this.registerHandshake();
        this.registerCatalog();
        this.registerEvent();
        this.registerFriends();
        this.registerNavigator();
        this.registerUsers();
        this.registerHotelview();
        this.registerInventory();
        this.registerRooms();
        this.registerPolls();
        this.registerHotlooks();
        this.registerUnknown();
        this.registerModTool();
        this.registerTrading();
        this.registerGuilds();
        this.registerPets();
        this.registerWired();
        this.registerAchievements();
        this.registerFloorPlanEditor();
        this.registerAmbassadors();
        this.registerGuides();
        this.registerCrafting();
        this.registerCamera();
        this.registerGameCenter();
    }

    public PacketNames getNames() {
        return names;
    }

    @EventHandler
    public static void onConfigurationUpdated(EmulatorConfigUpdatedEvent event) {
        logList.clear();

        for (String s : Emulator.getConfig().getValue("debug.show.headers").split(";")) {
            try {
                logList.add(Integer.valueOf(s));
            } catch (NumberFormatException ignored) {

            }
        }
    }

    public void registerHandler(Integer header, Class<? extends MessageHandler> handler) throws Exception {
        if (header < 0)
            return;

        if (this.incoming.containsKey(header)) {
            throw new Exception("Header already registered. Failed to register " + handler.getName() + " with header " + header);
        }

        this.incoming.putIfAbsent(header, handler);
    }

    public void registerCallable(Integer header, ICallable callable) {
        this.callables.putIfAbsent(header, new ArrayList<>());
        this.callables.get(header).add(callable);
    }

    public void unregisterCallables(Integer header, ICallable callable) {
        if (this.callables.containsKey(header)) {
            this.callables.get(header).remove(callable);
        }
    }

    public void unregisterCallables(Integer header) {
        if (this.callables.containsKey(header)) {
            this.callables.clear();
        }
    }

    public void handlePacket(GameClient client, ClientMessage packet) {
        if (client == null || Emulator.isShuttingDown)
            return;

        try {
            if (this.isRegistered(packet.getMessageId())) {
                Class<? extends MessageHandler> handlerClass = this.incoming.get(packet.getMessageId());

                if (handlerClass == null) throw new Exception("Unknown message " + packet.getMessageId());

                if (client.getHabbo() == null && !handlerClass.isAnnotationPresent(NoAuthMessage.class)) {
                    if (DEBUG_SHOW_PACKETS) {
                        LOGGER.warn("Client packet {} requires an authenticated session.", packet.getMessageId());
                    }

                    return;
                }

                final MessageHandler handler = handlerClass.newInstance();

                if (handler.getRatelimit() > 0) {
                    if (client.messageTimestamps.containsKey(handlerClass) && System.currentTimeMillis() - client.messageTimestamps.get(handlerClass) < handler.getRatelimit()) {
                        if (PacketManager.DEBUG_SHOW_PACKETS) {
                            LOGGER.warn("Client packet {} was ratelimited.", packet.getMessageId());
                        }

                        return;
                    } else {
                        client.messageTimestamps.put(handlerClass, System.currentTimeMillis());
                    }
                }

                if (logList.contains(packet.getMessageId()) && client.getHabbo() != null) {
                    LOGGER.info("User {} sent packet {} with body {}", client.getHabbo().getHabboInfo().getUsername(), packet.getMessageId(), packet.getMessageBody());
                }

                handler.client = client;
                handler.packet = packet;

                if (this.callables.containsKey(packet.getMessageId())) {
                    for (ICallable callable : this.callables.get(packet.getMessageId())) {
                        callable.call(handler);
                    }
                }

                if (!handler.isCancelled) {
                    handler.handle();
                }
            }
        } catch (Exception e) {
            LOGGER.error("Caught exception", e);
        }
    }

    boolean isRegistered(int header) {
        return this.incoming.containsKey(header);
    }

    private void registerAmbassadors() throws Exception {
        this.registerHandler(Incoming.ambassadorAlertMessageEvent, AmbassadorAlertEvent.class);
        this.registerHandler(Incoming.followFriendMessageEvent, FollowFriendEvent.class);
    }

    private void registerHotlooks() throws Exception {
        this.registerHandler(Incoming.getHotLooksMessageEvent, getHotLooksMessageEvent.class);
    }

    private void registerCatalog() throws Exception {
        //this.registerHandler(Incoming.getRecyclerPrizesMessageEvent, GetRecyclerPrizesEvent.class);
        this.registerHandler(Incoming.getBundleDiscountRulesetEvent, GetBundleDiscountRulesetEvent.class);
        this.registerHandler(Incoming.getGiftWrappingConfigurationEvent, GetGiftWrappingConfigurationEvent.class);
        this.registerHandler(Incoming.getMarketplaceConfigurationMessageEvent, GetMarketplaceConfigurationEvent.class);
        this.registerHandler(Incoming.getCatalogIndexEvent, GetCatalogIndexEvent.class);
        this.registerHandler(Incoming.buildersClubQueryFurniCountMessageEvent, BuildersClubQueryFurniCountEvent.class);
        this.registerHandler(Incoming.getCatalogPageEvent, GetCatalogPageEvent.class);
        this.registerHandler(Incoming.purchaseFromCatalogAsGiftEvent, PurchaseFromCatalogAsGiftEvent.class);
        this.registerHandler(Incoming.purchaseFromCatalogEvent, PurchaseFromCatalogEvent.class);
        this.registerHandler(Incoming.redeemVoucherMessageEvent, RedeemVoucherEvent.class);
        //this.registerHandler(Incoming.getRecyclerStatusEvent, GetRecyclerStatusEvent.class);
        //this.registerHandler(Incoming.recycleItemsEvent, RecycleItemsEvent.class);
        this.registerHandler(Incoming.presentOpenMessageEvent, PresentOpenEvent.class);
        this.registerHandler(Incoming.getMarketplaceOwnOffersMessageEvent, GetMarketplaceOwnOffersEvent.class);
        this.registerHandler(Incoming.cancelMarketplaceOfferMessageEvent, CancelMarketplaceOfferEvent.class);
        this.registerHandler(Incoming.getMarketplaceOffersMessageEvent, GetMarketplaceOffersEvent.class);
        this.registerHandler(Incoming.getMarketplaceItemStatsEvent, GetMarketplaceItemStatsEvent.class);
        this.registerHandler(Incoming.buyMarketplaceOfferMessageEvent, BuyMarketplaceOfferEvent.class);
        this.registerHandler(Incoming.getMarketplaceCanMakeOfferMessageEvent, GetMarketplaceCanMakeOfferEvent.class);
        this.registerHandler(Incoming.makeOfferMessageEvent, MakeOfferEvent.class);
        this.registerHandler(Incoming.redeemMarketplaceOfferCreditsMessageEvent, RedeemMarketplaceOfferCreditsEvent.class);
        this.registerHandler(Incoming.getSellablePetPalettesEvent, GetSellablePetPalettesEvent.class);
        this.registerHandler(Incoming.approveNameMessageEvent, ApproveNameEvent.class);
        this.registerHandler(Incoming.getClubOffersMessageEvent, GetClubOffersEvent.class);
        this.registerHandler(Incoming.getClubGiftMessageEvent, GetClubGiftInfo.class);
        this.registerHandler(Incoming.getProductOfferEvent, GetProductOfferEvent.class);
        this.registerHandler(Incoming.purchaseTargetedOfferEvent, PurchaseTargetedOfferEvent.class);
        this.registerHandler(Incoming.setTargetedOfferStateEvent, SetTargetedOfferStateEvent.class);
        this.registerHandler(Incoming.selectClubGiftEvent, SelectClubGiftEvent.class);
        this.registerHandler(Incoming.scrGetKickbackInfoMessageEvent, ScrGetKickbackInfoEvent.class);
        this.registerHandler(Incoming.buildersClubPlaceWallItemMessageEvent, BuildersClubPlaceWallItemEvent.class);
        this.registerHandler(Incoming.purchaseVipMembershipExtensionEvent, PurchaseVipMembershipExtensionEvent.class);
    }

    private void registerEvent() throws Exception {
        this.registerHandler(Incoming.openCampaignCalendarDoorAsStaffEvent, OpenCampaignCalendarDoorAsStaffEvent.class);
        this.registerHandler(Incoming.openCampaignCalendarDoorEvent, OpenCampaignCalendarDoorEvent.class);
    }

    private void registerHandshake() throws Exception {
        this.registerHandler(Incoming.clientHelloMessageEvent, ClientHelloEvent.class);
        this.registerHandler(Incoming.initDiffieHandshakeMessageEvent, InitDiffieHandshakeEvent.class);
        this.registerHandler(Incoming.completeDiffieHandshakeMessageEvent, CompleteDiffieHandshakeEvent.class);
        this.registerHandler(Incoming.sSOTicketMessageEvent, SSOTicketEvent.class);
        this.registerHandler(Incoming.uniqueIDMessageEvent, UniqueIDEvent.class);
        this.registerHandler(Incoming.getIgnoredUsersMessageEvent, GetIgnoredUsersEvent.class);
        this.registerHandler(Incoming.latencyPingRequestMessageEvent, LatencyPingRequestEvent.class);
    }

    private void registerFriends() throws Exception {
        this.registerHandler(Incoming.getMOTDMessageEvent, GetMOTDEvent.class);
        this.registerHandler(Incoming.setRelationshipStatusMessageEvent, SetRelationshipStatusEvent.class);
        this.registerHandler(Incoming.removeFriendMessageEvent, RemoveFriendEvent.class);
        this.registerHandler(Incoming.habboSearchMessageEvent, HabboSearchEvent.class);
        this.registerHandler(Incoming.requestFriendMessageEvent, RequestFriendEvent.class);
        this.registerHandler(Incoming.acceptFriendMessageEvent, AcceptFriendEvent.class);
        this.registerHandler(Incoming.declineFriendMessageEvent, DeclineFriendEvent.class);
        this.registerHandler(Incoming.sendMsgMessageEvent, SendMsgEvent.class);
        this.registerHandler(Incoming.getFriendRequestsMessageEvent, GetFriendRequestsEvent.class);
        this.registerHandler(Incoming.visitUserMessageEvent, VisitUserEvent.class);
        this.registerHandler(Incoming.messengerInitMessageEvent, MessengerInitEvent.class);
        this.registerHandler(Incoming.findNewFriendsMessageEvent, FindNewFriendsEvent.class);
        this.registerHandler(Incoming.sendRoomInviteMessageEvent, SendRoomInviteEvent.class);
    }

    private void registerUsers() throws Exception {
        this.registerHandler(Incoming.infoRetrieveMessageEvent, InfoRetrieveEvent.class);
        this.registerHandler(Incoming.getCreditsInfoEvent, GetCreditsInfoEvent.class);
        this.registerHandler(Incoming.scrGetUserInfoMessageEvent, ScrGetUserInfoEvent.class);
        this.registerHandler(Incoming.getSoundSettingsEvent, GetSoundSettingsEvent.class);
        this.registerHandler(Incoming.getTalentTrackLevelMessageEvent, GetTalentTrackLevelEvent.class);
        this.registerHandler(Incoming.getExtendedProfileMessageEvent, GetExtendedProfileEvent.class);
        this.registerHandler(Incoming.getRelationshipStatusInfoMessageEvent, GetRelationshipStatusInfoEvent.class);
        this.registerHandler(Incoming.getWardrobeMessageEvent, GetWardrobeEvent.class);
        this.registerHandler(Incoming.getHotLooksMessageEvent, GetHotLooksMessageEvent.class);
        this.registerHandler(Incoming.saveWardrobeOutfitMessageEvent, SaveWardrobeOutfitEvent.class);
        this.registerHandler(Incoming.changeMottoMessageEvent, ChangeMottoEvent.class);
        this.registerHandler(Incoming.updateFigureDataMessageEvent, UpdateFigureDataEvent.class);
        this.registerHandler(Incoming.setActivatedBadgesEvent, SetActivatedBadgesEvent.class);
        this.registerHandler(Incoming.getSelectedBadgesMessageEvent, GetSelectedBadgesEvent.class);
        this.registerHandler(Incoming.setSoundSettingsEvent, SetSoundSettingsEvent.class);
        this.registerHandler(Incoming.setRoomCameraPreferencesMessageEvent, SetRoomCameraPreferencesEvent.class);
        this.registerHandler(Incoming.setIgnoreRoomInvitesMessageEvent, SetIgnoreRoomInvitesEvent.class);
        this.registerHandler(Incoming.setChatPreferencesMessageEvent, SetChatPreferencesEvent.class);
        this.registerHandler(Incoming.avatarEffectActivatedEvent, AvatarEffectActivatedEvent.class);
        this.registerHandler(Incoming.avatarEffectSelectedEvent, AvatarEffectSelectedEvent.class);
        this.registerHandler(Incoming.eventLogMessageEvent, EventLogEvent.class);
        this.registerHandler(Incoming.newUserExperienceScriptProceedEvent, NewUserExperienceScriptProceedEvent.class);
        this.registerHandler(Incoming.newUserExperienceGetGiftsMessageEvent, NewUserExperienceGetGiftsEvent.class);
        this.registerHandler(Incoming.checkUserNameMessageEvent, CheckUserNameEvent.class);
        this.registerHandler(Incoming.changeUserNameMessageEvent, ChangeUserNameEvent.class);
        this.registerHandler(Incoming.setChatStylePreferenceEvent, SetChatStylePreferenceEvent.class);
        this.registerHandler(Incoming.setUIFlagsMessageEvent, UpdateUIFlagsEvent.class);
    }

    private void registerNavigator() throws Exception {
        this.registerHandler(Incoming.getUserFlatCatsMessageEvent, GetUserFlatCatsEvent.class);
        this.registerHandler(Incoming.popularRoomsSearchMessageEvent, PopularRoomsSearchEvent.class);
        this.registerHandler(Incoming.roomsWithHighestScoreSearchMessageEvent, RoomsWithHighestScoreSearchEvent.class);
        this.registerHandler(Incoming.myRoomsSearchMessageEvent, MyRoomsSearchEvent.class);
        this.registerHandler(Incoming.canCreateRoomMessageEvent, CanCreateRoomEvent.class);
        this.registerHandler(Incoming.getUnreadForumsCountMessageEvent, GetUnreadForumsCountEvent.class);
        this.registerHandler(Incoming.createFlatMessageEvent, CreateFlatEvent.class);
        this.registerHandler(Incoming.getPopularRoomTagsMessageEvent, GetPopularRoomTagsEvent.class);
        //this.registerHandler(Incoming.searchRoomsByTagEvent, SearchRoomsByTagEvent.class);
        this.registerHandler(Incoming.roomTextSearchMessageEvent, RoomTextSearchEvent.class);
        this.registerHandler(Incoming.roomsWhereMyFriendsAreSearchMessageEvent, RoomsWhereMyFriendsAreSearchEvent.class);
        this.registerHandler(Incoming.myFriendsRoomsSearchMessageEvent, MyFriendsRoomsSearchEvent.class);
        this.registerHandler(Incoming.myRoomRightsSearchMessageEvent, MyRoomRightsSearchEvent.class);
        this.registerHandler(Incoming.myGuildBasesSearchMessageEvent, MyGuildBasesSearchEvent.class);
        this.registerHandler(Incoming.myFavouriteRoomsSearchMessageEvent, MyFavouriteRoomsSearchEvent.class);
        this.registerHandler(Incoming.myRoomHistorySearchMessageEvent, MyRoomHistorySearchEvent.class);
        this.registerHandler(Incoming.newNavigatorInitEvent, NewNavigatorInitEvent.class);
        this.registerHandler(Incoming.newNavigatorSearchEvent, NewNavigatorSearchEvent.class);
        this.registerHandler(Incoming.forwardToSomeRoomMessageEvent, ForwardToSomeRoomEvent.class);
        this.registerHandler(Incoming.getUserEventCatsMessageEvent, GetUserEventCatsEvent.class);
        this.registerHandler(Incoming.setNewNavigatorWindowPreferencesMessageEvent, SetNewNavigatorWindowPreferencesEvent.class);
        this.registerHandler(Incoming.deleteRoomMessageEvent, DeleteRoomEvent.class);
        this.registerHandler(Incoming.navigatorSetSearchCodeViewModeMessageEvent, NavigatorSetSearchCodeViewModeEvent.class);
        this.registerHandler(Incoming.navigatorAddCollapsedCategoryMessageEvent, NavigatorAddCollapsedCategoryEvent.class);
        this.registerHandler(Incoming.navigatorRemoveCollapsedCategoryMessageEvent, NavigatorRemoveCollapsedCategoryEvent.class);
        this.registerHandler(Incoming.navigatorAddSavedSearchEvent, NavigatorAddSavedSearchEvent.class);
        this.registerHandler(Incoming.navigatorDeleteSavedSearchEvent, NavigatorDeleteSavedSearchEvent.class);
    }

    private void registerHotelview() throws Exception {
        this.registerHandler(Incoming.quitMessageEvent, QuitEvent.class);
        this.registerHandler(Incoming.getBonusRareInfoMessageEvent, GetBonusRareInfoEvent.class);
        this.registerHandler(Incoming.getPromoArticlesMessageEvent, GetPromoArticlesEvent.class);
        this.registerHandler(Incoming.getCurrentTimingCodeMessageEvent, GetCurrentTimingCodeEvent.class);
        this.registerHandler(Incoming.getConcurrentUsersRewardMessageEvent, HotelViewRequestBadgeRewardEvent.class); //todo not sure
        this.registerHandler(Incoming.incomeRewardClaimMessageEvent, HotelViewClaimBadgeRewardEvent.class); //todo not sure
        this.registerHandler(Incoming.getLimitedOfferAppearingNextEvent, GetLimitedOfferAppearingNextEvent.class);
        this.registerHandler(Incoming.getSecondsUntilMessageEvent, HotelViewRequestSecondsUntilEvent.class);
    }

    private void registerInventory() throws Exception {
        this.registerHandler(Incoming.getBadgesEvent, GetBadgesEvent.class);
        this.registerHandler(Incoming.getBotInventoryEvent, GetBotInventoryEvent.class);
        this.registerHandler(Incoming.requestFurniInventoryEvent, RequestFurniInventoryWhenNotInRoomEvent.class);
        this.registerHandler(Incoming.requestFurniInventoryWhenNotInRoomEvent, RequestFurniInventoryWhenNotInRoomEvent.class);
        this.registerHandler(Incoming.getPetInventoryEvent, GetPetInventoryEvent.class);
    }

    void registerRooms() throws Exception {
        this.registerHandler(Incoming.openFlatConnectionMessageEvent, OpenFlatConnectionEvent.class);
        this.registerHandler(Incoming.getFurnitureAliasesMessageEvent, GetRoomEntryDataEvent.class);// should this be seperate event classes?
        this.registerHandler(Incoming.getHeightMapMessageEvent, GetRoomEntryDataEvent.class); //should this be seperate event classes?
        this.registerHandler(Incoming.rateFlatMessageEvent, RateFlatEvent.class);
        this.registerHandler(Incoming.getGuestRoomMessageEvent, GetGuestRoomEvent.class);
        this.registerHandler(Incoming.saveRoomSettingsMessageEvent, SaveRoomSettingsEvent.class);
        this.registerHandler(Incoming.placeObjectMessageEvent, PlaceObjectEvent.class);
        this.registerHandler(Incoming.moveObjectMessageEvent, MoveObjectEvent.class);
        this.registerHandler(Incoming.moveWallItemMessageEvent, MoveWallItemEvent.class);
        this.registerHandler(Incoming.pickupObjectMessageEvent, PickupObjectEvent.class);
        this.registerHandler(Incoming.requestRoomPropertySet, RequestRoomPropertySet.class);
        this.registerHandler(Incoming.startTypingMessageEvent, StartTypingEvent.class);
        this.registerHandler(Incoming.cancelTypingMessageEvent, CancelTypingEvent.class);
        this.registerHandler(Incoming.useFurnitureMessageEvent, UseFurnitureEvent.class);
        this.registerHandler(Incoming.useWallItemMessageEvent, UseWallItemEvent.class);
        this.registerHandler(Incoming.setRoomBackgroundColorDataEvent, SetRoomBackgroundColorDataEvent.class);
        this.registerHandler(Incoming.setMannequinNameEvent, SetMannequinNameEvent.class);
        this.registerHandler(Incoming.setMannequinFigureEvent, SetMannequinFigureEvent.class);
        this.registerHandler(Incoming.setClothingChangeDataMessageEvent, SetClothingChangeDataEvent.class);
        this.registerHandler(Incoming.setObjectDataMessageEvent, SetObjectDataEvent.class);
        this.registerHandler(Incoming.getRoomSettingsMessageEvent, GetRoomSettingsEvent.class);
        this.registerHandler(Incoming.roomDimmerGetPresetsMessageEvent, RoomDimmerGetPresetsEvent.class);
        this.registerHandler(Incoming.roomDimmerChangeStateMessageEvent, RoomDimmerChangeStateEvent.class);
        this.registerHandler(Incoming.dropCarryItemMessageEvent, DropCarryItemEvent.class);
        this.registerHandler(Incoming.lookToMessageEvent, LookToEvent.class);
        this.registerHandler(Incoming.chatMessageEvent, ChatEvent.class);
        this.registerHandler(Incoming.shoutMessageEvent, ShoutEvent.class);
        this.registerHandler(Incoming.whisperMessageEvent, WhisperEvent.class);
        this.registerHandler(Incoming.avatarExpressionMessageEvent, AvatarExpressionEvent.class);
        this.registerHandler(Incoming.changePostureMessageEvent, ChangePostureEvent.class);
        this.registerHandler(Incoming.danceMessageEvent, DanceEvent.class);
        this.registerHandler(Incoming.signMessageEvent, SignEvent.class);
        this.registerHandler(Incoming.moveAvatarMessageEvent, MoveAvatarEvent.class);
        //this.registerHandler(Incoming.respectUserEvent, RespectUserEvent.class); ;c todo it gone
        this.registerHandler(Incoming.assignRightsMessageEvent, AssignRightsEvent.class);
        this.registerHandler(Incoming.removeOwnRoomRightsRoomMessageEvent, RemoveOwnRoomRightsRoomEvent.class);
        this.registerHandler(Incoming.getFlatControllersMessageEvent, GetFlatControllersEvent.class);
        this.registerHandler(Incoming.removeAllRightsMessageEvent, RemoveAllRightsEvent.class);
        this.registerHandler(Incoming.removeRightsMessageEvent, RemoveRightsEvent.class);
        this.registerHandler(Incoming.placeBotMessageEvent, PlaceBotEvent.class);
        this.registerHandler(Incoming.removeBotFromFlatMessageEvent, RemoveBotFromFlatEvent.class);
        this.registerHandler(Incoming.commandBotEvent, CommandBotEvent.class);
        this.registerHandler(Incoming.getBotCommandConfigurationDataEvent, GetBotCommandConfigurationDataEvent.class);
        this.registerHandler(Incoming.throwDiceMessageEvent, ThrowDiceEvent.class);
        this.registerHandler(Incoming.diceOffMessageEvent, DiceOffEvent.class);
        this.registerHandler(Incoming.spinWheelOfFortuneMessageEvent, SpinWheelOfFortuneEvent.class);
        this.registerHandler(Incoming.creditFurniRedeemMessageEvent, CreditFurniRedeemEvent.class);
        this.registerHandler(Incoming.placePetMessageEvent, PlacePetEvent.class);
        this.registerHandler(Incoming.kickUserMessageEvent, RoomUserKickEvent.class);
        this.registerHandler(Incoming.setCustomStackingHeightEvent, SetCustomStackingHeightEvent.class);
        this.registerHandler(Incoming.enterOneWayDoorMessageEvent, EnterOneWayDoorEvent.class);
        this.registerHandler(Incoming.letUserInMessageEvent, LetUserInEvent.class);
        this.registerHandler(Incoming.customizeAvatarWithFurniMessageEvent, CustomizeAvatarWithFurniEvent.class);
        this.registerHandler(Incoming.placePostItMessageEvent, PlacePostItEvent.class);
        this.registerHandler(Incoming.getItemDataMessageEvent, GetItemDataEvent.class);
        this.registerHandler(Incoming.setItemDataMessageEvent, SetItemDataEvent.class);
        this.registerHandler(Incoming.removeItemMessageEvent, RemoveItemEvent.class);
        this.registerHandler(Incoming.roomDimmerSavePresetMessageEvent, RoomDimmerSavePresetEvent.class);
        this.registerHandler(Incoming.rentableSpaceRentMessageEvent, RentableSpaceRentEvent.class);
        this.registerHandler(Incoming.rentableSpaceCancelRentMessageEvent, RentableSpaceCancelRentEvent.class);
        this.registerHandler(Incoming.updateHomeRoomMessageEvent, UpdateHomeRoomEvent.class);
        this.registerHandler(Incoming.passCarryItemMessageEvent, PassCarryItemEvent.class);
        this.registerHandler(Incoming.muteAllInRoomEvent, MuteAllInRoomEvent.class);
        this.registerHandler(Incoming.getCustomRoomFilterMessageEvent, GetCustomRoomFilterEvent.class);
        this.registerHandler(Incoming.updateRoomFilterMessageEvent, UpdateRoomFilterEvent.class);
        this.registerHandler(Incoming.submitRoomToCompetitionMessageEvent, SubmitRoomToCompetitionEvent.class);
        this.registerHandler(Incoming.getBannedUsersFromRoomMessageEvent, GetBannedUsersFromRoomEvent.class);
        this.registerHandler(Incoming.getOfficialSongIdMessageEvent, GetOfficialSongIdEvent.class);
        this.registerHandler(Incoming.getSongInfoMessageEvent, GetSongInfoEvent.class);
        this.registerHandler(Incoming.addJukeboxDiskEvent, AddJukeboxDiskEvent.class);
        this.registerHandler(Incoming.removeJukeboxDiskEvent, RemoveJukeboxDiskEvent.class);
        this.registerHandler(Incoming.getNowPlayingMessageEvent, GetNowPlayingEvent.class);
        //this.registerHandler(Incoming.jukeBoxEventOne, JukeBoxEventOne.class); todo this doesnt even exist in the swf src for this prod.
        this.registerHandler(Incoming.getJukeboxPlayListMessageEvent, GetJukeboxPlayListEvent.class);
        this.registerHandler(Incoming.addSpamWallPostItMessageEvent, AddSpamWallPostItEvent.class);
        this.registerHandler(Incoming.getRoomAdPurchaseInfoEvent, GetRoomAdPurchaseInfoEvent.class);
        this.registerHandler(Incoming.purchaseRoomAdMessageEvent, PurchaseRoomAdEvent.class);
        this.registerHandler(Incoming.editEventMessageEvent, EditEventEvent.class);
        this.registerHandler(Incoming.ignoreUserMessageEvent, IgnoreUserEvent.class);
        this.registerHandler(Incoming.unignoreUserMessageEvent, UnignoreUserEvent.class);
        this.registerHandler(Incoming.muteUserMessageEvent, RoomUserMuteEvent.class);
        this.registerHandler(Incoming.banUserWithDurationMessageEvent, BanUserWithDurationEvent.class);
        this.registerHandler(Incoming.unbanUserFromRoomMessageEvent, UnbanUserFromRoomEvent.class);
        //this.registerHandler(Incoming.getUserTagsMessageEvent, GetUserTagsEvent.class); todo it gone
        this.registerHandler(Incoming.getYoutubeDisplayStatusMessageEvent, GetYoutubeDisplayStatusEvent.class);
        this.registerHandler(Incoming.controlYoutubeDisplayPlaybackMessageEvent, ControlYoutubeDisplayPlaybackEvent.class);
        this.registerHandler(Incoming.setYoutubeDisplayPlaylistMessageEvent, SetYoutubeDisplayPlaylistEvent.class);
        this.registerHandler(Incoming.addFavouriteRoomMessageEvent, AddFavouriteRoomEvent.class);
        this.registerHandler(Incoming.friendFurniConfirmLockMessageEvent, FriendFurniConfirmLockEvent.class);
        this.registerHandler(Incoming.deleteFavouriteRoomMessageEvent, DeleteFavouriteRoomEvent.class);
        this.registerHandler(Incoming.setRandomStateMessageEvent, SetRandomStateEvent.class);
    }

    void registerPolls() throws Exception {
        this.registerHandler(Incoming.pollRejectEvent, PollRejectEvent.class);
        this.registerHandler(Incoming.pollStartEvent, PollStartEvent.class);
        this.registerHandler(Incoming.pollAnswerEvent, AnswerPollEvent.class);
    }

    void registerModTool() throws Exception {
        this.registerHandler(Incoming.getModeratorRoomInfoMessageEvent, GetModeratorRoomInfoEvent.class);
        this.registerHandler(Incoming.getRoomChatlogMessageEvent, GetRoomChatlogEvent.class);
        this.registerHandler(Incoming.getModeratorUserInfoMessageEvent, GetModeratorUserInfoEvent.class);
        this.registerHandler(Incoming.pickIssuesMessageEvent, PickIssuesEvent.class);
        this.registerHandler(Incoming.closeIssuesMessageEvent, CloseIssuesEvent.class);
        this.registerHandler(Incoming.releaseIssuesMessageEvent, ReleaseIssuesEvent.class);
        this.registerHandler(Incoming.modMessageMessageEvent, ModMessageEvent.class);
        //this.registerHandler(Incoming.modToolWarnMessageEvent, ModToolWarnEvent.class); todo it gone
        this.registerHandler(Incoming.modKickMessageEvent, ModKickEvent.class);
        this.registerHandler(Incoming.moderatorActionMessageEvent, ModeratorActionEvent.class);
        this.registerHandler(Incoming.moderateRoomMessageEvent, ModerateRoomEvent.class);
        this.registerHandler(Incoming.getRoomVisitsMessageEvent, GetRoomVisitsEvent.class);
        this.registerHandler(Incoming.getCfhChatlogMessageEvent, GetCfhChatlogEvent.class);
        //this.registerHandler(Incoming.modToolRequestRoomUserChatlogEvent, ModToolRequestRoomUserChatlogEvent.class); todo it gone
        this.registerHandler(Incoming.getUserChatlogMessageEvent, GetUserChatlogEvent.class);
        this.registerHandler(Incoming.modAlertMessageEvent, ModAlertEvent.class);
        this.registerHandler(Incoming.modMuteMessageEvent, ModMuteEvent.class);
        this.registerHandler(Incoming.modBanMessageEvent, ModBanEvent.class);
        this.registerHandler(Incoming.modTradingLockMessageEvent, ModTradingLockEvent.class);
        this.registerHandler(Incoming.modToolSanctionEvent, ModToolSanctionEvent.class);
        this.registerHandler(Incoming.closeIssueDefaultActionMessageEvent, CloseIssueDefaultActionEvent.class);

        this.registerHandler(Incoming.getPendingCallsForHelpMessageEvent, GetPendingCallsForHelpEvent.class);
        this.registerHandler(Incoming.getGuideReportingStatusMessageEvent, GetGuideReportingStatusEvent.class);
        this.registerHandler(Incoming.chatReviewSessionCreateMessageEvent, ChatReviewSessionCreateEvent.class);
        this.registerHandler(Incoming.callForHelpMessageEvent, CallForHelpEvent.class);
        this.registerHandler(Incoming.callForHelpFromIMMessageEvent, CallForHelpFromIMEvent.class);
        this.registerHandler(Incoming.callForHelpFromForumThreadMessageEvent, CallForHelpFromForumThreadEvent.class);
        this.registerHandler(Incoming.callForHelpFromForumMessageMessageEvent, CallForHelpFromForumMessageEvent.class);
        this.registerHandler(Incoming.callForHelpFromPhotoMessageEvent, CallForHelpFromPhotoEvent.class);
    }

    void registerTrading() throws Exception {
        this.registerHandler(Incoming.openTradingEvent, OpenTradingEvent.class);
        this.registerHandler(Incoming.addItemToTradeEvent, AddItemToTradeEvent.class);
        this.registerHandler(Incoming.addItemsToTradeEvent, AddItemsToTradeEvent.class);
        this.registerHandler(Incoming.removeItemFromTradeEvent, RemoveItemFromTradeEvent.class);
        this.registerHandler(Incoming.acceptTradingEvent, AcceptTradingEvent.class);
        this.registerHandler(Incoming.unacceptTradingEvent, UnacceptTradingEvent.class);
        this.registerHandler(Incoming.confirmAcceptTradingEvent, ConfirmAcceptTradingEvent.class);
        this.registerHandler(Incoming.closeTradingEvent, CloseTradingEvent.class);
        this.registerHandler(Incoming.confirmDeclineTradingEvent, ConfirmDeclineTradingEvent.class);
    }

    void registerGuilds() throws Exception {
        this.registerHandler(Incoming.getGuildCreationInfoMessageEvent, GetGuildCreationInfoEvent.class);
        this.registerHandler(Incoming.getGuildEditorDataMessageEvent, GetGuildEditorDataEvent.class);
        this.registerHandler(Incoming.createGuildMessageEvent, CreateGuildEvent.class);
        this.registerHandler(Incoming.getHabboGroupDetailsMessageEvent, GetHabboGroupDetailsEvent.class);
        this.registerHandler(Incoming.getGuildEditInfoMessageEvent, GetGuildEditInfoEvent.class);
        this.registerHandler(Incoming.getGuildMembersMessageEvent, GetGuildMembersEvent.class);
        this.registerHandler(Incoming.joinHabboGroupMessageEvent, JoinHabboGroupEvent.class);
        this.registerHandler(Incoming.updateGuildIdentityMessageEvent, UpdateGuildIdentityEvent.class);
        this.registerHandler(Incoming.updateGuildBadgeMessageEvent, UpdateGuildBadgeEvent.class);
        this.registerHandler(Incoming.updateGuildColorsMessageEvent, UpdateGuildColorsEvent.class);
        this.registerHandler(Incoming.removeAdminRightsFromMemberMessageEvent, RemoveAdminRightsFromMemberEvent.class);
        this.registerHandler(Incoming.kickMemberMessageEvent, KickMemberEvent.class);
        this.registerHandler(Incoming.updateGuildSettingsMessageEvent, UpdateGuildSettingsEvent.class);
        this.registerHandler(Incoming.approveMembershipRequestMessageEvent, ApproveMembershipRequestEvent.class);
        this.registerHandler(Incoming.rejectMembershipRequestMessageEvent, RejectMembershipRequestEvent.class);
        this.registerHandler(Incoming.addAdminRightsToMemberMessageEvent, AddAdminRightsToMemberEvent.class);
        this.registerHandler(Incoming.selectFavouriteHabboGroupMessageEvent, SelectFavouriteHabboGroupEvent.class);
        this.registerHandler(Incoming.getGuildMembershipsMessageEvent, GetGuildMembershipsEvent.class);
        this.registerHandler(Incoming.getGuildFurniContextMenuInfoMessageEvent, GetGuildFurniContextMenuInfoEvent.class);
        this.registerHandler(Incoming.getMemberGuildItemCountMessageEvent, GetMemberGuildItemCountEvent.class);
        this.registerHandler(Incoming.deselectFavouriteHabboGroupMessageEvent, DeselectFavouriteHabboGroupEvent.class);
        this.registerHandler(Incoming.deactivateGuildMessageEvent, DeactivateGuildEvent.class);
        this.registerHandler(Incoming.getForumsListMessageEvent, GetForumsListEvent.class);
        this.registerHandler(Incoming.getThreadsMessageEvent, GetThreadsEvent.class);
        this.registerHandler(Incoming.getForumStatsMessageEvent, GetForumStatsEvent.class);
        this.registerHandler(Incoming.postMessageMessageEvent, PostMessageEvent.class);
        this.registerHandler(Incoming.updateForumSettingsMessageEvent, UpdateForumSettingsEvent.class);
        this.registerHandler(Incoming.getMessagesMessageEvent, GetMessagesEvent.class);
        this.registerHandler(Incoming.moderateMessageMessageEvent, ModerateMessageEvent.class);
        this.registerHandler(Incoming.moderateThreadMessageEvent, ModerateThreadEvent.class);
        this.registerHandler(Incoming.updateThreadMessageEvent, UpdateThreadEvent.class);
        this.registerHandler(Incoming.getHabboGroupBadgesMessageEvent, GetHabboGroupBadgesEvent.class);

//        this.registerHandler(Incoming.guildForumDataEvent,              GuildForumModerateMessageEvent.class);
//        this.registerHandler(Incoming.guildForumDataEvent,              GuildForumModerateThreadEvent.class);
//        this.registerHandler(Incoming.guildForumDataEvent,              GuildForumPostThreadEvent.class);
//        this.registerHandler(Incoming.guildForumDataEvent,              GuildForumThreadsEvent.class);
//        this.registerHandler(Incoming.guildForumDataEvent,              GuildForumThreadsMessagesEvent.class);
//        this.registerHandler(Incoming.guildForumDataEvent,              GuildForumUpdateSettingsEvent.class);
    }

    void registerPets() throws Exception {
        this.registerHandler(Incoming.getPetInfoMessageEvent, GetPetInfoEvent.class);
        this.registerHandler(Incoming.removePetFromFlatMessageEvent, RemovePetFromFlatEvent.class);
        this.registerHandler(Incoming.respectPetMessageEvent, RespectPetEvent.class);
        this.registerHandler(Incoming.getPetCommandsMessageEvent, GetPetCommandsEvent.class);
        this.registerHandler(Incoming.customizePetWithFurniEvent, CustomizePetWithFurniEvent.class);
        this.registerHandler(Incoming.togglePetRidingPermissionMessageEvent, TogglePetRidingPermissionEvent.class);
        this.registerHandler(Incoming.mountPetMessageEvent, MountPetEvent.class);
        this.registerHandler(Incoming.removeSaddleFromPetMessageEvent, RemoveSaddleFromPetEvent.class);
        this.registerHandler(Incoming.togglePetBreedingPermissionMessageEvent, TogglePetBreedingPermissionEvent.class);
        this.registerHandler(Incoming.compostPlantMessageEvent, CompostPlantEvent.class);
        this.registerHandler(Incoming.breedPetsMessageEvent, BreedPetsEvent.class);
        this.registerHandler(Incoming.movePetMessageEvent, MovePetEvent.class);
        this.registerHandler(Incoming.openPetPackageMessageEvent, OpenPetPackageEvent.class);
        this.registerHandler(Incoming.cancelPetBreedingEvent, CancelPetBreedingEvent.class);
        this.registerHandler(Incoming.confirmPetBreedingEvent, ConfirmPetBreedingEvent.class);
    }

    void registerWired() throws Exception {
        this.registerHandler(Incoming.updateTriggerMessageEvent, UpdateTriggerEvent.class);
        this.registerHandler(Incoming.updateActionMessageEvent, UpdateActionEvent.class);
        this.registerHandler(Incoming.updateConditionMessageEvent, UpdateConditionEvent.class);
        this.registerHandler(Incoming.applySnapshotMessageEvent, ApplySnapshotEvent.class);
    }

    void registerUnknown() throws Exception {
        this.registerHandler(Incoming.getResolutionAchievementsMessageEvent, GetResolutionAchievementsEvent.class);
        this.registerHandler(Incoming.getTalentTrackMessageEvent, GetTalentTrackEvent.class);
        this.registerHandler(Incoming.getBadgePointLimitsEvent, GetBadgePointLimitsEvent.class);
        this.registerHandler(Incoming.getCfhStatusMessageEvent, GetCfhStatusEvent.class);
    }

    void registerFloorPlanEditor() throws Exception {
        this.registerHandler(Incoming.updateFloorPropertiesMessageEvent, UpdateFloorPropertiesEvent.class);
        this.registerHandler(Incoming.getOccupiedTilesMessageEvent, GetOccupiedTilesEvent.class);
        this.registerHandler(Incoming.getRoomEntryTileMessageEvent, GetRoomEntryTileEvent.class);
    }

    void registerAchievements() throws Exception {
        this.registerHandler(Incoming.getAchievementsEvent, GetAchievementsEvent.class);
        //this.registerHandler(Incoming.requestAchievementConfigurationMessageEvent, RequestAchievementConfigurationEvent.class); todo it gonw
    }

    void registerGuides() throws Exception {
        this.registerHandler(Incoming.guideSessionOnDutyUpdateMessageEvent, GuideSessionOnDutyUpdateEvent.class);
        this.registerHandler(Incoming.guideSessionCreateMessageEvent, GuideSessionCreateEvent.class);
        this.registerHandler(Incoming.guideSessionIsTypingMessageEvent, GuideSessionIsTypingEvent.class);
        this.registerHandler(Incoming.guideSessionReportMessageEvent, GuideSessionReportEvent.class);
        this.registerHandler(Incoming.guideSessionFeedbackMessageEvent, GuideSessionFeedbackEvent.class);
        this.registerHandler(Incoming.guideSessionMessageMessageEvent, GuideSessionMessageEvent.class);
        this.registerHandler(Incoming.guideSessionRequesterCancelsMessageEvent, GuideSessionRequesterCancelsEvent.class);
        this.registerHandler(Incoming.guideSessionGuideDecidesMessageEvent, GuideSessionGuideDecidesEvent.class);
        this.registerHandler(Incoming.guideSessionInviteRequesterMessageEvent, GuideSessionInviteRequesterEvent.class);
        this.registerHandler(Incoming.guideSessionGetRequesterRoomMessageEvent, GuideSessionGetRequesterRoomEvent.class);
        this.registerHandler(Incoming.guideSessionResolvedMessageEvent, GuideSessionResolvedEvent.class);

        this.registerHandler(Incoming.chatReviewGuideDetachedMessageEvent, ChatReviewGuideDetachedEvent.class);
        this.registerHandler(Incoming.chatReviewGuideDecidesOnOfferMessageEvent, ChatReviewGuideDecidesOnOfferEvent.class);
        this.registerHandler(Incoming.chatReviewGuideVoteMessageEvent, ChatReviewGuideVoteEvent.class);
    }

    void registerCrafting() throws Exception {
        this.registerHandler(Incoming.getCraftingRecipeEvent, GetCraftingRecipeEvent.class);
        this.registerHandler(Incoming.getCraftableProductsEvent, GetCraftableProductsEvent.class);
        this.registerHandler(Incoming.craftEvent, CraftEvent.class);
        this.registerHandler(Incoming.craftSecretEvent, CraftSecretEvent.class);
        this.registerHandler(Incoming.getCraftingRecipesAvailableEvent, GetCraftingRecipesAvailableEvent.class);
    }

    void registerCamera() throws Exception {
        this.registerHandler(Incoming.renderRoomMessageEvent, RenderRoomEvent.class);
        this.registerHandler(Incoming.requestCameraConfigurationMessageEvent, RequestCameraConfigurationEvent.class);
        this.registerHandler(Incoming.purchasePhotoMessageEvent, PurchasePhotoEvent.class);
        this.registerHandler(Incoming.renderRoomThumbnailMessageEvent, RenderRoomThumbnailEvent.class);
        this.registerHandler(Incoming.publishPhotoMessageEvent, PublishPhotoEvent.class);
    }

    void registerGameCenter() throws Exception {
        this.registerHandler(Incoming.game2CheckGameDirectoryStatusMessageEvent, Game2CheckGameDirectoryStatusEvent.class);
        //this.registerHandler(Incoming.getGameListEvent, GetGameListEvent.class);
        //this.registerHandler(Incoming.getGameStatusEvent, GetGameStatusEvent.class);
        //this.registerHandler(Incoming.joinQueueEvent, JoinQueueEvent.class);
        //this.registerHandler(Incoming.getWeeklyGameRewardWinnersEvent, GetWeeklyGameRewardWinnersEvent.class);
        //this.registerHandler(Incoming.gameUnloadedEvent, GameUnloadedEvent.class);
        //this.registerHandler(Incoming.getWeeklyGameRewardEvent, GetWeeklyGameRewardEvent.class);
        //this.registerHandler(Incoming.game2GetAccountGameStatusEvent, Game2GetAccountGameStatusEvent.class);
    }
}