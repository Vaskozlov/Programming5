package application

import exceptions.InvalidOutputFormatException
import exceptions.KeyboardInterruptException
import exceptions.OrganizationAlreadyPresentedException
import lib.Localization
import java.io.FileNotFoundException

object ApplicationErrorMessages {
    fun defaultErrorHandling(error: Exception?): String? {
        if (error is KeyboardInterruptException) {
            return Localization.get("message.collection.add.canceled")
        }

        if (error is IllegalArgumentException) {
            return Localization.get("message.collection.add.failed")
        }

        if (error is OrganizationAlreadyPresentedException) {
            return Localization.get("message.organization.error.already_presented")
        }

        return null
    }

    fun addCommandGetErrorMessage(error: Exception?): String {
        return defaultErrorHandling(error) ?: Localization.get("message.command.failed")
    }

    fun addMaxCommandGetErrorMessage(error: Exception?): String {
        if (error == null) {
            return Localization.get("message.collection.add.max_check_failed")
        }

        return defaultErrorHandling(error) ?: Localization.get("message.command.failed")
    }

    fun modifyOrganizationGetErrorMessage(error: Exception?): String {
        if (error is KeyboardInterruptException) {
            return Localization.get("message.organization.modification_canceled")
        }

        if (error is IllegalArgumentException) {
            return Localization.get("message.organization.modification_error")
        }

        if (error is OrganizationAlreadyPresentedException) {
            return Localization.get("message.organization.error.already_presented_after_modification")
        }

        return Localization.get("message.command.failed")
    }

    fun removeByIdCommandGetErrorMessage(error: Exception?): String {
        if (error is IllegalArgumentException) {
            return Localization.get("message.organization.remove_by_id_error")
        }

        return Localization.get("message.command.failed")
    }

    fun removeHeadCommandGetErrorMessage(error: Exception?): String {
        if (error is IllegalArgumentException) {
            return Localization.get("message.unable_to_remove_organization")
        }

        return Localization.get("message.command.failed")
    }

    fun removeAllByPostalAddressCommandGetErrorMessage(error: Exception?): String {
        if (error is KeyboardInterruptException) {
            return Localization.get("message.collection.remove.canceled")
        }

        return Localization.get("message.command.failed")
    }

    fun executeScriptCommandGetErrorMessage(error: Exception?, filename: String?): String {
        if (error is FileNotFoundException) {
            return String.format(Localization.get("message.file.not_found"), filename)
        }

        return Localization.get("message.command.failed")
    }

    fun showCommandGetErrorMessage(error: Exception?): String? {
        if (error is InvalidOutputFormatException) {
            return Localization.get("message.show.unrecognizable_format")
        }

        return Localization.get("message.command.failed")
    }
}
