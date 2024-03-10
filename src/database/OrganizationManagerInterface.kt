package database

import lib.ExecutionStatus

interface OrganizationManagerInterface {
    val info: String

    val sumOfAnnualTurnover: Float

    fun maxByFullName(): Organization?

    fun add(organization: Organization)

    fun add(vararg newOrganizations: Organization)

    fun addIfMax(newOrganization: Organization): ExecutionStatus

    fun modifyOrganization(updatedOrganization: Organization)

    fun removeById(id: Int) : ExecutionStatus

    fun removeAllByPostalAddress(address: Address)

    fun removeHead(): Organization?

    fun clear()

    fun saveToFile(path: String): ExecutionStatus

    fun loadFromFile(path: String): ExecutionStatus

    fun toYaml(): String

    fun toJson(): String

    fun toCSV(): String
}
