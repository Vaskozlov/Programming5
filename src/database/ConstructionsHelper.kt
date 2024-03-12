package database

import lib.CSV.CSVStreamLikeReader
import java.time.LocalDate

fun fillAddressWithMissedInformation(first: Address?, second: Address?): Address? {
    if (first == null && second == null) {
        return null
    }

    return Address(
        first?.zipCode ?: second?.zipCode,
        first?.town ?: second?.town
    )
}

fun fillCoordinatesWithMissedInformation(first: Coordinates?, second: Coordinates?): Coordinates? {
    if (first == null && second == null) {
        return null
    }

    return Coordinates(
        first?.x ?: second?.x,
        first?.y ?: second?.y
    )
}

fun addressFromStream(stream: CSVStreamLikeReader): Address {
    return Address(
        stream.readNullableElem(),
        locationFromStream(stream)
    )
}

fun organizationTypeOrNull(value: String): OrganizationType? {
    if (OrganizationType.entries.map { it.name }.contains(value)) {
        return OrganizationType.valueOf(value)
    }

    return null
}

fun organizationFromStream(stream: CSVStreamLikeReader): Organization {
    return Organization(
        stream.readElem().toIntOrNull(),
        stream.readNullableElem(),
        coordinatesFromStream(stream),
        LocalDate.parse(stream.readElem()),
        stream.readElem().toDoubleOrNull(),
        stream.readNullableElem(),
        stream.readElem().toIntOrNull(),
        organizationTypeOrNull(stream.readElem()),
        addressFromStream(stream).simplify()
    )
}

fun locationFromStream(stream: CSVStreamLikeReader): Location {
    return Location(
        stream.readElem().toDoubleOrNull(),
        stream.readElem().toFloatOrNull(),
        stream.readElem().toLongOrNull(),
        stream.readNullableElem()
    )
}

fun coordinatesFromStream(stream: CSVStreamLikeReader): Coordinates {
    return Coordinates(stream.readElem().toLongOrNull(), stream.readElem().toLongOrNull())
}