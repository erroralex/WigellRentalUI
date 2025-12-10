package com.nilsson.camping.service;

import com.nilsson.camping.app.LanguageManager;
import com.nilsson.camping.data.DataHandler;
import com.nilsson.camping.model.Member;
import com.nilsson.camping.model.registries.MemberRegistry;
import com.nilsson.camping.ui.UIUtil;
import com.nilsson.camping.ui.dialogs.AddMemberDialog;
import com.nilsson.camping.ui.dialogs.EditMemberDialog;
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
            UIUtil.showInfoAlert(
                    LanguageManager.getInstance().getString("msg.memberAdded"),
                    LanguageManager.getInstance().getString("msg.success"),
                    newMember.getFirstName() + " " + newMember.getLastName() + " (ID: " + newMember.getId() + ")" +
                            LanguageManager.getInstance().getString("msg.addedSuccess"));
            return newMember;
        }
        // Cancel was clicked
        return null;
    }

    // Accept the Member object from the View
    public void handleEditMember(Member selectedMember) {
        if (selectedMember == null) {
            UIUtil.showErrorAlert(
                    LanguageManager.getInstance().getString("error.editError"),
                    LanguageManager.getInstance().getString("error.missingMember"),
                    LanguageManager.getInstance().getString("error.pleaseSelectEditMember"));
            return;
        }

        EditMemberDialog dialog = new EditMemberDialog(selectedMember);
        Optional<Member> result = dialog.showAndWait();

        if (result.isPresent()) {
            Member updatedMember = result.get();

            DataHandler.saveMembers(MemberRegistry.getInstance().getMembers());

            UIUtil.showInfoAlert(
                    LanguageManager.getInstance().getString("msg.memberUpdated"),
                    LanguageManager.getInstance().getString("msg.success"),
                    updatedMember.getFirstName() + " " + updatedMember.getLastName() +
                            LanguageManager.getInstance().getString("msg.updateSuccess"));
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
            UIUtil.showInfoAlert(
                    LanguageManager.getInstance().getString("msg.memberRemoved"),
                    LanguageManager.getInstance().getString("msg.success"),
                    selectedMember.getFirstName() + " " + selectedMember.getLastName() +
                            LanguageManager.getInstance().getString("msg.removedSuccess"));
        } else {
            UIUtil.showErrorAlert(
                    LanguageManager.getInstance().getString("error.removalFailed"),
                    LanguageManager.getInstance().getString("error.operationError"),
                    LanguageManager.getInstance().getString("error.couldNotFindMember"));
        }

        return wasRemoved;
    }
}