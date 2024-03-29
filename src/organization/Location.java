package organization;

import lib.CSV.CSVStreamLikeReader;
import lib.CSV.CSVStreamWriter;
import lib.ConvertToStreamHelper;
import lib.PrettyStringBuilder;
import lib.WritableToCSVStream;
import lib.YamlConvertable;

import java.io.IOException;

/**
 * @param z    can not be null
 * @param name nullable, length can not be greater than 933
 */
public record Location(
        Double x,
        Float y,
        Long z,
        String name) implements YamlConvertable, WritableToCSVStream {

    public Location {
        if (z == null) {
            throw new IllegalArgumentException("organization.Location() z must not be null");
        }

        if (name != null && name.length() > 933) {
            throw new IllegalArgumentException("organization.Location() name is too long! It can not contain more than than 933 symbols.");
        }
    }

    public static Location joinLocations(Location first, Location second) {
        if (first == null) {
            return second;
        }

        return new Location(
                first.x() == null ? second.x() : first.x(),
                first.y() == null ? second.y() : first.y(),
                first.z() == null ? second.z() : first.z(),
                first.name() == null ? second.name() : first.name()
        );
    }

    @Override
    public PrettyStringBuilder constructYaml(PrettyStringBuilder builder) {
        builder.appendLine("organization.Location:");
        builder.increaseIdent();

        builder.appendLine("x: %f", x);
        builder.appendLine("y: %f", y);
        builder.appendLine("z: %d", z);
        builder.appendLine("name: %s", name);

        builder.decreaseIdent();

        return builder;
    }

    public static Location fromStream(CSVStreamLikeReader stream) throws IOException {
        return new Location(
                Double.parseDouble(stream.readElem()),
                Float.parseFloat(stream.readElem()),
                Long.parseLong(stream.readElem()),
                ConvertToStreamHelper.convertNullableFromStream(stream, CSVStreamLikeReader::readElem)
        );
    }

    @Override
    public void writeToStream(CSVStreamWriter stream) throws IOException {
        stream.append(x);
        stream.append(y);
        stream.append(z);
        WritableToCSVStream.writeNullableToStream(stream, name, stream::append);
    }
}
