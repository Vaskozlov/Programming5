package OrganizationDatabase;

import exceptions.OrganizationAlreadyPresentedException;
import exceptions.OrganizationNotFoundException;
import lib.ExecutionStatus;

public interface OrganizationManagerInterface {
    String getInfo();

    float getSumOfAnnualTurnover();

    Organization maxByFullName() throws OrganizationNotFoundException;

    void add(Organization organization) throws OrganizationAlreadyPresentedException;

    void add(Organization... newOrganizations) throws OrganizationAlreadyPresentedException;

    ExecutionStatus addIfMax(Organization newOrganization) throws OrganizationAlreadyPresentedException;

    void modifyOrganization(Organization updatedOrganization) throws OrganizationAlreadyPresentedException;

    void removeById(int id) throws OrganizationNotFoundException;

    void removeAllByPostalAddress(Address address);

    Organization removeHead();

    void clear();

    ExecutionStatus saveToFile(String path);

    ExecutionStatus loadFromFile(String path);

    String toYaml();

    String toJson();

    String toCSV();
}
