package client

import database.Address
import database.NetworkCode
import database.Organization
import database.OrganizationManagerInterface
import exceptions.*
import lib.ExecutionStatus
import lib.json.fromJson
import network.client.DatabaseCommand
import java.net.InetAddress

class RemoteDatabase(address: InetAddress, port: Int) : OrganizationManagerInterface {
    val commandSender = CommandSender(address, port)

    constructor(address: String, port: Int)
            : this(InetAddress.getByName(address), port)

    private suspend fun sendCommandAndReceiveResult(command: DatabaseCommand, argument: Any?): Result<Any?> {
        commandSender.sendCommand(command, argument)
        val json = commandSender.receiveJson()
        val code = NetworkCode.valueOf(json.getNode("code").asText())

        return when (code) {
            NetworkCode.SUCCESS -> Result.success(commandSender.jsonMapper.fromJson(json.getNode("value")))
            NetworkCode.NOT_SUPPOERTED_COMMAND -> Result.failure(CommandNotExistsException())
            NetworkCode.NOT_A_MAXIMUM_ORGANIZATION -> Result.failure(NotMaximumOrganizationException())
            NetworkCode.ORGANIZATION_ALREADY_EXISTS -> Result.failure(OrganizationAlreadyPresentedException())
            NetworkCode.UNABLE_TO_SAVE_TO_FILE -> Result.failure(FileWriteException())
            NetworkCode.UNABLE_TO_READ_FROM_FILE -> Result.failure(FileReadException())
            NetworkCode.NOT_FOUND -> Result.failure(OrganizationNotFoundException())
            NetworkCode.ORGANIZATION_KEY_ERROR -> Result.failure(OrganizationKeyException())
            NetworkCode.INVALID_OUTPUT_FORMAT -> Result.failure(InvalidOutputFormatException())
            NetworkCode.FAILURE -> Result.failure(Exception())
        }
    }

    override val info: String
        get() = TODO("Not yet implemented")

    override val sumOfAnnualTurnover: Float
        get() = TODO("Not yet implemented")

    override suspend fun maxByFullName(): Organization? {
        val result = sendCommandAndReceiveResult(DatabaseCommand.MAX_BY_FULL_NAME, null)

        if (result.isSuccess) {
            return result.getOrNull() as Organization
        }

        return null
    }

    override suspend fun add(organization: Organization) {
        sendCommandAndReceiveResult(DatabaseCommand.ADD, organization).onFailure { throw it }
    }

    override suspend fun add(vararg newOrganizations: Organization) {
        newOrganizations.forEach { add(it) }
    }

    override suspend fun addIfMax(newOrganization: Organization): ExecutionStatus {
        val result = sendCommandAndReceiveResult(DatabaseCommand.ADD_IF_MAX, newOrganization)

        return if (result.isSuccess) {
            ExecutionStatus.SUCCESS
        } else {
            ExecutionStatus.FAILURE
        }
    }

    override suspend fun modifyOrganization(updatedOrganization: Organization) {
        sendCommandAndReceiveResult(DatabaseCommand.UPDATE, updatedOrganization).onFailure { throw it }
    }

    override suspend fun removeById(id: Int): ExecutionStatus {
        val result = sendCommandAndReceiveResult(DatabaseCommand.REMOVE_BY_ID, id)

        return if (result.isSuccess) {
            ExecutionStatus.SUCCESS
        } else {
            ExecutionStatus.FAILURE
        }
    }

    override suspend fun removeAllByPostalAddress(address: Address) {
        sendCommandAndReceiveResult(DatabaseCommand.REMOVE_ALL_BY_POSTAL_ADDRESS, address).onFailure { throw it }
    }

    override suspend fun removeHead(): Organization? {
        val result = sendCommandAndReceiveResult(DatabaseCommand.REMOVE_HEAD, null)

        if (result.isSuccess) {
            return result.getOrNull() as Organization
        }

        return null
    }

    override suspend fun clear() {
        sendCommandAndReceiveResult(DatabaseCommand.CLEAR, null).onFailure { throw it }
    }

    override suspend fun save(path: String): ExecutionStatus {
        val result = sendCommandAndReceiveResult(DatabaseCommand.SAVE, path)

        return if (result.isSuccess) {
            ExecutionStatus.SUCCESS
        } else {
            ExecutionStatus.FAILURE
        }
    }

    override suspend fun loadFromFile(path: String): ExecutionStatus {
        val result = sendCommandAndReceiveResult(DatabaseCommand.READ, path)

        return if (result.isSuccess) {
            ExecutionStatus.SUCCESS
        } else {
            ExecutionStatus.FAILURE
        }
    }

    override suspend fun toYaml(): String {
        val result = sendCommandAndReceiveResult(DatabaseCommand.SHOW, "yaml")

        return if (result.isSuccess) {
            result.getOrNull() as String
        } else {
            throw Exception()
        }
    }

    override suspend fun toJson(): String {
        val result = sendCommandAndReceiveResult(DatabaseCommand.SHOW, "json")

        return if (result.isSuccess) {
            result.getOrNull() as String
        } else {
            throw Exception()
        }
    }

    override suspend fun toCSV(): String {
        val result = sendCommandAndReceiveResult(DatabaseCommand.SHOW, "csv")

        return if (result.isSuccess) {
            result.getOrNull() as String
        } else {
            throw Exception()
        }
    }
}
