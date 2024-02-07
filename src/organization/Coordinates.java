package organization;

import lib.*;

public record Coordinates(long x, long y) implements PrettyPrintable, WritableToStream {

    public Coordinates {
        if (y > 464) {
            throw new IllegalArgumentException("Coordinates() y can not be greater than 464");
        }
    }

    @Override
    public PrettyStringBuilder buildPrettyString(PrettyStringBuilder build) {
        build.appendLine("Coordinates:");
        build.increaseIdent();

        build.appendLine("x: %d", x);
        build.appendLine("y: %d", y);

        build.decreaseIdent();
        return build;
    }

    public static Coordinates fromStream(StringStream stream) {
        return new Coordinates(Long.parseLong(stream.read()), Long.parseLong(stream.read()));
    }

    @Override
    public void writeToStream(StringStream stream) {
        stream.writeAny(x);
        stream.writeAny(y);
    }
}