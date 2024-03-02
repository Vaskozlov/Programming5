package organization;

import lib.CSV.CSVStreamLikeReader;
import lib.CSV.CSVStreamWriter;
import lib.ConvertToStreamHelper;
import lib.PrettyStringBuilder;
import lib.WritableToCSVStream;
import lib.YamlConvertable;

import java.io.IOException;

/**
 * @param zipCode nullable, must contain at least 3 character
 * @param town    can not be null
 */
public record Address(String zipCode, Location town) implements YamlConvertable, WritableToCSVStream {
    public void validate() throws IllegalArgumentException {
        if (zipCode != null && zipCode.length() < 3) {
            throw new IllegalArgumentException("Invalid zip code");
        }

        if (town == null) {
            throw new IllegalArgumentException("organization.Address town must not be null");
        }
    }

    public static Address joinAddresses(Address first, Address second) {
        if (first == null) {
            return second;
        }

        return new Address(
                first.zipCode() == null ? second.zipCode() : first.zipCode(),
                Location.joinLocations(first.town(), second.town())
        );
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

    public static Address fromStream(CSVStreamLikeReader stream) throws IOException {
        if (stream.getNext().equals("null") && stream.getElementLeftInLine() == 1) {
            stream.readElem();
            return null;
        }

        return new Address(
                ConvertToStreamHelper.convertNullableFromStream(stream, CSVStreamLikeReader::readElem),
                Location.fromStream(stream)
        );
    }

    @Override
    public void writeToStream(CSVStreamWriter stream) throws IOException {
        WritableToCSVStream.writeNullableToStream(stream, zipCode, stream::append);
        town.writeToStream(stream);
    }
}