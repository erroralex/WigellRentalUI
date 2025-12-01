package com.nilsson.camping.service;

import com.nilsson.camping.model.Member;
import com.nilsson.camping.model.registries.MemberRegistry;
import com.nilsson.camping.ui.UIUtil;
import com.nilsson.camping.ui.dialogs.AddMemberDialog;

import java.util.Optional;

public class MembershipService {

    public Member handleAddMember() {
        // Display the input form and collect data
        AddMemberDialog dialog = new AddMemberDialog();
        Optional<Member> result = dialog.showAndWait();

        if (result.isPresent()) {
            Member newMemberData = result.get();

            // Create a new Member object
            MemberRegistry registry = MemberRegistry.getInstance();
            int newID = registry.getUniqueID();

            // Assign unique ID
            Member newMember = new Member(
                    newID,
                    newMemberData.getFirstName(),
                    newMemberData.getLastName(),
                    newMemberData.getMembershipLevel(),
                    null
            );

            // Add to registry
            registry.addMember(newMember);

            // Show success confirmation
            UIUtil.showInfoAlert("Member Added", "Success",
                    newMember.getFirstName() + " " + newMember.getLastName() + " (ID: " + newMember.getId() + ") has been successfully added.");
            return newMember;
        }
        // Cancel was clicked
        return null;
    }


    // Accept the Member object from the View
    public void handleEditMember(Member selectedMember) {
        if (selectedMember != null) {
            UIUtil.showInfoAlert("Edit Member", "Functionality Pending",
                    "A dialog/form for editing member " + selectedMember.getFirstName() + " will be implemented here.");
        } else {
            // Error handling remains in the service, but ideally, the view handles null
        }
    }

    /**
     * Attempts to remove a member from the registry and triggers persistence.
     * @param selectedMember The Member object to remove.
     * @return true if the member was successfully removed, false otherwise.
     */
    public boolean removeMemberFromRegistry(Member selectedMember) {
        if (selectedMember == null) {
            return false;
        }

        // Call the Registry's remove method.
        boolean wasRemoved = MemberRegistry.getInstance().removeMember(selectedMember);

        if (wasRemoved) {
            UIUtil.showInfoAlert("Member Removed", "Success",
                    selectedMember.getFirstName() + " " + selectedMember.getLastName() + " has been successfully removed.");
        } else {
            // This might happen if the member exists in the UI but was somehow already removed from the registry
            UIUtil.showErrorAlert("Removal Error", "Error", "Could not find member in registry.");
        }

        return wasRemoved;
    }
}