package com.nilsson.camping.service;

import com.nilsson.camping.data.DataHandler;
import com.nilsson.camping.model.Member;
import com.nilsson.camping.model.registries.MemberRegistry;
import com.nilsson.camping.ui.UIUtil;
import com.nilsson.camping.ui.dialogs.AddMemberDialog;
import com.nilsson.camping.ui.dialogs.EditMemberDialog;
import java.util.Optional;

public class MemberService {

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
        if (selectedMember == null) {
            UIUtil.showErrorAlert("Edit Error", "No Member Selected", "Please select a member to edit.");
            return;
        }

        EditMemberDialog dialog = new EditMemberDialog(selectedMember);
        Optional<Member> result = dialog.showAndWait();

        if (result.isPresent()) {
            Member updatedMember = result.get();

            DataHandler.saveMembers(MemberRegistry.getInstance().getMembers());

            UIUtil.showInfoAlert("Member Updated", "Success",
                    updatedMember.getFirstName() + " " + updatedMember.getLastName() + " has been successfully updated.");
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
            UIUtil.showErrorAlert("Removal Error", "Error", "Could not find member in registry.");
        }

        return wasRemoved;
    }
}