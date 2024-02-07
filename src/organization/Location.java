package organization;

import lib.*;

public record Location(
        double x,
        float y,
        Long z,
        String name) implements PrettyPrintable, WritableToStream {

    /**
     * @param z    can not be null
     * @param name nullable, length can not be greater than 933
     */
    public Location {
        if (z == null) {
            throw new IllegalArgumentException("organization.Location() z must not be null");
        }

        if (name != null && name.length() > 933) {
            throw new IllegalArgumentException("organization.Location() name is too long! It can not contain more than than 933 symbols.");
        }
    }

    @Override
    public PrettyStringBuilder buildPrettyString(PrettyStringBuilder builder) {
        builder.appendLine("organization.Location:");
        builder.increaseIdent();

        builder.appendLine("x: %f", x);
        builder.appendLine("x: %f", y);
        builder.appendLine("z: %d", z);
        builder.appendLine("name: %s", name);

        builder.decreaseIdent();

        return builder;
    }

    public static Location fromStream(StringStream stream) {
        return new Location(
                Double.parseDouble(stream.read()),
                Float.parseFloat(stream.read()),
                Long.parseLong(stream.read()),
                ConvertToStreamHelper.convertNullableFromStream(stream, Stream::read)
        );
    }

    @Override
    public void writeToStream(StringStream stream) {
        stream.writeAny(x);
        stream.writeAny(y);
        stream.writeAny(z);
        WritableToStream.writeNullableToStream(stream, name, stream::write);
    }
}
