package organization;

import lib.*;

public record Address(String zipCode, Location town) implements PrettyPrintable, ConvertableToStream {
    /**
     * @param zipCode nullable, must contain at least 3 characters
     * @param town    can not be null
     */
    public Address {
        if (zipCode != null && zipCode.length() < 3) {
            throw new IllegalArgumentException("Invalid zip code");
        }

        if (town == null) {
            throw new IllegalArgumentException("organization.Address town must not be null");
        }
    }

    @Override
    public PrettyStringBuilder buildPrettyString(PrettyStringBuilder builder) {
        builder.appendLine("organization.Address:");
        builder.increaseIdent();

        builder.appendLine("zipCode: %s", zipCode);
        builder.appendPrettyObject(town);

        builder.decreaseIdent();

        return builder;
    }

    public static Address fromStream(StringStream stream) {
        if (stream.getFirst().equals("null") && stream.elementsLeft() == 1) {
            return null;
        }

        return new Address(
                ConvertableFromStream.convertNullableFromStream(stream, Stream::read),
                Location.fromStream(stream)
        );
    }

    @Override
    public void convertToStream(StringStream stream) {
        ConvertableToStream.convertNullableToStream(stream, zipCode, stream::write);
        town.convertToStream(stream);
    }
}