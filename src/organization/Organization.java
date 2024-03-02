package organization;

import client.OrganizationPrototype;
import exceptions.IllegalArgumentsForOrganizationException;
import lib.CSV.CSVStreamLikeReader;
import lib.CSV.CSVStreamWriter;
import lib.ConvertToStreamHelper;
import lib.PrettyStringBuilder;
import lib.WritableToCSVStream;
import lib.YamlConvertable;

import java.io.IOException;
import java.time.LocalDate;

public class Organization extends OrganizationPrototype implements YamlConvertable, WritableToCSVStream {
    public Organization(
            Integer id,
            String name,
            Coordinates coordinates,
            LocalDate creationDate,
            Float annualTurnover,
            String fullName,
            Integer employeesCount,
            OrganizationType type,
            Address postalAddress
    ) {
        super(id, name, coordinates, creationDate, annualTurnover, fullName, employeesCount, type, postalAddress);
    }

    public void setId(int newId) {
        id = newId;
    }

    public void validate() {
        ValidationResult validationResult = checkCorrectness();

        if (!validationResult.isValid()) {
            throw new IllegalArgumentsForOrganizationException(validationResult.getReason());
        }
    }

    public void fillNullFromAnotherOrganization(OrganizationPrototype organization) {
        id = id == null ? organization.getId() : id;
        name = name == null ? organization.getName() : name;
        coordinates = Coordinates.joinCoordinates(coordinates, organization.getCoordinates());
        creationDate = creationDate == null ? organization.getCreationDate() : creationDate;
        annualTurnover = annualTurnover == null ? organization.getAnnualTurnover() : annualTurnover;
        fullName = fullName == null ? organization.getFullName() : fullName;
        employeesCount = employeesCount == null ? organization.getEmployeesCount() : employeesCount;
        type = type == null ? organization.getType() : type;
        postalAddress = Address.joinAddresses(postalAddress, organization.getPostalAddress());
    }

    @Override
    public PrettyStringBuilder constructYaml(PrettyStringBuilder builder) {
        builder.appendLine("- id: %d", id);
        builder.increaseIdent();

        builder.appendLine("name: %s", name);
        getCoordinates().constructYaml(builder);
        builder.appendLine("creationDate: %s", creationDate.toString());
        builder.appendLine("annualTurnover: %s", annualTurnover);
        builder.appendLine("fullName: %s", fullName);
        builder.appendLine("employeesCount: %s", employeesCount);
        builder.appendLine("type: %s", type);

        if (postalAddress == null) {
            builder.appendLine("postalAddress: null");
        } else {
            postalAddress.constructYaml(builder);
        }

        builder.decreaseIdent();

        return builder;
    }

    public static Organization fromStream(CSVStreamLikeReader stream) throws IOException {
        return new Organization(
                Integer.parseInt(stream.readElem()),
                stream.readElem(),
                Coordinates.fromStream(stream),
                LocalDate.parse(stream.readElem()),
                Float.parseFloat(stream.readElem()),
                stream.readElem(),
                ConvertToStreamHelper.convertNullableFromStream(
                        stream,
                        (CSVStreamLikeReader dataStream) ->
                                Integer.parseInt(dataStream.readElem())
                ),
                ConvertToStreamHelper.convertNullableFromStream(
                        stream,
                        (CSVStreamLikeReader dataStream) ->
                                OrganizationType.valueOf(dataStream.readElem())
                ),
                Address.fromStream(stream)
        );
    }

    @Override
    public void writeToStream(CSVStreamWriter stream) throws IOException {
        stream.append(id);
        stream.append(name);
        coordinates.writeToStream(stream);
        stream.append(creationDate.toString());
        stream.append(annualTurnover);
        stream.append(fullName);

        WritableToCSVStream.writeNullableToStream(stream, getEmployeesCount(), stream::append);
        WritableToCSVStream.writeNullableToStream(stream, getType().toString(), stream::append);

        WritableToCSVStream.writeNullableToStream(stream, getPostalAddress(), (Address address) ->
                address.writeToStream(stream)
        );
    }
}