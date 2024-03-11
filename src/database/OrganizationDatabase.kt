package database

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import exceptions.OrganizationAlreadyPresentedException
import kotlinx.coroutines.runBlocking
import lib.*
import lib.CSV.CSVStreamLikeReader
import lib.CSV.CSVStreamWriter
import lib.collections.ImmutablePair
import java.io.*
import java.nio.file.Path
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors
import kotlin.io.path.absolutePathString
import kotlin.math.max

class OrganizationDatabase(path: Path) : OrganizationManagerInterface {
    private var idFactory = IdFactory(1)

    private val initializationDate: LocalDateTime = LocalDateTime.now()
    private val organizations = LinkedList<Organization>()
    private val storedOrganizations = HashSet<ImmutablePair<String?, OrganizationType?>>()

    init {
        runBlocking { loadFromFile(path.absolutePathString()) }
    }

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

    override suspend fun maxByFullName(): Organization? {
        if (organizations.isEmpty()) {
            return null
        }

        return organizations.stream().max(Comparator.comparing { obj: Organization -> obj.fullName!! }).get()
    }

    override val sumOfAnnualTurnover: Double
        get() {
            return organizations.stream()
                .collect(Collectors.summingDouble { organization -> organization.annualTurnover ?: 0.0 })
        }

    override suspend fun add(vararg newOrganizations: Organization) {
        for (organization in newOrganizations) {
            add(organization)
        }
    }

    override suspend fun add(organization: Organization) {
        if (organization.id == null) {
            organization.id = idFactory.nextId
        }

        organization.creationDate = LocalDate.now()

        if (isOrganizationAlreadyPresented(organization)) {
            throw OrganizationAlreadyPresentedException()
        }

        addNoCheck(organization)
    }

    @Throws(OrganizationAlreadyPresentedException::class)
    override suspend fun addIfMax(newOrganization: Organization): ExecutionStatus {
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
    override suspend fun modifyOrganization(updatedOrganization: Organization) {
        for (organization in organizations) {
            if (organization.id == updatedOrganization.id) {
                completeModification(organization, updatedOrganization)
                break
            }
        }
    }

    override suspend fun removeAllByPostalAddress(address: Address) {
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

    override suspend fun removeById(id: Int): ExecutionStatus {
        for (organization in organizations) {
            if (organization.id == id) {
                storedOrganizations.remove(organization.toPairOfFullNameAndType())
                organizations.remove(organization)
                return ExecutionStatus.SUCCESS
            }
        }

        return ExecutionStatus.FAILURE
    }

    override suspend fun removeHead(): Organization? {
        if (organizations.isEmpty()) {
            return null
        }

        val removedOrganization = organizations.removeFirst()
        storedOrganizations.remove(removedOrganization.toPairOfFullNameAndType())

        return removedOrganization
    }

    override suspend fun clear() {
        organizations.clear()
        storedOrganizations.clear()
    }

    private suspend fun tryToLoadFromFile(filename: String): ExecutionStatus {
        clear()

        val fileContent = IOHelper.readFile(filename) ?: return ExecutionStatus.FAILURE

        var maxId = 0
        val reader = CSVStreamLikeReader(fileContent.substring(fileContent.indexOf('\n') + 1))

        while (!reader.isEndOfStream) {
            if (reader.next.isBlank() && reader.elementLeftInLine == 0) {
                reader.readElem()
                continue
            }

            val newOrganization: Organization = organizationFromStream(reader)
            maxId = max(maxId, newOrganization.id!!)
            add(newOrganization)
        }

        idFactory.setValue(maxId + 1)
        return ExecutionStatus.SUCCESS
    }

    override suspend fun loadFromFile(path: String): ExecutionStatus {
        try {
            return tryToLoadFromFile(path)
        } catch (ignored: Exception) {
            println("$ignored.message, $ignored.stackTrace")
        }

        return ExecutionStatus.FAILURE
    }

    override suspend fun save(path: String): ExecutionStatus {
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

    override suspend fun toCSV(): String {
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

    override suspend fun toYaml(): String {
        val result = PrettyStringBuilder(2)
        result.appendLine("Organizations:")
        result.increaseIdent()

        for (organization in organizations) {
            organization.constructYaml(result)
        }

        return result.toString()
    }

    override suspend fun toJson(): String {
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
