package database

import lib.CSV.CSVStreamLikeReader
import lib.ConvertToStreamHelper
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

fun addressFromStream(stream: CSVStreamLikeReader): Address? {
    if (stream.next == "null" && stream.elementLeftInLine == 1) {
        stream.readElem();
        return null
    }

    return Address(
        ConvertToStreamHelper.convertNullableFromStream(
            stream
        ) { obj -> obj.readElem() },
        locationFromStream(stream)
    )
}

fun organizationFromStream(stream: CSVStreamLikeReader): Organization {
    return Organization(
        stream.readElem().toInt(),
        stream.readElem(),
        coordinatesFromStream(stream),
        LocalDate.parse(stream.readElem()),
        stream.readElem().toFloat(),
        stream.readElem(),
        ConvertToStreamHelper.convertNullableFromStream(
            stream
        ) { dataStream: CSVStreamLikeReader? -> dataStream!!.readElem().toInt() },
        ConvertToStreamHelper.convertNullableFromStream(
            stream
        ) { dataStream: CSVStreamLikeReader? ->
            OrganizationType.valueOf(
                dataStream!!.readElem()
            )
        },
        addressFromStream(stream)
    )
}

fun locationFromStream(stream: CSVStreamLikeReader): Location {
    return Location(
        stream.readElem().toDouble(),
        stream.readElem().toFloat(),
        stream.readElem().toLong(),
        ConvertToStreamHelper.convertNullableFromStream(
            stream
        ) { obj -> obj.readElem() }
    )
}

fun coordinatesFromStream(stream: CSVStreamLikeReader): Coordinates {
    return Coordinates(stream.readElem().toLong(), stream.readElem().toLong())
}