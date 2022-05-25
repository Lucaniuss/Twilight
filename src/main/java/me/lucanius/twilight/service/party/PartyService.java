package me.lucanius.twilight.service.party;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Clouke
 * @since 25.05.2022 13:39
 * © Twilight - All Rights Reserved
 */
public interface PartyService {

    Party getParty(UUID uuid);

    PartyService createParty(UUID leader);

    PartyService joinParty(UUID player, UUID leader);

    PartyService leaveParty(UUID player);

    PartyService disbandParty(UUID leader);

    Collection<UUID> getMembers(Party party);

    Collection<Party> getParties();

}
