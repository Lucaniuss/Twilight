package me.lucanius.twilight.service.party.impl;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.game.Game;
import me.lucanius.twilight.service.lobby.hotbar.HotbarItem;
import me.lucanius.twilight.service.party.Party;
import me.lucanius.twilight.service.party.PartyService;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.profile.ProfileState;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.Clickable;
import me.lucanius.twilight.tools.functions.Voluntary;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Clouke
 * @since 25.05.2022 13:41
 * © Twilight - All Rights Reserved
 */
public class StandardPartyService implements PartyService {

    private final Twilight plugin;
    private final Map<UUID, Party> parties;
    private final Map<UUID, UUID> invites;

    public StandardPartyService(Twilight plugin) {
        this.plugin = plugin;
        this.parties = new HashMap<>();
        this.invites = new HashMap<>();
    }

    @Override
    public Party getParty(UUID uuid) {
        return parties.values().stream().filter(party -> party.getMembers().contains(uuid)).findFirst().orElse(null);
    }

    @Override
    public boolean isParty(UUID uuid) {
        return parties.values().stream().anyMatch(party -> party.getMembers().contains(uuid));
    }

    @Override
    public boolean isLeader(UUID uuid) {
        return parties.containsKey(uuid);
    }

    @Override
    public PartyService createParty(Player leader) {
        UUID uniqueId = leader.getUniqueId();
        Party party = new Party(uniqueId);
        parties.put(uniqueId, party);

        leader.getInventory().clear();
        getItems().forEach(item -> leader.getInventory().setItem(item.getSlot(), item.getItem()));

        leader.sendMessage(CC.SECOND + "Successfully created a party.");

        return this;
    }

    @Override
    public PartyService joinParty(Player player, UUID leader) {
        Party party = getParty(leader);
        if (party == null) {
            return null;
        }

        party.add(player.getUniqueId());

        player.getInventory().clear();
        getItems().forEach(item -> player.getInventory().setItem(item.getSlot(), item.getItem()));

        party.broadcast(CC.MAIN + player.getName() + CC.SECOND + " joined the party.");

        return this;
    }

    @Override
    public PartyService leaveParty(Player player) {
        UUID uniqueId = player.getUniqueId();
        Party party = getParty(uniqueId);
        if (party == null) {
            return null;
        }

        if (isLeader(uniqueId)) {
            disbandParty(party);
        } else {
            party.broadcast(CC.MAIN + player.getName() + CC.SECOND + " left the party.");
            party.remove(uniqueId);
        }

        Profile profile = plugin.getProfiles().get(uniqueId);
        Voluntary<Game> game = Voluntary.ofNull(plugin.getGames().get(profile));
        switch (profile.getState()) {
            case PLAYING:
                game.ifPresent(value -> value.getLoadout().getType().getCallable().execute(plugin, player, plugin.getDamages().get(uniqueId), value));
                break;
            case SPECTATING:
                game.ifPresent(value -> value.removeSpectator(uniqueId));
                break;
        }

        plugin.getLobby().toLobby(player, profile, false);
        return this;
    }

    @Override
    public PartyService disbandParty(Party party) {
        parties.remove(party.getLeader(), party);
        party.broadcast(CC.SECOND + "The party has been disbanded.");

        party.getMembers().stream()
                .map(member -> plugin.getProfiles().get(member))
                .filter(profile -> profile.getState().equals(ProfileState.LOBBY))
                .forEach(profile -> plugin.getLobby().toLobby(profile, false));

        return this;
    }

    @Override
    public PartyService invite(Player receiver, Player sender) {
        UUID uuid = receiver.getUniqueId();
        if (isParty(uuid)) {
            return null;
        }

        invites.put(uuid, sender.getUniqueId());
        new Clickable(
                CC.SECOND + "You've been invited to join " + CC.MAIN + sender.getName() + "'s " + CC.SECOND + "party",
                CC.SECOND + CC.ITALIC + "Click to join",
                "/party accept"
        ).send(receiver);
        return this;
    }

    @Override
    public PartyService accept(Player player) {
        UUID uuid = player.getUniqueId();
        if (!invites.containsKey(uuid)) {
            return null;
        }

        return joinParty(player, invites.remove(uuid));
    }

    @Override
    public Collection<Party> getParties() {
        return parties.values();
    }

    @Override
    public Map<UUID, UUID> getInvites() {
        return invites;
    }

    @Override
    public Collection<HotbarItem> getItems() {
        return plugin.getLobby().getPartyItems();
    }
}
