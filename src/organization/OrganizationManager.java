package organization;

import lib.*;
import exceptions.*;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrganizationManager {
    private static final String CSVHeader = "id;name;coordinates.x;coordinates.y;creation date;annual turnover;full name;employees count;type;postalAddress:zipCode;postalAddress.organization.Location.x;postalAddress.organization.Location.y;postalAddress.organization.Location.z;postalAddress.organization.Location.name\n";

    private IdFactory idFactory = new IdFactory(1);
    private final LinkedList<Organization> organizations = new LinkedList<>();
    private final HashSet<ImmutablePair<String, OrganizationType>> storedOrganizations = new HashSet<>();
    private final java.time.LocalDateTime initializationDate = java.time.LocalDateTime.now();

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public String getInfo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return String.format(
                Localization.get("organization.info_message"),
                formatter.format(initializationDate),
                organizations.size()
        );
    }

    public float getSumOfAnnualTurnover() {
        float result = 0.0f;

        for (Organization organization : organizations) {
            result += organization.annualTurnover();
        }

        return result;
    }

    public Organization constructOrganization(BufferedReaderWithQueueOfStreams reader) throws KeyboardInterruptException, IOException {
        return constructOrganization(reader, null);
    }

    public Organization constructOrganization(BufferedReaderWithQueueOfStreams reader, Organization defaultValues) throws KeyboardInterruptException, IOException {
        int id = defaultValues == null ? idFactory.generateId() : defaultValues.id();
        var organizationBuilder = new UserInteractiveOrganizationBuilder(reader, defaultValues);

        return new Organization(
                id,
                organizationBuilder.getName(),
                organizationBuilder.getCoordinates(),
                LocalDate.now(),
                organizationBuilder.getAnnualTurnover(),
                organizationBuilder.getFullName(),
                organizationBuilder.getEmployeesCount(),
                organizationBuilder.getOrganizationType(),
                organizationBuilder.getAddress()
        );
    }

    public void add(Organization... newOrganizations) throws OrganizationAlreadyPresentedException {
        for (Organization organization : newOrganizations) {
            add(organization);
        }
    }

    public void add(Organization organization) throws OrganizationAlreadyPresentedException {
        if (isOrganizationAlreadyPresented(organization)) {
            throw new OrganizationAlreadyPresentedException();
        }

        organizations.add(organization);
        storedOrganizations.add(organization.toPairOfFullNameAndType());
        Collections.sort(organizations);
    }

    public void modifyOrganization(int organizationId, BufferedReaderWithQueueOfStreams reader)
            throws KeyboardInterruptException, IOException, OrganizationAlreadyPresentedException {
        for (Organization organization : organizations) {
            if (organization.id() == organizationId) {
                completeModification(organization, reader);
                break;
            }
        }
    }

    public void removeAllByPostalAddress(BufferedReaderWithQueueOfStreams reader) throws KeyboardInterruptException, IOException {
        var organizationBuilder = new UserInteractiveOrganizationBuilder(reader);
        Address address = organizationBuilder.getAddress();
        removeAllByPostalAddress(address);
    }

    public void removeAllByPostalAddress(Address address) {
        List<Organization> toRemove = new ArrayList<>();
        List<ImmutablePair<String, OrganizationType>> pairsToRemove = new ArrayList<>();

        for (Organization organization : organizations) {
            if (organization.postalAddress().equals(address)) {
                toRemove.add(organization);
                pairsToRemove.add(organization.toPairOfFullNameAndType());
            }
        }

        organizations.removeAll(toRemove);
        pairsToRemove.forEach(storedOrganizations::remove);
    }

    public void removeOrganization(int id) throws OrganizationNotFoundException {
        for (Organization organization : organizations) {
            if (organization.id() == id) {
                storedOrganizations.remove(organization.toPairOfFullNameAndType());
                organizations.remove(organization);
                return;
            }
        }

        throw new OrganizationNotFoundException(String.format("organization.Organization with id %d not found", id));
    }

    public Organization removeHead() {
        if (organizations.isEmpty()) {
            return null;
        }

        var removedOrganization = organizations.removeFirst();
        storedOrganizations.remove(removedOrganization.toPairOfFullNameAndType());

        return removedOrganization;
    }

    public void clear() {
        organizations.clear();
        storedOrganizations.clear();
    }

    public boolean loadFromFile(String filename) throws OrganizationAlreadyPresentedException {
        clear();

        String fileContent = IOHelper.readFile(filename);

        if (fileContent == null) {
            return false;
        }

        int maxId = 0;
        boolean initialized = false;
        String[] lines = fileContent.split("\n");

        for (String line : lines) {
            if (!initialized) {
                initialized = true;
                continue;
            }

            StringStream stream = new StringStream(Arrays.asList(line.split(";")));
            Organization newOrganization = Organization.fromStream(stream);
            maxId = Math.max(maxId, newOrganization.id());

            add(newOrganization);
        }

        idFactory = new IdFactory(maxId + 1);
        return true;
    }

    public boolean saveToFile(String filename) {
        try (FileWriter file = new FileWriter(filename)) {
            file.write(CSVHeader);
            for (Organization organization : organizations) {
                StringStream stream = new StringStream();
                organization.convertToStream(stream);
                file.write(String.join(";", stream));
                file.append('\n');
            }

            return true;
        } catch (IOException exception) {
            return false;
        }
    }

    public String toPrettyString() {
        StringBuilder result = new StringBuilder();

        for (Organization organization : organizations) {
            result.append(organization.toPrettyString()).append("\n");
        }

        return result.toString();
    }

    private void completeModification(Organization organization, BufferedReaderWithQueueOfStreams reader)
            throws KeyboardInterruptException, IOException, OrganizationAlreadyPresentedException {
        Organization updatedOrganization = constructOrganization(reader, organization);

        if (!isModificationLegal(organization, updatedOrganization)) {
            throw new OrganizationAlreadyPresentedException();
        }

        add(constructOrganization(reader, organization));
        organizations.remove(organization);
    }

    private boolean isModificationLegal(Organization previous, Organization newVersion) {
        if (!previous.fullName().equals(newVersion.fullName()) || !previous.type().equals(newVersion.type())) {
            return !isOrganizationAlreadyPresented(newVersion);
        }

        return true;
    }

    private boolean isOrganizationAlreadyPresented(Organization organization) {
        return storedOrganizations.contains(organization.toPairOfFullNameAndType());
    }
}
