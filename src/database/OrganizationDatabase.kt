package database

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import exceptions.OrganizationAlreadyPresentedException
import exceptions.OrganizationNotFoundException
import lib.*
import lib.CSV.CSVStreamLikeReader
import lib.CSV.CSVStreamWriter
import lib.collections.ImmutablePair
import java.io.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.function.Consumer
import kotlin.math.max

class OrganizationDatabase : OrganizationManagerInterface {
    private var idFactory = IdFactory(1)

    private val initializationDate: LocalDateTime = LocalDateTime.now()
    private val organizations = LinkedList<Organization>()
    private val storedOrganizations = HashSet<ImmutablePair<String?, OrganizationType?>>()


    fun getOrganizations(): List<Organization> {
        return organizations
    }

    override val info: String
        get() {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

            return String.format(
                Localization.get("organization.info_message"),
                formatter.format(initializationDate),
                organizations.size
            )
        }

    override fun maxByFullName(): Organization? {
        if (organizations.isEmpty()) {
            return null
        }

        return Collections.max(
            organizations,
            Comparator.comparing { obj: Organization -> obj.fullName!! }
        )
    }

    override val sumOfAnnualTurnover: Float
        get() {
            var result = 0.0f

            for (organization in organizations) {
                result += organization.annualTurnover!!
            }

            return result
        }

    @Throws(OrganizationAlreadyPresentedException::class)
    override fun add(vararg newOrganizations: Organization) {
        for (organization in newOrganizations) {
            add(organization)
        }
    }

    @Throws(OrganizationAlreadyPresentedException::class)
    override fun add(organization: Organization) {
        if (organization.id == null) {
            organization.id = idFactory.nextId
        }

        if (isOrganizationAlreadyPresented(organization)) {
            throw OrganizationAlreadyPresentedException()
        }

        addNoCheck(organization)
    }

    @Throws(OrganizationAlreadyPresentedException::class)
    override fun addIfMax(newOrganization: Organization): ExecutionStatus {
        val maxOrganization = Collections.max(
            organizations,
            Comparator.comparing { obj: Organization -> obj.fullName!! }
        )

        if (maxOrganization.fullName!!.compareTo(newOrganization.fullName!!) < 0) {
            add(newOrganization)
            return ExecutionStatus.SUCCESS
        }

        return ExecutionStatus.FAILURE
    }

    private fun addNoCheck(organization: Organization) {
        organizations.add(organization)
        storedOrganizations.add(organization.toPairOfFullNameAndType())
        Collections.sort(organizations, Comparator.comparing { obj: Organization -> obj.fullName!! })
    }

    @Throws(OrganizationAlreadyPresentedException::class)
    override fun modifyOrganization(updatedOrganization: Organization) {
        for (organization in organizations) {
            if (organization.id == updatedOrganization.id) {
                completeModification(organization, updatedOrganization)
                break
            }
        }
    }

    override fun removeAllByPostalAddress(address: Address) {
        val toRemove: MutableList<Organization> = ArrayList()
        val pairsToRemove: MutableList<ImmutablePair<String?, OrganizationType?>> = ArrayList()

        for (organization in organizations) {
            if (organization.postalAddress == address) {
                toRemove.add(organization)
                pairsToRemove.add(organization.toPairOfFullNameAndType())
            }
        }

        organizations.removeAll(toRemove)
        pairsToRemove.forEach(Consumer { o: ImmutablePair<String?, OrganizationType?> -> storedOrganizations.remove(o) })
    }

    override fun removeById(id: Int): ExecutionStatus {
        for (organization in organizations) {
            if (organization.id == id) {
                storedOrganizations.remove(organization.toPairOfFullNameAndType())
                organizations.remove(organization)
                return ExecutionStatus.SUCCESS
            }
        }

        return ExecutionStatus.FAILURE
    }

    override fun removeHead(): Organization? {
        if (organizations.isEmpty()) {
            return null
        }

        val removedOrganization = organizations.removeFirst()
        storedOrganizations.remove(removedOrganization.toPairOfFullNameAndType())

        return removedOrganization
    }

    override fun clear() {
        organizations.clear()
        storedOrganizations.clear()
    }

    @Throws(OrganizationAlreadyPresentedException::class, IOException::class)
    private fun tryToLoadFromFile(filename: String): ExecutionStatus {
        clear()

        val fileContent = IOHelper.readFile(filename) ?: return ExecutionStatus.FAILURE

        var maxId = 0
        val reader = CSVStreamLikeReader(fileContent.substring(fileContent.indexOf('\n') + 1))

        while (!reader.isEndOfStream) {
            val newOrganization: Organization = organizationFromStream(reader)
            maxId = max(maxId.toDouble(), newOrganization.id!!.toDouble()).toInt()
            add(newOrganization)
        }

        idFactory = IdFactory(maxId + 1)
        return ExecutionStatus.SUCCESS
    }

    override fun loadFromFile(path: String): ExecutionStatus {
        try {
            return tryToLoadFromFile(path)
        } catch (ignored: Exception) {
        }

        return ExecutionStatus.FAILURE
    }

    override fun saveToFile(path: String): ExecutionStatus {
        try {
            FileWriter(path).use { file ->
                file.write(toCSV())
                file.flush()
                return ExecutionStatus.SUCCESS
            }
        } catch (exception: IOException) {
            return ExecutionStatus.FAILURE
        }
    }

    override fun toCSV(): String {
        val stream = CSVStreamWriter(StringWriter())
        try {
            stream.append(CSVHeader.headerAsString)
            stream.newLine()

            for (organization in organizations) {
                organization.writeToStream(stream)
                stream.newLine()
            }

            return stream.writer.toString()
        } catch (exception: IOException) {
            // This should never happen
            return ""
        }
    }

    override fun toYaml(): String {
        val result = PrettyStringBuilder(2)
        result.appendLine("Organizations:")
        result.increaseIdent()

        for (organization in organizations) {
            organization.constructYaml(result)
        }

        return result.toString()
    }

    override fun toJson(): String {
        try {
            val yaml = toYaml()
            val yamlReader = ObjectMapper(YAMLFactory())
            val obj = yamlReader.readValue(yaml, Any::class.java)
            val jsonWriter = ObjectMapper()

            return jsonWriter.writerWithDefaultPrettyPrinter().writeValueAsString(obj)
        } catch (error: JsonProcessingException) {
            return "Unable to generate json file, because yaml version has mistakes"
        }
    }

    @Throws(OrganizationAlreadyPresentedException::class)
    private fun completeModification(organization: Organization, updatedOrganization: Organization) {
        updatedOrganization.fillNullFromAnotherOrganization(organization)

        if (!isModificationLegal(organization, updatedOrganization)) {
            throw OrganizationAlreadyPresentedException()
        }

        addNoCheck(updatedOrganization)
        organizations.remove(organization)
    }

    private fun isModificationLegal(previous: Organization, newVersion: Organization): Boolean {
        if (previous.fullName != newVersion.fullName || previous.type != newVersion.type) {
            return !isOrganizationAlreadyPresented(newVersion)
        }

        return true
    }

    private fun isOrganizationAlreadyPresented(organization: Organization): Boolean {
        return storedOrganizations.contains(organization.toPairOfFullNameAndType())
    }
}
