package com.nilsson.camping.model.registries;

import com.nilsson.camping.data.DataHandler;
import com.nilsson.camping.model.Member;

import java.util.*;

public class MemberRegistry {

    private List<Member> membersList = new ArrayList<>();
    private Set<Integer> usedIDs = new HashSet<>();

    private MemberRegistry() {
        // Load members from JSON file via DataHandler on instantiation
        loadMembersFromDataHandler();
    }

    public static MemberRegistry getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final MemberRegistry INSTANCE = new MemberRegistry();
    }

    // Generates a unique 4-digit ID between 1000 and 9999.
    public int getUniqueID() {
        Random random = new Random();

        while (true) {
            int id = 1000 + random.nextInt(9000);
            if (usedIDs.add(id)) {
                return id;
            }
        }
    }

    public List<Member> getMembers() {
        return membersList;
    }

    public void addMember(Member member) {
        membersList.add(member);
        // Ensure the ID of the new member is registered as used
        usedIDs.add(member.getId());
    }

    // Loads members using the DataHandler and populates the registry and usedIDs set.
    private void loadMembersFromDataHandler() {
        this.membersList = DataHandler.loadMembers();

        // Populate the usedIDs set from the loaded members to ensure uniqueness for new members
        for (Member member : membersList) {
            usedIDs.add(member.getId());
        }
    }
}