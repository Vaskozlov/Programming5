package organization;

import lib.*;

/**
 * @param zipCode nullable, must contain at least 3 character
 * @param town    can not be null
 */
public record Address(String zipCode, Location town) implements YamlConvertable, WritableToStream {
    public Address {
        if (zipCode != null && zipCode.length() < 3) {
            throw new IllegalArgumentException("Invalid zip code");
        }

        if (town == null) {
            throw new IllegalArgumentException("organization.Address town must not be null");
        }
    }

    @Override
    public PrettyStringBuilder constructYaml(PrettyStringBuilder builder) {
        builder.appendLine("organization.Address:");
        builder.increaseIdent();

        builder.appendLine("zipCode: %s", zipCode);
        town.constructYaml(builder);

        builder.decreaseIdent();

        return builder;
    }

    public static Address fromStream(StringStream stream) {
        if (stream.getFirst().equals("null") && stream.elementsLeft() == 1) {
            return null;
        }

        return new Address(
                ConvertToStreamHelper.convertNullableFromStream(stream, Stream::read),
                Location.fromStream(stream)
        );
    }

    @Override
    public void writeToStream(StringStream stream) {
        WritableToStream.writeNullableToStream(stream, zipCode, stream::write);
        town.writeToStream(stream);
    }
}