package database

import lib.ExecutionStatus

interface OrganizationManagerInterface {
    val info: String

    val sumOfAnnualTurnover: Float

    suspend fun maxByFullName(): Organization?

    suspend fun add(organization: Organization)

    suspend fun add(vararg newOrganizations: Organization)

    suspend fun addIfMax(newOrganization: Organization): ExecutionStatus

    suspend fun modifyOrganization(updatedOrganization: Organization)

    suspend fun removeById(id: Int) : ExecutionStatus

    suspend fun removeAllByPostalAddress(address: Address)

    suspend fun removeHead(): Organization?

    suspend fun clear()

    suspend fun save(path: String): ExecutionStatus

    suspend fun loadFromFile(path: String): ExecutionStatus

    suspend fun toYaml(): String

    suspend fun toJson(): String

    suspend fun toCSV(): String
}
