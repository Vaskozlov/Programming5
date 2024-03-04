package application;

import exceptions.InvalidOutputFormatException;
import exceptions.KeyboardInterruptException;
import exceptions.OrganizationAlreadyPresentedException;
import lib.Localization;

import java.io.FileNotFoundException;

public class ApplicationErrorMessages {
    public static String addCommandGetErrorMessage(Exception error) {
        if (error instanceof KeyboardInterruptException) {
            return Localization.get("message.collection.add.canceled");
        }

        if (error instanceof IllegalArgumentException) {
            return Localization.get("message.collection.add.failed");
        }

        if (error instanceof OrganizationAlreadyPresentedException) {
            return Localization.get("message.organization.error.already_presented");
        }

        return Localization.get("message.command.failed");
    }

    public static String addMaxCommandGetErrorMessage(Exception error) {
        if (error instanceof KeyboardInterruptException) {
            return Localization.get("message.collection.add.canceled");
        }

        if (error instanceof IllegalArgumentException) {
            return Localization.get("message.collection.add.failed");
        }

        if (error instanceof OrganizationAlreadyPresentedException) {
            return Localization.get("message.organization.error.already_presented");
        }

        if (error == null) {
            return Localization.get("message.collection.add.max_check_failed");
        }

        return Localization.get("message.command.failed");

    }

    public static String modifyOrganizationGetErrorMessage(Exception error) {
        if (error instanceof KeyboardInterruptException) {
            return Localization.get("message.organization.modification_canceled");
        }

        if (error instanceof IllegalArgumentException) {
            return Localization.get("message.organization.modification_error");
        }

        if (error instanceof OrganizationAlreadyPresentedException) {
            return Localization.get("message.organization.error.already_presented_after_modification");
        }

        return Localization.get("message.command.failed");
    }

    public static String removeByIdCommandGetErrorMessage(Exception error) {
        if (error instanceof IllegalArgumentException) {
            return Localization.get("message.organization.remove_by_id_error");
        }

        return Localization.get("message.command.failed");
    }

    public static String removeHeadCommandGetErrorMessage(Exception error) {
        if (error instanceof IllegalArgumentException) {
            return Localization.get("message.unable_to_remove_organization");
        }

        return Localization.get("message.command.failed");
    }

    public static String removeAllByPostalAddressCommandGetErrorMessage(Exception error) {
        if (error instanceof KeyboardInterruptException) {
            return Localization.get("message.collection.remove.canceled");
        }

        return Localization.get("message.command.failed");
    }

    public static String executeScriptCommandGetErrorMessage(Exception error, String filename) {
        if (error instanceof FileNotFoundException) {
            return String.format(Localization.get("message.file.not_found"), filename);
        }

        return Localization.get("message.command.failed");
    }

    public static String showCommandGetErrorMessage(Exception error) {
        if (error instanceof InvalidOutputFormatException) {
            return Localization.get("message.show.unrecognizable_format");
        }

        return Localization.get("message.command.failed");
    }
}
