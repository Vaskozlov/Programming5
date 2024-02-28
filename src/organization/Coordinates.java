package organization;

import lib.*;

import java.io.IOException;

public record Coordinates(long x, long y) implements YamlConvertable, WritableToCSVStream {

    public Coordinates {
        if (y > 464) {
            throw new IllegalArgumentException("Coordinates() y can not be greater than 464");
        }
    }

    @Override
    public PrettyStringBuilder constructYaml(PrettyStringBuilder build) {
        build.appendLine("Coordinates:");
        build.increaseIdent();

        build.appendLine("x: %d", x);
        build.appendLine("y: %d", y);

        build.decreaseIdent();
        return build;
    }

    public static Coordinates fromStream(CSVStreamLikeReader stream) throws IOException {
        return new Coordinates(Long.parseLong(stream.readElem()), Long.parseLong(stream.readElem()));
    }

    @Override
    public void writeToStream(CSVStreamWriter stream) throws IOException {
        stream.append(String.valueOf(x));
        stream.append(String.valueOf(y));
    }
}