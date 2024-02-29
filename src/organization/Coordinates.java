package organization;

import lib.*;

import java.io.IOException;

public record Coordinates(Long x, Long y) implements YamlConvertable, WritableToCSVStream {
    public static Coordinates joinCoordinates(Coordinates first, Coordinates second) {
        if (first == null) {
            return second;
        }

        return new Coordinates(
                first.x() == null ? second.x() : first.x(),
                first.y() == null ? second.y() : first.y()
        );
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