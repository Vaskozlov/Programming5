package organization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import exceptions.OrganizationAlreadyPresentedException;
import exceptions.OrganizationNotFoundException;
import lib.CSV.CSVStreamLikeReader;
import lib.CSV.CSVStreamWriter;
import lib.IOHelper;
import lib.IdFactory;
import lib.Localization;
import lib.PrettyStringBuilder;
import lib.collections.ImmutablePair;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrganizationManager {
    private static final String CSVHeader = "id;name;coordinates.x;coordinates.y;creation date;annual turnover;full name;employees count;type;postalAddress:zipCode;postalAddress.organization.Location.x;postalAddress.organization.Location.y;postalAddress.organization.Location.z;postalAddress.organization.Location.name";

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
            result += organization.getAnnualTurnover();
        }

        return result;
    }

    public void add(Organization... newOrganizations) throws OrganizationAlreadyPresentedException {
        for (Organization organization : newOrganizations) {
            add(organization);
        }
    }

    public void add(Organization organization) throws OrganizationAlreadyPresentedException {
        if (organization.getId() == null) {
            organization.setId(idFactory.getNextId());
        }

        if (isOrganizationAlreadyPresented(organization)) {
            throw new OrganizationAlreadyPresentedException();
        }

        addNoCheck(organization);
    }

    private void addNoCheck(Organization organization) {
        organizations.add(organization);
        storedOrganizations.add(organization.toPairOfFullNameAndType());
        Collections.sort(organizations);
    }

    public void modifyOrganization(int organizationId, Organization updatedOrganization)
            throws OrganizationAlreadyPresentedException {
        for (Organization organization : organizations) {
            if (organization.getId() == organizationId) {
                completeModification(organization, updatedOrganization);
                break;
            }
        }
    }

    public void removeAllByPostalAddress(Address address) {
        List<Organization> toRemove = new ArrayList<>();
        List<ImmutablePair<String, OrganizationType>> pairsToRemove = new ArrayList<>();

        for (Organization organization : organizations) {
            if (organization.getPostalAddress().equals(address)) {
                toRemove.add(organization);
                pairsToRemove.add(organization.toPairOfFullNameAndType());
            }
        }

        organizations.removeAll(toRemove);
        pairsToRemove.forEach(storedOrganizations::remove);
    }

    public void removeOrganization(int id) throws OrganizationNotFoundException {
        for (Organization organization : organizations) {
            if (organization.getId() == id) {
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

    private boolean tryToLoadFromFile(String filename) throws OrganizationAlreadyPresentedException, IOException {
        clear();

        String fileContent = IOHelper.readFile(filename);

        if (fileContent == null) {
            return false;
        }

        int maxId = 0;
        CSVStreamLikeReader reader = new CSVStreamLikeReader(fileContent.substring(fileContent.indexOf('\n') + 1));

        while (!reader.isEndOfStream()) {
            Organization newOrganization = Organization.fromStream(reader);
            maxId = Math.max(maxId, newOrganization.getId());
            add(newOrganization);
        }

        idFactory = new IdFactory(maxId + 1);
        return true;
    }

    public boolean loadFromFile(String filename) {
        try {
            return tryToLoadFromFile(filename);
        } catch (Exception ignored) {
        }

        return false;
    }

    public boolean saveToFile(String filename) {
        try (FileWriter file = new FileWriter(filename)) {
            file.write(toCSV());
            file.flush();
            return true;
        } catch (IOException exception) {
            return false;
        }
    }

    public String toCSV() throws IOException {
        CSVStreamWriter stream = new CSVStreamWriter(new StringWriter());
        stream.append(CSVHeader);
        stream.newLine();

        for (Organization organization : organizations) {
            organization.writeToStream(stream);
            stream.newLine();
        }

        return stream.getWriter().toString();
    }

    public String toYaml() {
        PrettyStringBuilder result = new PrettyStringBuilder(2);
        result.appendLine("Organizations:");
        result.increaseIdent();

        for (Organization organization : organizations) {
            organization.constructYaml(result);
        }

        return result.toString();
    }

    public String toJson() {
        try {
            String yaml = toYaml();
            ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
            Object obj = yamlReader.readValue(yaml, Object.class);
            ObjectMapper jsonWriter = new ObjectMapper();

            return jsonWriter.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException error) {
            return "Unable to generate json file, because yaml version has mistakes";
        }
    }

    private void completeModification(Organization organization, Organization updatedOrganization)
            throws OrganizationAlreadyPresentedException {
        updatedOrganization.fillNullFromAnotherOrganization(organization);

        if (!isModificationLegal(organization, updatedOrganization)) {
            throw new OrganizationAlreadyPresentedException();
        }

        addNoCheck(updatedOrganization);
        organizations.remove(organization);
    }

    private boolean isModificationLegal(Organization previous, Organization newVersion) {
        if (!previous.getFullName().equals(newVersion.getFullName()) || !previous.getType().equals(newVersion.getType())) {
            return !isOrganizationAlreadyPresented(newVersion);
        }

        return true;
    }

    private boolean isOrganizationAlreadyPresented(Organization organization) {
        return storedOrganizations.contains(organization.toPairOfFullNameAndType());
    }
}
