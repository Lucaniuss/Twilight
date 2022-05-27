package me.lucanius.twilight.service.party;

import me.lucanius.twilight.service.lobby.hotbar.HotbarItem;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * @author Clouke
 * @since 25.05.2022 13:39
 * © Twilight - All Rights Reserved
 */
public interface PartyService {

    Party getParty(UUID uuid);

    boolean isParty(UUID uuid);

    boolean isLeader(UUID uuid);

    PartyService createParty(Player leader);

    PartyService joinParty(Player player, UUID leader);

    PartyService leaveParty(Player player);

    PartyService disbandParty(Party party);

    PartyService invite(Player receiver, Player sender);

    PartyService accept(Player player);

    Collection<Party> getParties();

    Map<UUID, UUID> getInvites();

    Collection<HotbarItem> getItems();

}
