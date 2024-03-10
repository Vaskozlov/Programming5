package application

import database.Address
import database.Coordinates
import database.Location
import database.OrganizationType
import exceptions.KeyboardInterruptException
import lib.BufferedReaderWithQueueOfStreams
import lib.Localization

/**
 * Reads information needed to construct Organization from the buffer
 */
class UserInteractiveOrganizationBuilder(
    private val reader: BufferedReaderWithQueueOfStreams,
    private val allowBlank: Boolean
) {
    val name: String?
        get() = getString(
            Localization.get("organization_builder.input.organization_name"),
            false
        )

    val coordinates: Coordinates
        get() {
            val x = getNumber(
                Localization.get("organization_builder.input.coordinate.x"),
                false
            ) { s -> s.toLong() }!!

            val y = getNumber(
                Localization.get("organization_builder.input.coordinate.y"),
                false
            ) { s -> s.toLong() }!!

            if (y > 464) {
                println(Localization.get("organization_builder.input.coordinate.y.limit.message"))
                return coordinates
            }

            return Coordinates(x, y)
        }

    val annualTurnover: Float?
        get() {
            val result = getNumber(
                Localization.get("organization_builder.input.annual_turnover"),
                false
            ) { s -> s.toFloat() }

            if (result != null && result <= 0) {
                println(Localization.get("organization_builder.input.annual_turnover.limit.message"))
                return annualTurnover
            }

            return result
        }

    val fullName: String?
        get() = getString(
            Localization.get("organization_builder.input.full_name"),
            false
        )

    val employeesCount: Int
        get() = getNumber(
            Localization.get("organization_builder.input.employees_count"),
            true
        ) { s -> s.toInt() }!!

    val organizationType: OrganizationType?
        get() {
            System.out.printf(Localization.get("organization_builder.input.type"))
            val line = reader.readLine()

            if (line.isEmpty()) {
                return null
            }

            checkForExitCommand(line)

            return when (line) {
                "null" -> null
                "0" -> OrganizationType.COMMERCIAL
                "1" -> OrganizationType.PUBLIC
                "2" -> OrganizationType.PRIVATE_LIMITED_COMPANY
                "3" -> OrganizationType.OPEN_JOINT_STOCK_COMPANY
                else -> {
                    println(Localization.get("organization_builder.input.type.invalid_input"))
                    return organizationType
                }
            }
        }

    val address: Address?
        get() {
            val zipCode = getString(
                Localization.get("organization_builder.input.zip_code"),
                true
            )

            if (zipCode != null && zipCode.length < 3) {
                println(Localization.get("organization_builder.input.zip_code.limit.message"))
                return address
            }

            if (zipCode == null) {
                val answer = getString(
                    Localization.get("organization_builder.input.address.possible_null"),
                    true
                )

                if (answer == null) {
                    return null
                }
            }

            val x = getNumber(
                Localization.get("organization_builder.input.location.x"),
                false
            ) { s -> s.toDouble() }!!

            val y = getNumber(
                Localization.get("organization_builder.input.location.y"),
                false
            ) { s -> s.toFloat() }!!

            val z = getNumber(
                Localization.get("organization_builder.input.location.z"),
                false
            ) { s -> s.toLong() }!!

            val name = getString(
                Localization.get("organization_builder.input.location.name"),
                true
            )

            return Address(zipCode, Location(x, y, z, name))
        }

    private fun getString(
        fieldName: String,
        nullable: Boolean
    ): String? {
        val line = getInput(fieldName, nullable)

        if (line.contains(";")) {
            println(Localization.get("message.input.error.semicolon"))
            return getString(fieldName, nullable)
        }

        if (needToTakeDataFromProvidedOrganization(line) && allowBlank) {
            return null
        }

        checkForExitCommand(line)

        if (isNullInput(nullable, line)) {
            return null
        }

        return line
    }

    private fun <T> getNumber(
        fieldName: String,
        nullable: Boolean,
        function: (String) -> T
    ): T? {
        val line = getInput(fieldName, nullable)

        if (needToTakeDataFromProvidedOrganization(line) && allowBlank) {
            return null
        }

        checkForExitCommand(line)

        if (isNullInput(nullable, line)) {
            return null
        }

        try {
            return function.invoke(line)
        } catch (exception: Exception) {
            println(Localization.get("organization_builder.input.type.invalid_input"))
            return getNumber(fieldName, nullable, function)
        }
    }

    private fun getInput(
        fieldName: String,
        nullable: Boolean
    ): String {
        System.out.printf(
            Localization.get("organization_builder.input.get"),
            fieldName,
            if (nullable) String.format(" (%s) ", Localization.get("input.nullable")) else ""
        )

        return reader.readLine()
    }

    private fun checkForExitCommand(line: String?) {
        if (line == "exit") {
            throw KeyboardInterruptException()
        }
    }

    private fun isNullInput(nullable: Boolean, input: String): Boolean {
        return nullable && (input.isEmpty() || input == Localization.get("input.null"))
    }

    private fun needToTakeDataFromProvidedOrganization(line: String): Boolean {
        return line.isEmpty()
    }
}
