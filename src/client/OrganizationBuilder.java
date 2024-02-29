package client;

import exceptions.KeyboardInterruptException;
import lib.BufferedReaderWithQueueOfStreams;
import organization.Organization;
import organization.UserInteractiveOrganizationBuilder;

import java.io.IOException;
import java.time.LocalDate;

public class OrganizationBuilder {
    public static Organization constructOrganization(BufferedReaderWithQueueOfStreams reader, boolean prototypedFromAnother) throws KeyboardInterruptException, IOException {
        var organizationBuilder = new UserInteractiveOrganizationBuilder(reader, prototypedFromAnother);

        return new Organization(
                null,
                organizationBuilder.getName(),
                organizationBuilder.getCoordinates(),
                LocalDate.now(),
                organizationBuilder.getAnnualTurnover(),
                organizationBuilder.getFullName(),
                organizationBuilder.getEmployeesCount(),
                organizationBuilder.getOrganizationType(),
                organizationBuilder.getAddress()
        );
    }
}
