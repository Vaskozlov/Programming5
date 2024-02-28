package organization;

import exceptions.KeyboardInterruptException;
import lib.FunctionWithArgumentAndReturnType;
import lib.Localization;
import lib.BufferedReaderWithQueueOfStreams;

import java.io.IOException;
import java.util.Objects;

/**
 * Reads information needed to construct Organization from the buffer
 */
public class UserInteractiveOrganizationBuilder {
    private final BufferedReaderWithQueueOfStreams reader;
    private final boolean allowBlank;

    public UserInteractiveOrganizationBuilder(BufferedReaderWithQueueOfStreams reader, boolean allowBlank) {
        this.reader = reader;
        this.allowBlank = allowBlank;
    }

    public String getName() throws KeyboardInterruptException, IOException {
        return getString(
                Localization.get("organization_builder.input.organization_name"),
                false
        );
    }

    public Coordinates getCoordinates() throws KeyboardInterruptException, IOException {
        Long x = getNumber(
                Localization.get("organization_builder.input.coordinate.x"),
                false,
                Long::parseLong
        );

        Long y = getNumber(
                Localization.get("organization_builder.input.coordinate.y"),
                false,
                Long::parseLong
        );

        if (y > 464) {
            System.out.println(Localization.get("organization_builder.input.coordinate.y.limit.message"));
            return getCoordinates();
        }

        return new Coordinates(x, y);
    }

    public float getAnnualTurnover() throws KeyboardInterruptException, IOException {
        Float annualTurnover = getNumber(
                Localization.get("organization_builder.input.annual_turnover"),
                false,
                Float::parseFloat
        );

        if (annualTurnover <= 0) {
            System.out.println(Localization.get("organization_builder.input.annual_turnover.limit.message"));
            return getAnnualTurnover();
        }

        return annualTurnover;
    }

    public String getFullName() throws KeyboardInterruptException, IOException {
        return getString(
                Localization.get("organization_builder.input.full_name"),
                false
        );
    }

    public Integer getEmployeesCount() throws KeyboardInterruptException, IOException {
        return getNumber(
                Localization.get("organization_builder.input.employees_count"),
                true,
                Integer::parseInt
        );
    }

    public OrganizationType getOrganizationType() throws KeyboardInterruptException, IOException {
        System.out.printf(Localization.get("organization_builder.input.type"));
        String line = reader.readLine();

        if (line.isEmpty()) {
            return null;
        }

        checkForExitCommand(line);

        return switch (line) {
            case "null" -> null;
            case "0" -> OrganizationType.COMMERCIAL;
            case "1" -> OrganizationType.PUBLIC;
            case "2" -> OrganizationType.PRIVATE_LIMITED_COMPANY;
            case "3" -> OrganizationType.OPEN_JOINT_STOCK_COMPANY;
            default -> {
                System.out.println(Localization.get("organization_builder.input.type.invalid_input"));
                yield getOrganizationType();
            }
        };
    }

    public Address getAddress() throws KeyboardInterruptException, IOException, IllegalArgumentException {
        String zipCode = getString(
                Localization.get("organization_builder.input.zip_code"),
                true
        );

        if (zipCode == null) {
            String answer = getString(
                    Localization.get("organization_builder.input.address.possible_null"),
                    true
            );

            if (answer == null) {
                return null;
            }
        }

        Double x = getNumber(
                Localization.get("organization_builder.input.location.x"),
                false,
                Double::parseDouble
        );

        Float y = getNumber(
                Localization.get("organization_builder.input.location.y"),
                false,
                Float::parseFloat
        );

        Long z = getNumber(
                Localization.get("organization_builder.input.location.z"),
                false,
                Long::parseLong
        );

        String name = getString(
                Localization.get("organization_builder.input.location.name"),
                true
        );

        return new Address(zipCode, new Location(x, y, z, name));
    }

    private String getString(
            String fieldName,
            boolean nullable
    ) throws KeyboardInterruptException, IOException {
        String line = getInput(fieldName, nullable);

        if (line.contains(";")) {
            System.out.println(Localization.get("message.input.error.semicolon"));
            return getString(fieldName, nullable);
        }

        if (needToTakeDataFromProvidedOrganization(line) && allowBlank) {
            return null;
        }

        checkForExitCommand(line);

        if (isNullInput(nullable, line)) {
            return null;
        }

        return line;
    }

    private <T> T getNumber(String fieldName,
                            boolean nullable,
                            FunctionWithArgumentAndReturnType<T, String> function
    ) throws KeyboardInterruptException, IOException {
        String line = getInput(fieldName, nullable);

        if (needToTakeDataFromProvidedOrganization(line) && allowBlank) {
            return null;
        }

        checkForExitCommand(line);

        if (isNullInput(nullable, line)) {
            return null;
        }

        try {
            return function.invoke(line);
        } catch (Exception exception) {
            System.out.println(Localization.get("organization_builder.input.type.invalid_input"));
            return getNumber(fieldName, nullable, function);
        }
    }

    private static boolean isNullInput(boolean nullable, String input) {
        return nullable && (input.isEmpty() || input.equals(Localization.get("input.null")));
    }

    private static boolean needToTakeDataFromProvidedOrganization(String line) {
        return line.isEmpty();
    }

    private String getInput(String fieldName,
                            boolean nullable) throws IOException {
        System.out.printf(
                Localization.get("organization_builder.input.get"),
                fieldName,
                nullable ? Localization.get("input.nullable") : ""
        );
        return reader.readLine();
    }

    private void checkForExitCommand(String line) throws KeyboardInterruptException {
        if (Objects.equals(line, "exit")) {
            throw new KeyboardInterruptException();
        }
    }
}
