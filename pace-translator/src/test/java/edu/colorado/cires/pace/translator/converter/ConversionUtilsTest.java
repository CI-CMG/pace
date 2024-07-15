package edu.colorado.cires.pace.translator.converter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.cires.pace.data.translator.DateTimeSeparatedTimeTranslator;
import edu.colorado.cires.pace.data.translator.DateTranslator;
import edu.colorado.cires.pace.data.translator.DefaultTimeTranslator;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.repository.ShipRepository;
import edu.colorado.cires.pace.translator.FieldException;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ConversionUtilsTest {

  @Test
  void uuidFromMap() {
    UUID uuid = UUID.randomUUID();
    
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("test-property", new ValueWithColumnNumber(Optional.of(uuid.toString()), 1));

    UUID result = ConversionUtils.uuidFromMap(map, "test-property", 2, new RuntimeException());
    assertEquals(uuid, result);

    RuntimeException runtimeException = new RuntimeException();
    map.put("test-property", new ValueWithColumnNumber(Optional.of("test"), 1));

    result = ConversionUtils.uuidFromMap(map, "test-property", 2, runtimeException);
    assertNull(result);

    assertEquals(1, runtimeException.getSuppressed().length);
    Throwable throwable = runtimeException.getSuppressed()[0];
    assertInstanceOf(FieldException.class, throwable);
    FieldException fieldException = (FieldException) throwable;
    assertEquals("test-property", fieldException.getProperty());
    assertEquals(1, fieldException.getColumn());
    assertEquals(2, fieldException.getRow());
    assertEquals("Invalid UUID format", fieldException.getMessage());
  }

  @Test
  void floatFromMap() {
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("test-property", new ValueWithColumnNumber(Optional.of("1"), 1));

    Float result = ConversionUtils.floatFromMap(map, "test-property", 2, new RuntimeException());
    assertEquals(result, 1);

    RuntimeException runtimeException = new RuntimeException();
    map.put("test-property", new ValueWithColumnNumber(Optional.of("test"), 1));

    result = ConversionUtils.floatFromMap(map, "test-property", 2, runtimeException);
    assertNull(result);

    assertEquals(1, runtimeException.getSuppressed().length);
    Throwable throwable = runtimeException.getSuppressed()[0];
    assertInstanceOf(FieldException.class, throwable);
    FieldException fieldException = (FieldException) throwable;
    assertEquals("test-property", fieldException.getProperty());
    assertEquals(1, fieldException.getColumn());
    assertEquals(2, fieldException.getRow());
    assertEquals("Invalid float format", fieldException.getMessage());
  }

  @Test
  void integerFromMap() {
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("test-property", new ValueWithColumnNumber(Optional.of("1"), 1));
    
    Integer result = ConversionUtils.integerFromMap(map, "test-property", 2, new RuntimeException());
    assertEquals(result, 1);
    
    RuntimeException runtimeException = new RuntimeException();
    map.put("test-property", new ValueWithColumnNumber(Optional.of("test"), 1));
    
    result = ConversionUtils.integerFromMap(map, "test-property", 2, runtimeException);
    assertNull(result);
    
    assertEquals(1, runtimeException.getSuppressed().length);
    Throwable throwable = runtimeException.getSuppressed()[0];
    assertInstanceOf(FieldException.class, throwable);
    FieldException fieldException = (FieldException) throwable;
    assertEquals("test-property", fieldException.getProperty());
    assertEquals(1, fieldException.getColumn());
    assertEquals(2, fieldException.getRow());
    assertEquals("Invalid integer format", fieldException.getMessage());
  }

  @Test
  void pathFromMap() {
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("test-property", new ValueWithColumnNumber(Optional.of("test"), 1));

    Path result = ConversionUtils.pathFromMap(map, "test-property", 2, new RuntimeException());
    assertEquals(result, Path.of("test"));
  }

  @Test
  void localDateFromMap() {
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("test-property", new ValueWithColumnNumber(Optional.of("2024-01-01"), 1));
    map.put("test-zone", new ValueWithColumnNumber(Optional.of("UTC"), 2));

    RuntimeException runtimeException = new RuntimeException();
    LocalDate result = ConversionUtils.localDateFromMap(map, DateTranslator.builder()
            .date("test-property")
            .timeZone("test-zone")
        .build(), 2, runtimeException);
    assertEquals(result, LocalDate.parse("2024-01-01"));

    map.put("test-property", new ValueWithColumnNumber(Optional.of("test"), 1));
    runtimeException = new RuntimeException();
    result = ConversionUtils.localDateFromMap(map, DateTranslator.builder()
            .date("test-property")
            .timeZone("test-zone")
        .build(), 2, runtimeException);
    assertNull(result);

    assertEquals(1, runtimeException.getSuppressed().length);
    Throwable throwable = runtimeException.getSuppressed()[0];
    assertInstanceOf(FieldException.class, throwable);
    FieldException fieldException = (FieldException) throwable;
    assertEquals("test-property", fieldException.getProperty());
    assertEquals(1, fieldException.getColumn());
    assertEquals(2, fieldException.getRow());
    assertEquals("Invalid date format", fieldException.getMessage());
  }
  
  @ParameterizedTest
  @ValueSource(strings = {
      "2024-01-01",
      "2024-01-01T01:02:03",
      "2024-01-01 01:02:03",
      "01/01/2024",
      "01/01/2024 01:02:03",
      "1/1/24",
      "1/1/24 01:02:03",
      "1/1/2024",
      "1/1/2024 01:02:03"
  })
  void testDateFormats(String inputDate) {
    RuntimeException runtimeException = new RuntimeException();
    LocalDate result = ConversionUtils.localDateFromMap(
        Map.of(
            "date", new ValueWithColumnNumber(Optional.of(inputDate), 1),
            "timeZone", new ValueWithColumnNumber(Optional.of("UTC"), 2)
        ),
        DateTranslator.builder()
            .date("date")
            .timeZone("timeZone")
            .build(),
        1,
        runtimeException
    );
    assertNotNull(result);
    assertEquals(LocalDate.parse("2024-01-01"), result);
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "2024-01-01T01:02:03",
      "2024-01-01 01:02:03",
      "01/01/2024 01:02:03",
      "1/1/24 01:02:03",
      "1/1/2024 01:02:03"
  })
  void testDateTimeFormats(String inputDate) {
    RuntimeException runtimeException = new RuntimeException();
    LocalDateTime result = ConversionUtils.localDateTimeFromMap(
        Map.of(
            "date", new ValueWithColumnNumber(Optional.of(inputDate), 1),
            "timeZone", new ValueWithColumnNumber(Optional.of("UTC"), 2)
        ),
        DefaultTimeTranslator.builder()
            .time("date")
            .timeZone("timeZone")
            .build(),
        1,
        runtimeException
    );
    assertNotNull(result);
    assertEquals(LocalDateTime.parse("2024-01-01T01:02:03"), result);
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "UTC",
      "Asia/Aden",
      "America/Cuiaba",
      "Etc/GMT+9",
      "Etc/GMT+8",
      "Africa/Nairobi",
      "America/Marigot",
      "Asia/Aqtau",
      "Pacific/Kwajalein",
      "America/El_Salvador",
      "Asia/Pontianak",
      "Africa/Cairo",
      "Pacific/Pago_Pago",
      "Africa/Mbabane",
      "Asia/Kuching",
      "Pacific/Honolulu",
      "Pacific/Rarotonga",
      "America/Guatemala",
      "Australia/Hobart",
      "Europe/London",
      "America/Belize",
      "America/Panama",
      "Asia/Chungking",
      "America/Managua",
      "America/Indiana/Petersburg",
      "Asia/Yerevan",
      "Europe/Brussels",
      "GMT",
      "Europe/Warsaw",
      "America/Chicago",
      "Asia/Kashgar",
      "Chile/Continental",
      "Pacific/Yap",
      "CET",
      "Etc/GMT-1",
      "Etc/GMT-0",
      "Europe/Jersey",
      "America/Tegucigalpa",
      "Etc/GMT-5",
      "Europe/Istanbul",
      "America/Eirunepe",
      "Etc/GMT-4",
      "America/Miquelon",
      "Etc/GMT-3",
      "Europe/Luxembourg",
      "Etc/GMT-2",
      "Etc/GMT-9",
      "America/Argentina/Catamarca",
      "Etc/GMT-8",
      "Etc/GMT-7",
      "Etc/GMT-6",
      "Europe/Zaporozhye",
      "Canada/Yukon",
      "Canada/Atlantic",
      "Atlantic/St_Helena",
      "Australia/Tasmania",
      "Libya",
      "Europe/Guernsey",
      "America/Grand_Turk",
      "Asia/Samarkand",
      "America/Argentina/Cordoba",
      "Asia/Phnom_Penh",
      "Africa/Kigali",
      "Asia/Almaty",
      "US/Alaska",
      "Asia/Dubai",
      "Europe/Isle_of_Man",
      "America/Araguaina",
      "Cuba",
      "Asia/Novosibirsk",
      "America/Argentina/Salta",
      "Etc/GMT+3",
      "Africa/Tunis",
      "Etc/GMT+2",
      "Etc/GMT+1",
      "Pacific/Fakaofo",
      "Africa/Tripoli",
      "Etc/GMT+0",
      "Israel",
      "Africa/Banjul",
      "Etc/GMT+7",
      "Indian/Comoro",
      "Etc/GMT+6",
      "Etc/GMT+5",
      "Etc/GMT+4",
      "Pacific/Port_Moresby",
      "US/Arizona",
      "Antarctica/Syowa",
      "Indian/Reunion",
      "Pacific/Palau",
      "Europe/Kaliningrad",
      "America/Montevideo",
      "Africa/Windhoek",
      "Asia/Karachi",
      "Africa/Mogadishu",
      "Australia/Perth",
      "Brazil/East",
      "Etc/GMT",
      "Asia/Chita",
      "Pacific/Easter",
      "Antarctica/Davis",
      "Antarctica/McMurdo",
      "Asia/Macao",
      "America/Manaus",
      "Africa/Freetown",
      "Europe/Bucharest",
      "Asia/Tomsk",
      "America/Argentina/Mendoza",
      "Asia/Macau",
      "Europe/Malta",
      "Mexico/BajaSur",
      "Pacific/Tahiti",
      "Africa/Asmera",
      "Europe/Busingen",
      "America/Argentina/Rio_Gallegos",
      "Africa/Malabo",
      "Europe/Skopje",
      "America/Catamarca",
      "America/Godthab",
      "Europe/Sarajevo",
      "Australia/ACT",
      "GB-Eire",
      "Africa/Lagos",
      "America/Cordoba",
      "Europe/Rome",
      "Asia/Dacca",
      "Indian/Mauritius",
      "Pacific/Samoa",
      "America/Regina",
      "America/Fort_Wayne",
      "America/Dawson_Creek",
      "Africa/Algiers",
      "Europe/Mariehamn",
      "America/St_Johns",
      "America/St_Thomas",
      "Europe/Zurich",
      "America/Anguilla",
      "Asia/Dili",
      "America/Denver",
      "Africa/Bamako",
      "Europe/Saratov",
      "GB",
      "Mexico/General",
      "Pacific/Wallis",
      "Europe/Gibraltar",
      "Africa/Conakry",
      "Africa/Lubumbashi",
      "Asia/Istanbul",
      "America/Havana",
      "NZ-CHAT",
      "Asia/Choibalsan",
      "America/Porto_Acre",
      "Asia/Omsk",
      "Europe/Vaduz",
      "US/Michigan",
      "Asia/Dhaka",
      "America/Barbados",
      "Europe/Tiraspol",
      "Atlantic/Cape_Verde",
      "Asia/Yekaterinburg",
      "America/Louisville",
      "Pacific/Johnston",
      "Pacific/Chatham",
      "Europe/Ljubljana",
      "America/Sao_Paulo",
      "Asia/Jayapura",
      "America/Curacao",
      "Asia/Dushanbe",
      "America/Guyana",
      "America/Guayaquil",
      "America/Martinique",
      "Portugal",
      "Europe/Berlin",
      "Europe/Moscow",
      "Europe/Chisinau",
      "America/Puerto_Rico",
      "America/Rankin_Inlet",
      "Pacific/Ponape",
      "Europe/Stockholm",
      "Europe/Budapest",
      "America/Argentina/Jujuy",
      "Australia/Eucla",
      "Asia/Shanghai",
      "Universal",
      "Europe/Zagreb",
      "America/Port_of_Spain",
      "Europe/Helsinki",
      "Asia/Beirut",
      "Asia/Tel_Aviv",
      "Pacific/Bougainville",
      "US/Central",
      "Africa/Sao_Tome",
      "Indian/Chagos",
      "America/Cayenne",
      "Asia/Yakutsk",
      "Pacific/Galapagos",
      "Australia/North",
      "Europe/Paris",
      "Africa/Ndjamena",
      "Pacific/Fiji",
      "America/Rainy_River",
      "Indian/Maldives",
      "Australia/Yancowinna",
      "SystemV/AST4",
      "Asia/Oral",
      "America/Yellowknife",
      "Pacific/Enderbury",
      "America/Juneau",
      "Australia/Victoria",
      "America/Indiana/Vevay",
      "Asia/Tashkent",
      "Asia/Jakarta",
      "Africa/Ceuta",
      "Asia/Barnaul",
      "America/Recife",
      "America/Buenos_Aires",
      "America/Noronha",
      "America/Swift_Current",
      "Australia/Adelaide",
      "America/Metlakatla",
      "Africa/Djibouti",
      "America/Paramaribo",
      "Asia/Qostanay",
      "Europe/Simferopol",
      "Europe/Sofia",
      "Africa/Nouakchott",
      "Europe/Prague",
      "America/Indiana/Vincennes",
      "Antarctica/Mawson",
      "America/Kralendijk",
      "Antarctica/Troll",
      "Europe/Samara",
      "Indian/Christmas",
      "America/Antigua",
      "Pacific/Gambier",
      "America/Indianapolis",
      "America/Inuvik",
      "America/Iqaluit",
      "Pacific/Funafuti",
      "UTC",
      "Antarctica/Macquarie",
      "Canada/Pacific",
      "America/Moncton",
      "Africa/Gaborone",
      "Pacific/Chuuk",
      "Asia/Pyongyang",
      "America/St_Vincent",
      "Asia/Gaza",
      "Etc/Universal",
      "PST8PDT",
      "Atlantic/Faeroe",
      "Asia/Qyzylorda",
      "Canada/Newfoundland",
      "America/Kentucky/Louisville",
      "America/Yakutat",
      "America/Ciudad_Juarez",
      "Asia/Ho_Chi_Minh",
      "Antarctica/Casey",
      "Europe/Copenhagen",
      "Africa/Asmara",
      "Atlantic/Azores",
      "Europe/Vienna",
      "ROK",
      "Pacific/Pitcairn",
      "America/Mazatlan",
      "Australia/Queensland",
      "Pacific/Nauru",
      "Europe/Tirane",
      "Asia/Kolkata",
      "SystemV/MST7",
      "Australia/Canberra",
      "MET",
      "Australia/Broken_Hill",
      "Europe/Riga",
      "America/Dominica",
      "Africa/Abidjan",
      "America/Mendoza",
      "America/Santarem",
      "Kwajalein",
      "America/Asuncion",
      "Asia/Ulan_Bator",
      "NZ",
      "America/Boise",
      "Australia/Currie",
      "EST5EDT",
      "Pacific/Guam",
      "Pacific/Wake",
      "Atlantic/Bermuda",
      "America/Costa_Rica",
      "America/Dawson",
      "Asia/Chongqing",
      "Eire",
      "Europe/Amsterdam",
      "America/Indiana/Knox",
      "America/North_Dakota/Beulah",
      "Africa/Accra",
      "Atlantic/Faroe",
      "Mexico/BajaNorte",
      "America/Maceio",
      "Etc/UCT",
      "Pacific/Apia",
      "GMT0",
      "America/Atka",
      "Pacific/Niue",
      "Australia/Lord_Howe",
      "Europe/Dublin",
      "Pacific/Truk",
      "MST7MDT",
      "America/Monterrey",
      "America/Nassau",
      "America/Jamaica",
      "Asia/Bishkek",
      "America/Atikokan",
      "Atlantic/Stanley",
      "Australia/NSW",
      "US/Hawaii",
      "SystemV/CST6",
      "Indian/Mahe",
      "Asia/Aqtobe",
      "America/Sitka",
      "Asia/Vladivostok",
      "Africa/Libreville",
      "Africa/Maputo",
      "Zulu",
      "America/Kentucky/Monticello",
      "Africa/El_Aaiun",
      "Africa/Ouagadougou",
      "America/Coral_Harbour",
      "Pacific/Marquesas",
      "Brazil/West",
      "America/Aruba",
      "America/North_Dakota/Center",
      "America/Cayman",
      "Asia/Ulaanbaatar",
      "Asia/Baghdad",
      "Europe/San_Marino",
      "America/Indiana/Tell_City",
      "America/Tijuana",
      "Pacific/Saipan",
      "SystemV/YST9",
      "Africa/Douala",
      "America/Chihuahua",
      "America/Ojinaga",
      "Asia/Hovd",
      "America/Anchorage",
      "Chile/EasterIsland",
      "America/Halifax",
      "Antarctica/Rothera",
      "America/Indiana/Indianapolis",
      "US/Mountain",
      "Asia/Damascus",
      "America/Argentina/San_Luis",
      "America/Santiago",
      "Asia/Baku",
      "America/Argentina/Ushuaia",
      "Atlantic/Reykjavik",
      "Africa/Brazzaville",
      "Africa/Porto-Novo",
      "America/La_Paz",
      "Antarctica/DumontDUrville",
      "Asia/Taipei",
      "Antarctica/South_Pole",
      "Asia/Manila",
      "Asia/Bangkok",
      "Africa/Dar_es_Salaam",
      "Poland",
      "Atlantic/Madeira",
      "Antarctica/Palmer",
      "America/Thunder_Bay",
      "Africa/Addis_Ababa",
      "Asia/Yangon",
      "Europe/Uzhgorod",
      "Brazil/DeNoronha",
      "Asia/Ashkhabad",
      "Etc/Zulu",
      "America/Indiana/Marengo",
      "America/Creston",
      "America/Punta_Arenas",
      "America/Mexico_City",
      "Antarctica/Vostok",
      "Asia/Jerusalem",
      "Europe/Andorra",
      "US/Samoa",
      "PRC",
      "Asia/Vientiane",
      "Pacific/Kiritimati",
      "America/Matamoros",
      "America/Blanc-Sablon",
      "Asia/Riyadh",
      "Iceland",
      "Pacific/Pohnpei",
      "Asia/Ujung_Pandang",
      "Atlantic/South_Georgia",
      "Europe/Lisbon",
      "Asia/Harbin",
      "Europe/Oslo",
      "Asia/Novokuznetsk",
      "CST6CDT",
      "Atlantic/Canary",
      "America/Knox_IN",
      "Asia/Kuwait",
      "SystemV/HST10",
      "Pacific/Efate",
      "Africa/Lome",
      "America/Bogota",
      "America/Menominee",
      "America/Adak",
      "Pacific/Norfolk",
      "Europe/Kirov",
      "America/Resolute",
      "Pacific/Kanton",
      "Pacific/Tarawa",
      "Africa/Kampala",
      "Asia/Krasnoyarsk",
      "Greenwich",
      "SystemV/EST5",
      "America/Edmonton",
      "Europe/Podgorica",
      "Australia/South",
      "Canada/Central",
      "Africa/Bujumbura",
      "America/Santo_Domingo",
      "US/Eastern",
      "Europe/Minsk",
      "Pacific/Auckland",
      "Africa/Casablanca",
      "America/Glace_Bay",
      "Canada/Eastern",
      "Asia/Qatar",
      "Europe/Kiev",
      "Singapore",
      "Asia/Magadan",
      "SystemV/PST8",
      "America/Port-au-Prince",
      "Europe/Belfast",
      "America/St_Barthelemy",
      "Asia/Ashgabat",
      "Africa/Luanda",
      "America/Nipigon",
      "Atlantic/Jan_Mayen",
      "Brazil/Acre",
      "Asia/Muscat",
      "Asia/Bahrain",
      "Europe/Vilnius",
      "America/Fortaleza",
      "Etc/GMT0",
      "US/East-Indiana",
      "America/Hermosillo",
      "America/Cancun",
      "Africa/Maseru",
      "Pacific/Kosrae",
      "Africa/Kinshasa",
      "Asia/Kathmandu",
      "Asia/Seoul",
      "Australia/Sydney",
      "America/Lima",
      "Australia/LHI",
      "America/St_Lucia",
      "Europe/Madrid",
      "America/Bahia_Banderas",
      "America/Montserrat",
      "Asia/Brunei",
      "America/Santa_Isabel",
      "Canada/Mountain",
      "America/Cambridge_Bay",
      "Asia/Colombo",
      "Australia/West",
      "Indian/Antananarivo",
      "Australia/Brisbane",
      "Indian/Mayotte",
      "US/Indiana-Starke",
      "Asia/Urumqi",
      "US/Aleutian",
      "Europe/Volgograd",
      "America/Lower_Princes",
      "America/Vancouver",
      "Africa/Blantyre",
      "America/Rio_Branco",
      "America/Danmarkshavn",
      "America/Detroit",
      "America/Thule",
      "Africa/Lusaka",
      "Asia/Hong_Kong",
      "Iran",
      "America/Argentina/La_Rioja",
      "Africa/Dakar",
      "SystemV/CST6CDT",
      "America/Tortola",
      "America/Porto_Velho",
      "Asia/Sakhalin",
      "Etc/GMT+10",
      "America/Scoresbysund",
      "Asia/Kamchatka",
      "Asia/Thimbu",
      "Africa/Harare",
      "Etc/GMT+12",
      "Etc/GMT+11",
      "Navajo",
      "America/Nome",
      "Europe/Tallinn",
      "Turkey",
      "Africa/Khartoum",
      "Africa/Johannesburg",
      "Africa/Bangui",
      "Europe/Belgrade",
      "Jamaica",
      "Africa/Bissau",
      "Asia/Tehran",
      "WET",
      "Europe/Astrakhan",
      "Africa/Juba",
      "America/Campo_Grande",
      "America/Belem",
      "Etc/Greenwich",
      "Asia/Saigon",
      "America/Ensenada",
      "Pacific/Midway",
      "America/Jujuy",
      "Africa/Timbuktu",
      "America/Bahia",
      "America/Goose_Bay",
      "America/Virgin",
      "America/Pangnirtung",
      "Asia/Katmandu",
      "America/Phoenix",
      "Africa/Niamey",
      "America/Whitehorse",
      "Pacific/Noumea",
      "Asia/Tbilisi",
      "Europe/Kyiv",
      "America/Montreal",
      "Asia/Makassar",
      "America/Argentina/San_Juan",
      "Hongkong",
      "UCT",
      "Asia/Nicosia",
      "America/Indiana/Winamac",
      "SystemV/MST7MDT",
      "America/Argentina/ComodRivadavia",
      "America/Boa_Vista",
      "America/Grenada",
      "Asia/Atyrau",
      "Australia/Darwin",
      "Asia/Khandyga",
      "Asia/Kuala_Lumpur",
      "Asia/Famagusta",
      "Asia/Thimphu",
      "Asia/Rangoon",
      "Europe/Bratislava",
      "Asia/Calcutta",
      "America/Argentina/Tucuman",
      "Asia/Kabul",
      "Indian/Cocos",
      "Japan",
      "Pacific/Tongatapu",
      "America/New_York",
      "Etc/GMT-12",
      "Etc/GMT-11",
      "America/Nuuk",
      "Etc/GMT-10",
      "SystemV/YST9YDT",
      "Europe/Ulyanovsk",
      "Etc/GMT-14",
      "Etc/GMT-13",
      "W-SU",
      "America/Merida",
      "EET",
      "America/Rosario",
      "Canada/Saskatchewan",
      "America/St_Kitts",
      "Arctic/Longyearbyen",
      "America/Fort_Nelson",
      "America/Caracas",
      "America/Guadeloupe",
      "Asia/Hebron",
      "Indian/Kerguelen",
      "SystemV/PST8PDT",
      "Africa/Monrovia",
      "Asia/Ust-Nera",
      "Egypt",
      "Asia/Srednekolymsk",
      "America/North_Dakota/New_Salem",
      "Asia/Anadyr",
      "Australia/Melbourne",
      "Asia/Irkutsk",
      "America/Shiprock",
      "America/Winnipeg",
      "Europe/Vatican",
      "Asia/Amman",
      "Etc/UTC",
      "SystemV/AST4ADT",
      "Asia/Tokyo",
      "America/Toronto",
      "Asia/Singapore",
      "Australia/Lindeman",
      "America/Los_Angeles",
      "SystemV/EST5EDT",
      "Pacific/Majuro",
      "America/Argentina/Buenos_Aires",
      "Europe/Nicosia",
      "Pacific/Guadalcanal",
      "Europe/Athens",
      "US/Pacific",
      "Europe/Monaco",
  })
  void testDateTimeZones(String zone) {
    RuntimeException runtimeException = new RuntimeException();
    LocalDateTime result = ConversionUtils.localDateTimeFromMap(
        Map.of(
            "date", new ValueWithColumnNumber(Optional.of("1/1/24 01:02:03"), 1),
            "timeZone", new ValueWithColumnNumber(Optional.of(zone), 2)
        ),
        DefaultTimeTranslator.builder()
            .time("date")
            .timeZone("timeZone")
            .build(),
        1,
        runtimeException
    );
    assertNotNull(result);
    assertEquals(LocalDateTime.parse("2024-01-01T01:02:03"), result);
  }

  @Test
  void localDateTimeFromMap() {
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("test-property", new ValueWithColumnNumber(Optional.of("2024-01-01T01:00:00"), 1));
    map.put("test-zone", new ValueWithColumnNumber(Optional.of("UTC"), 2));

    LocalDateTime result = ConversionUtils.localDateTimeFromMap(map, DefaultTimeTranslator.builder()
        .timeZone("test-zone")
        .time("test-property")
        .build(), 2, new RuntimeException());
    assertEquals(result, LocalDateTime.parse("2024-01-01T01:00:00"));

    RuntimeException runtimeException = new RuntimeException();
    map.put("test-property", new ValueWithColumnNumber(Optional.of("test"), 1));

    result = ConversionUtils.localDateTimeFromMap(map, DefaultTimeTranslator.builder()
        .timeZone("test-zone")
        .time("test-property").build(), 2, runtimeException);
    assertNull(result);

    assertEquals(1, runtimeException.getSuppressed().length);
    Throwable throwable = runtimeException.getSuppressed()[0];
    assertInstanceOf(FieldException.class, throwable);
    FieldException fieldException = (FieldException) throwable;
    assertEquals("test-property", fieldException.getProperty());
    assertEquals(1, fieldException.getColumn());
    assertEquals(2, fieldException.getRow());
    assertEquals("Invalid date time format", fieldException.getMessage());
    
    map.put("test-property", new ValueWithColumnNumber(Optional.of("2024-01-01"), 1));
    map.put("test-other-property", new ValueWithColumnNumber(Optional.of("12:00:00"), 2));

    runtimeException = new RuntimeException();
    result = ConversionUtils.localDateTimeFromMap(map, () -> "test-zone-property", 2, runtimeException);
    assertNull(result);
    assertEquals(0, runtimeException.getSuppressed().length);
    
    result = ConversionUtils.localDateTimeFromMap(map, DateTimeSeparatedTimeTranslator.builder()
            .date("test-property")
            .time("test-other-property")
            .timeZone("test-zone")
        .build(), 2, new RuntimeException());
    
    assertEquals(LocalDateTime.parse("2024-01-01T12:00:00"), result);
    
    runtimeException = new RuntimeException();
    map.put("test-other-property", new ValueWithColumnNumber(Optional.of("-"), 2));
    result = ConversionUtils.localDateTimeFromMap(map, DateTimeSeparatedTimeTranslator.builder()
        .date("test-property")
        .time("test-other-property")
        .timeZone("test-zone")
        .build(), 2, runtimeException);
    assertEquals(LocalDateTime.parse("2024-01-01T00:00:00"), result);

    assertEquals(1, runtimeException.getSuppressed().length);
    throwable = runtimeException.getSuppressed()[0];
    assertInstanceOf(FieldException.class, throwable);
    fieldException = (FieldException) throwable;
    assertEquals("test-other-property", fieldException.getProperty());
    assertEquals(2, fieldException.getColumn());
    assertEquals(2, fieldException.getRow());
    assertEquals("Invalid time format", fieldException.getMessage());

    runtimeException = new RuntimeException();
    map.remove("test-other-property");
    result = ConversionUtils.localDateTimeFromMap(map, DateTimeSeparatedTimeTranslator.builder()
        .date("test-property")
        .time("test-other-property")
        .timeZone("test-zone")
        .build(), 2, runtimeException);
    assertEquals(LocalDateTime.parse("2024-01-01T00:00:00"), result);

    assertEquals(0, runtimeException.getSuppressed().length);

    runtimeException = new RuntimeException();
    map.remove("test-property");
    result = ConversionUtils.localDateTimeFromMap(map, DateTimeSeparatedTimeTranslator.builder()
        .date("test-property")
        .time("test-other-property")
        .timeZone("test-zone")
        .build(), 2, runtimeException);
    assertNull(result);

    assertEquals(0, runtimeException.getSuppressed().length);
  }

  @Test
  void propertyFromMap() {
    Map<String, ValueWithColumnNumber> map = Map.of(
        "test-property", new ValueWithColumnNumber(Optional.of("test"), 1)
    );
    
    ValueWithColumnNumber valueWithColumnNumber = ConversionUtils.propertyFromMap(
        map,
        "test-property"
    );
    
    assertEquals(1, valueWithColumnNumber.column());
    assertEquals("test", valueWithColumnNumber.value().orElseThrow());
    
    valueWithColumnNumber = ConversionUtils.propertyFromMap(
        map,
        "test-other-property"
    );
    assertTrue(valueWithColumnNumber.value().isEmpty());
    assertNull(valueWithColumnNumber.column());
  }

  @Test
  void stringFromMap() {
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("test-property", new ValueWithColumnNumber(Optional.of("test  "), 1));
    
    String result = ConversionUtils.stringFromMap(
        map, "test-property"
    );
    
    assertEquals("test", result);
    
    map.put("test-property", new ValueWithColumnNumber(Optional.of(""), 1));
    
    result = ConversionUtils.stringFromMap(
        map, "test-property"
    );
    
    assertNull(result);

    map.put("test-property", new ValueWithColumnNumber(Optional.empty(), 1));

    result = ConversionUtils.stringFromMap(
        map, "test-property"
    );

    assertNull(result);

    map.remove("test-property");

    result = ConversionUtils.stringFromMap(
        map, "test-property"
    );

    assertNull(result);
  }

  @Test
  void objectFromMap() throws NotFoundException, DatastoreException {
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("test-property", new ValueWithColumnNumber(Optional.of("ship-1"), 1));

    ShipRepository repository = mock(ShipRepository.class);
    try {
      when(repository.getByUniqueField("ship-1")).thenReturn(Ship.builder()
              .name("ship-1")
          .build());
    } catch (DatastoreException | NotFoundException e) {
      throw new RuntimeException(e);
    }

    Ship ship = ConversionUtils.objectFromMap(
        map, "test-property", repository, 1, new RuntimeException()
    );
    assertNotNull(ship);
    assertEquals("ship-1", ship.getName());
    
    when(repository.getByUniqueField("ship-1")).thenThrow(
        new NotFoundException("ship-1 not found")
    );

    RuntimeException runtimeException = new RuntimeException();
    ship = ConversionUtils.objectFromMap(
        map, "test-property", repository, 1, runtimeException
    );
    assertNull(ship);
    assertEquals(1, runtimeException.getSuppressed().length);
    Throwable throwable = runtimeException.getSuppressed()[0];
    assertInstanceOf(FieldException.class, throwable);
    FieldException fieldException = (FieldException) throwable;
    assertEquals("ship-1 not found", fieldException.getMessage());

    ship = ConversionUtils.objectFromMap(
        map, "test-other-property", repository, 1, runtimeException
    );
    assertNull(ship);
  }

  @Test
  void delimitedObjectsFromMap() throws NotFoundException, DatastoreException {
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("test-property", new ValueWithColumnNumber(Optional.of("ship-1;ship-2"), 1));

    ShipRepository repository = mock(ShipRepository.class);
    when(repository.getByUniqueField("ship-1")).thenReturn(Ship.builder()
        .name("ship-1")
        .build());

    when(repository.getByUniqueField("ship-2")).thenReturn(Ship.builder()
        .name("ship-2")
        .build());

    List<Ship> ships = ConversionUtils.delimitedObjectsFromMap(
        map, "test-property", 1, new RuntimeException(), repository
    );
    assertEquals(2, ships.size());
    assertEquals(Set.of("ship-1", "ship-2"), ships.stream().map(Ship::getName).collect(Collectors.toSet()));

    when(repository.getByUniqueField("ship-2")).thenThrow(
        new NotFoundException("ship-2 not found")
    );

    RuntimeException runtimeException = new RuntimeException();
    ships = ConversionUtils.delimitedObjectsFromMap(
        map, "test-property", 1, runtimeException, repository
    );
    assertEquals(1, ships.size());
    assertEquals(1, runtimeException.getSuppressed().length);
    Throwable throwable = runtimeException.getSuppressed()[0];
    assertInstanceOf(FieldException.class, throwable);
    FieldException fieldException = (FieldException) throwable;
    assertEquals("ship-2 not found", fieldException.getMessage());
    
    ships = ConversionUtils.delimitedObjectsFromMap(map, "test-other-property", 1, new RuntimeException(), repository);
    assertEquals(0, ships.size());
  }
  
  @ParameterizedTest
  @ValueSource(strings = {
      "150°35'21\"E",
      "150°35'21\"W",
      "150° 35' 21\" E",
      "150° 35' 21\" W",
      "150D35M21SE",
      "150D35M21SW",
      "150D 35M 21S E",
      "150D 35M 21S W",
      "150d35m21sE",
      "150d35m21sW",
      "150d 35m 21s E",
      "150d 35m 21s W",
      "150.589167",
      "-150.589167",
      "150°35.35'E",
      "150°35.35'W",
      "150° 35.35' E",
      "150° 35.35' W",
      "150D35.35ME",
      "150D35.35MW",
      "150D 35.35M E",
      "150D 35.35M W",
      "150d35.35mE",
      "150d35.35mW",
      "150d 35.35m E",
      "150d 35.35m W",
  })
  void testConvertDMSToDecimalDegreesLongitude(String dmsString) {
    double expected = 150.589167;

    assertEquals(
        (dmsString.endsWith("W") || dmsString.startsWith("-")) ? expected * -1 : expected,
        ConversionUtils.longitudeFromMap(
            Map.of("lon", new ValueWithColumnNumber(Optional.of(dmsString), 2)),
            "lon",
            1,
            new RuntimeException()
        )
    );
  }
  
  @ParameterizedTest
  @ValueSource(strings = {
    "30°35'21\"N",
    "30°35'21\"S",
    "30° 35' 21\" N",
    "30° 35' 21\" S",
    "30D35M21SN",
    "30D35M21SS",
    "30D 35M 21S N",
    "30D 35M 21S S",
    "30d35m21sN",
    "30d35m21sS",
    "30d 35m 21s N",
    "30d 35m 21s S",
    "30.589167",
    "-30.589167",
    "30°35.35'N",
    "30°35.35'S",
    "30° 35.35' N",
    "30° 35.35' S",
    "30D35.35MN",
    "30D35.35MS",
    "30D 35.35M N",
    "30D 35.35M S",
    "30d35.35mN",
    "30d35.35mS",
    "30d 35.35m N",
    "30d 35.35m S",
  })
  void testConvertDMSToDecimalDegreesLatitude(String dmsString) {
    double expected = 30.589167;
    assertEquals(
        (dmsString.endsWith("S") || dmsString.startsWith("-")) ? expected * -1 : expected,
        ConversionUtils.latitudeFromMap(
            Map.of("lat", new ValueWithColumnNumber(Optional.of(dmsString), 2)),
            "lat",
            1,
            new RuntimeException()
        )
    );
  }
}