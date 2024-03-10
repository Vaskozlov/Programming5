package database

import exceptions.IllegalArgumentsForOrganizationException
import lib.CSV.CSVStreamWriter
import lib.PrettyStringBuilder
import lib.WritableToCSVStream
import lib.YamlConvertable
import lib.collections.ImmutablePair
import java.time.LocalDate

data class Organization(
    var id: Int?,
    var name: String?,
    var coordinates: Coordinates?,
    var creationDate: LocalDate?,
    var annualTurnover: Float?,
    var fullName: String?,
    var employeesCount: Int?,
    var type: OrganizationType?,
    var postalAddress: Address?
) : Comparable<Organization>, YamlConvertable, WritableToCSVStream {
    fun validate() {
        val validationResult = checkCorrectness()

        if (!validationResult.isValid) {
            throw IllegalArgumentsForOrganizationException(validationResult.reason)
        }
    }

    fun fillNullFromAnotherOrganization(organization: Organization) {
        id = id ?: organization.id
        name = name ?: organization.name
        coordinates = fillCoordinatesWithMissedInformation(coordinates, organization.coordinates)
        creationDate = creationDate ?: organization.creationDate
        annualTurnover = annualTurnover ?: organization.annualTurnover
        fullName = fullName ?: organization.fullName
        employeesCount = employeesCount ?: organization.employeesCount
        type = type ?: organization.type
        postalAddress = fillAddressWithMissedInformation(postalAddress, organization.postalAddress)
    }

    override fun constructYaml(builder: PrettyStringBuilder): PrettyStringBuilder {
        builder.appendLine("- id: %d", id)
        builder.increaseIdent()

        builder.appendLine("name: %s", name)
        coordinates!!.constructYaml(builder)
        builder.appendLine("creationDate: %s", creationDate.toString())
        builder.appendLine("annualTurnover: %s", annualTurnover)
        builder.appendLine("fullName: %s", fullName)
        builder.appendLine("employeesCount: %s", employeesCount)
        builder.appendLine("type: %s", type)
        postalAddress?.constructYaml(builder) ?: builder.appendLine("postalAddress: null")

        builder.decreaseIdent()

        return builder
    }

    override fun writeToStream(stream: CSVStreamWriter) {
        stream.append(id)
        stream.append(name)
        coordinates!!.writeToStream(stream)
        stream.append(creationDate.toString())
        stream.append(annualTurnover)
        stream.append(fullName)

        lib.writeNullableToStream<Int?>(
            stream,
            employeesCount
        ) { number -> stream.append(number) }

        lib.writeNullableToStream(
            stream,
            type.toString()
        ) { sequence -> stream.append(sequence) }

        lib.writeNullableToStream(
            stream,
            postalAddress
        ) { address: Address ->
            address.writeToStream(stream)
        }
    }

    fun toPairOfFullNameAndType(): ImmutablePair<String?, OrganizationType?> {
        return ImmutablePair(fullName, type)
    }

    class ValidationResult {
        var errorReason: String? = null

        constructor()

        constructor(isValid: Boolean) {
            errorReason = if (isValid) null else "false"
        }

        constructor(errorReason: String?) {
            this.errorReason = errorReason
        }

        val isValid: Boolean
            get() = errorReason == null || errorReason!!.isEmpty()

        val reason: String
            get() {
                if (errorReason == null) {
                    throw RuntimeException()
                }

                return errorReason as String
            }
    }

    override fun compareTo(other: Organization): Int {
        return fullName!!.compareTo(other.fullName!!)
    }

    private fun checkCorrectness(): Organization.ValidationResult {
        if (name!!.isEmpty()) {
            return Organization.ValidationResult("Name must not be empty")
        }

        if (annualTurnover!! <= 0.0) {
            return Organization.ValidationResult("Annual turnover must be above zero")
        }

        if (fullName!!.length > 573) {
            return Organization.ValidationResult("Full name too long, it's length must be within 573 symbols")
        }

        if (employeesCount != null && employeesCount!! < 0) {
            return Organization.ValidationResult("Employees count must not be negative")
        }

        return Organization.ValidationResult()
    }
}