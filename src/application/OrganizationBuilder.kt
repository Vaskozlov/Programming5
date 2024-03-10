package application

import database.Organization
import exceptions.KeyboardInterruptException
import lib.BufferedReaderWithQueueOfStreams
import java.io.IOException
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
}
