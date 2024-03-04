package OrganizationDatabase;

public class CSVHeader {
    public static final String[] header = {
            "id",
            "name",
            "coordinates.x",
            "coordinates.y",
            "creation date",
            "annual turnover",
            "full name",
            "employees count",
            "type",
            "postalAddress:zipCode",
            "postalAddress.OrganizationDatabase.Location.x",
            "postalAddress.OrganizationDatabase.Location.y",
            "postalAddress.OrganizationDatabase.Location.z",
            "postalAddress.OrganizationDatabase.Location.name",
    };

    public static final String headerAsString = String.join(";", header);
}
