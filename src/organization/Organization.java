package organization;

import exceptions.IllegalArgumentsForOrganizationException;
import lib.*;

import java.time.LocalDate;

public record Organization(
        int id,
        String name,
        Coordinates coordinates,
        LocalDate creationDate,
        float annualTurnover,
        String fullName,
        Integer employeesCount,
        OrganizationType type,
        Address postalAddress
) implements PrettyPrintable, WritableToStream, Comparable<Organization> {

    /**
     * @param name           can not be null or empty
     * @param coordinates    can not be null
     * @param annualTurnover can not be below zero
     * @param fullName       can not be null or empty
     * @param employeesCount nullable
     * @param type           nullable
     * @param postalAddress  nullable
     */
    public Organization {
        if (name == null) {
            throw new IllegalArgumentsForOrganizationException("Name must not be null");
        }

        if (name.isEmpty()) {
            throw new IllegalArgumentsForOrganizationException("Name must not be empty");
        }

        if (coordinates == null) {
            throw new IllegalArgumentsForOrganizationException("organization.Coordinates must not be null");
        }

        if (annualTurnover <= 0.0) {
            throw new IllegalArgumentsForOrganizationException("Annual turnover must be above zero");
        }

        if (fullName == null) {
            throw new IllegalArgumentsForOrganizationException("Full name must not be null");
        }

        if (fullName.length() > 573) {
            throw new IllegalArgumentsForOrganizationException("Full name too long, it's length must be within 573 symbols");
        }

        if (employeesCount != null && employeesCount < 0) {
            throw new IllegalArgumentsForOrganizationException("Employees count must not be negative");
        }
    }

    public ImmutablePair<String, OrganizationType> toPairOfFullNameAndType() {
        return new ImmutablePair<>(fullName, type);
    }

    @Override
    public int compareTo(Organization other) {
        return fullName.compareTo(other.fullName);
    }

    @Override
    public PrettyStringBuilder buildPrettyString(PrettyStringBuilder builder) {
        builder.appendLine("Organization:");
        builder.increaseIdent();

        builder.appendLine("id: %d", id);
        builder.appendLine("name: %s", name);
        builder.appendPrettyObject(coordinates);
        builder.appendLine("creationDate: %s", creationDate.toString());
        builder.appendLine("annualTurnover: %s", annualTurnover);
        builder.appendLine("fullName: %s", fullName);
        builder.appendLine("employeesCount: %s", employeesCount);
        builder.appendLine("type: %s", type);

        if (postalAddress == null) {
            builder.appendLine("postalAddress: null");
        } else {
            builder.appendPrettyObject(postalAddress);
        }

        builder.decreaseIdent();

        return builder;
    }

    public static Organization fromStream(StringStream stream) {
        return new Organization(
                Integer.parseInt(stream.read()),
                stream.read(),
                Coordinates.fromStream(stream),
                LocalDate.parse(stream.read()),
                Float.parseFloat(stream.read()),
                stream.read(),
                ConvertToStreamHelper.convertNullableFromStream(
                        stream,
                        (StringStream dataStream) ->
                                Integer.parseInt(dataStream.read())
                ),
                ConvertToStreamHelper.convertNullableFromStream(
                        stream,
                        (StringStream dataStream) ->
                                OrganizationType.valueOf(dataStream.read())
                ),
                Address.fromStream(stream)
        );
    }

    @Override
    public void writeToStream(StringStream stream) {
        stream.writeAny(id);
        stream.write(name);
        coordinates.writeToStream(stream);
        stream.write(creationDate.toString());
        stream.writeAny(annualTurnover);
        stream.write(fullName);

        WritableToStream.writeNullableToStream(stream, employeesCount, stream::writeAny);
        WritableToStream.writeNullableToStream(stream, type, stream::writeAny);

        WritableToStream.writeNullableToStream(stream, postalAddress, (Address address) ->
                address.writeToStream(stream)
        );
    }
}