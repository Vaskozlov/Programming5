package client;

import lib.collections.ImmutablePair;
import organization.Address;
import organization.Coordinates;
import organization.OrganizationType;

import java.time.LocalDate;

public class OrganizationPrototype implements Comparable<OrganizationPrototype> {
    protected Integer id;
    protected String name;
    protected Coordinates coordinates;
    protected LocalDate creationDate;
    protected Float annualTurnover;
    protected String fullName;
    protected Integer employeesCount;
    protected OrganizationType type;
    protected Address postalAddress;

    public OrganizationPrototype(
            Integer id,
            String name,
            Coordinates coordinates,
            LocalDate creationDate,
            Float annualTurnover,
            String fullName,
            Integer employeesCount,
            OrganizationType type,
            Address postalAddress) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.annualTurnover = annualTurnover;
        this.fullName = fullName;
        this.employeesCount = employeesCount;
        this.type = type;
        this.postalAddress = postalAddress;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Float getAnnualTurnover() {
        return annualTurnover;
    }

    public String getFullName() {
        return fullName;
    }

    public Integer getEmployeesCount() {
        return employeesCount;
    }

    public OrganizationType getType() {
        return type;
    }

    public Address getPostalAddress() {
        return postalAddress;
    }

    public ImmutablePair<String, OrganizationType> toPairOfFullNameAndType() {
        return new ImmutablePair<>(fullName, type);
    }

    public static class ValidationResult {
        String errorReason = null;

        public ValidationResult() {

        }

        public ValidationResult(boolean isValid) {
            errorReason = isValid ? null : "false";
        }

        public ValidationResult(String errorReason) {
            this.errorReason = errorReason;
        }

        public boolean isValid() {
            return errorReason == null || errorReason.isEmpty();
        }

        public String getReason() {
            if (errorReason == null) {
                throw new RuntimeException();
            }

            return errorReason;
        }
    }

    @Override
    public int compareTo(OrganizationPrototype other) {
        return fullName.compareTo(other.fullName);
    }

    public ValidationResult checkCorrectness() {
        if (id == null || annualTurnover == null) {
            return new ValidationResult("id and annual turnover can not be null");
        }

        if (name == null) {
            return new ValidationResult("Name must not be null");
        }

        if (name.isEmpty()) {
            return new ValidationResult("Name must not be empty");
        }

        if (coordinates == null) {
            return new ValidationResult("organization.Coordinates must not be null");
        }

        if (annualTurnover <= 0.0) {
            return new ValidationResult("Annual turnover must be above zero");
        }

        if (fullName == null) {
            return new ValidationResult("Full name must not be null");
        }

        if (fullName.length() > 573) {
            return new ValidationResult("Full name too long, it's length must be within 573 symbols");
        }

        if (employeesCount != null && employeesCount < 0) {
            return new ValidationResult("Employees count must not be negative");
        }

        return new ValidationResult();
    }
}
