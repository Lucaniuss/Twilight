package me.lucanius.twilight.service.party.impl;

import me.lucanius.twilight.service.party.Party;
import me.lucanius.twilight.service.party.PartyService;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Clouke
 * @since 25.05.2022 13:41
 * © Twilight - All Rights Reserved
 */
public class StandardPartyService implements PartyService {

    @Override
    public Party getParty(UUID uuid) {
        return null;
    }

    @Override
    public PartyService createParty(UUID leader) {
        return null;
    }

    @Override
    public PartyService joinParty(UUID player, UUID leader) {
        return null;
    }

    @Override
    public PartyService leaveParty(UUID player) {
        return null;
    }

    @Override
    public PartyService disbandParty(UUID leader) {
        return null;
    }

    @Override
    public Collection<UUID> getMembers(Party party) {
        return null;
    }

    @Override
    public Collection<Party> getParties() {
        return null;
    }
}
