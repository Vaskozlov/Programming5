package application

import database.Address
import database.Organization
import lib.BufferedReaderWithQueueOfStreams
import java.time.LocalDate

object OrganizationBuilder {
    fun constructOrganization(reader: BufferedReaderWithQueueOfStreams, prototypedFromAnother: Boolean): Organization {
        val organizationBuilder = UserInteractiveOrganizationBuilder(reader, prototypedFromAnother)

        return Organization(
            null,
            organizationBuilder.name,
            organizationBuilder.coordinates,
            LocalDate.now(),
            organizationBuilder.annualTurnover,
            organizationBuilder.fullName,
            organizationBuilder.employeesCount,
            organizationBuilder.organizationType,
            organizationBuilder.address
        )
    }

    fun constructAddress(reader: BufferedReaderWithQueueOfStreams, prototypedFromAnother: Boolean): Address? {
        val organizationBuilder = UserInteractiveOrganizationBuilder(reader, prototypedFromAnother)
        return organizationBuilder.address
    }
}
