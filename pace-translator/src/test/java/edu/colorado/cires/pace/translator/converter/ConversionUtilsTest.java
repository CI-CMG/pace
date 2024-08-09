package edu.colorado.cires.pace.translator.converter;

import static org.junit.jupiter.api.Assertions.*;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DateTimeSeparatedTimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DateTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DefaultTimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;
import edu.colorado.cires.pace.translator.FieldException;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.util.StringUtils;

class ConversionUtilsTest {

  @Test
  void uuidFromMap() {
    UUID uuid = UUID.randomUUID();
    
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("test-property", new ValueWithColumnNumber(Optional.of(uuid.toString()), 1));

    UUID result = ConversionUtils.uuidFromMap(map, "TEST PROPERTY", "test-property", 2, new RuntimeException());
    assertEquals(uuid, result);

    RuntimeException runtimeException = new RuntimeException();
    map.put("test-property", new ValueWithColumnNumber(Optional.of("test"), 1));

    result = ConversionUtils.uuidFromMap(map, "TEST PROPERTY", "test-property", 2, runtimeException);
    assertNull(result);

    assertEquals(1, runtimeException.getSuppressed().length);
    Throwable throwable = runtimeException.getSuppressed()[0];
    assertInstanceOf(FieldException.class, throwable);
    FieldException fieldException = (FieldException) throwable;
    assertEquals("test-property", fieldException.getProperty());
    assertEquals("TEST PROPERTY", fieldException.getTargetProperty());
    assertEquals(1, fieldException.getColumn());
    assertEquals(2, fieldException.getRow());
    assertEquals("Invalid UUID format", fieldException.getMessage());
  }

  @Test
  void floatFromMap() {
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("test-property", new ValueWithColumnNumber(Optional.of("1"), 1));

    Float result = ConversionUtils.floatFromMap(map, "TEST PROPERTY", "test-property", 2, new RuntimeException());
    assertEquals(result, 1);

    RuntimeException runtimeException = new RuntimeException();
    map.put("test-property", new ValueWithColumnNumber(Optional.of("test"), 1));

    result = ConversionUtils.floatFromMap(map, "TEST PROPERTY", "test-property", 2, runtimeException);
    assertNull(result);

    assertEquals(1, runtimeException.getSuppressed().length);
    Throwable throwable = runtimeException.getSuppressed()[0];
    assertInstanceOf(FieldException.class, throwable);
    FieldException fieldException = (FieldException) throwable;
    assertEquals("test-property", fieldException.getProperty());
    assertEquals("TEST PROPERTY", fieldException.getTargetProperty());
    assertEquals(1, fieldException.getColumn());
    assertEquals(2, fieldException.getRow());
    assertEquals("Invalid float format", fieldException.getMessage());
  }

  @Test
  void integerFromMap() {
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("test-property", new ValueWithColumnNumber(Optional.of("1"), 1));
    
    Integer result = ConversionUtils.integerFromMap(map, "TEST PROPERTY", "test-property", 2, new RuntimeException());
    assertEquals(result, 1);
    
    RuntimeException runtimeException = new RuntimeException();
    map.put("test-property", new ValueWithColumnNumber(Optional.of("test"), 1));
    
    result = ConversionUtils.integerFromMap(map, "TEST PROPERTY", "test-property", 2, runtimeException);
    assertNull(result);
    
    assertEquals(1, runtimeException.getSuppressed().length);
    Throwable throwable = runtimeException.getSuppressed()[0];
    assertInstanceOf(FieldException.class, throwable);
    FieldException fieldException = (FieldException) throwable;
    assertEquals("test-property", fieldException.getProperty());
    assertEquals("TEST PROPERTY", fieldException.getTargetProperty());
    assertEquals(1, fieldException.getColumn());
    assertEquals(2, fieldException.getRow());
    assertEquals("Invalid integer format", fieldException.getMessage());
  }

  @Test
  void pathFromMap() {
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("test-property", new ValueWithColumnNumber(Optional.of("test"), 1));

    Path result = ConversionUtils.pathFromMap(map, "TEST PROPERTY", "test-property", 2, new RuntimeException());
    assertEquals(result, Path.of("test"));
  }

  @Test
  void localDateFromMap() {
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("test-property", new ValueWithColumnNumber(Optional.of("2024-01-01"), 1));
    map.put("test-zone", new ValueWithColumnNumber(Optional.of("UTC"), 2));

    RuntimeException runtimeException = new RuntimeException();
    LocalDate result = ConversionUtils.localDateFromMap(map, "TEST PROPERTY", DateTranslator.builder()
            .date("test-property")
            .timeZone("test-zone")
        .build(), 2, runtimeException);
    assertEquals(result, LocalDate.parse("2024-01-01"));

    map.put("test-property", new ValueWithColumnNumber(Optional.of("test"), 1));
    runtimeException = new RuntimeException();
    result = ConversionUtils.localDateFromMap(map, "TEST PROPERTY", DateTranslator.builder()
            .date("test-property")
            .timeZone("test-zone")
        .build(), 2, runtimeException);
    assertNull(result);

    assertEquals(1, runtimeException.getSuppressed().length);
    Throwable throwable = runtimeException.getSuppressed()[0];
    assertInstanceOf(FieldException.class, throwable);
    FieldException fieldException = (FieldException) throwable;
    assertEquals("test-property", fieldException.getProperty());
    assertEquals("TEST PROPERTY", fieldException.getTargetProperty());
    assertEquals(1, fieldException.getColumn());
    assertEquals(2, fieldException.getRow());
    assertEquals("Invalid date format", fieldException.getMessage());
    
    
  }
  
  @ParameterizedTest
  @CsvSource(value = {
      "2024-01-01,UTC,true",
      "2024-01-01T01:02:03,UTC,true",
      "2024-01-01 01:02:03,UTC,true",
      "01/01/2024,UTC,true",
      "01/01/2024 01:02:03,UTC,true",
      "1/1/24,UTC,true",
      "1/1/24 01:02:03,UTC,true",
      "1/1/2024,UTC,true",
      "1/1/2024 01:02:03,UTC,true",
      "1/1/2024 01:02:03,TEST,false",
      "1/1/2024 01:02:03,,false",
  })
  void testDateFormats(String inputDate, String timeZone, boolean expectedPass) {
    RuntimeException runtimeException = new RuntimeException();
    LocalDate result = ConversionUtils.localDateFromMap(
        Map.of(
            "date", new ValueWithColumnNumber(Optional.of(inputDate), 1),
            "timeZone", new ValueWithColumnNumber(Optional.ofNullable(timeZone), 2)
        ),
        "TEST PROPERTY",
        DateTranslator.builder()
            .date("date")
            .timeZone("timeZone")
            .build(),
        1,
        runtimeException
    );
    
    if (expectedPass) {
      assertNotNull(result);
      assertEquals(LocalDate.parse("2024-01-01"), result);
    } else {
      assertNull(result);
      
      if (StringUtils.isNotBlank(timeZone)) {
        FieldException fieldException = (FieldException) runtimeException.getSuppressed()[0];
        assertEquals("Time Zone", fieldException.getTargetProperty());
        assertEquals("Invalid time zone", fieldException.getMessage());
        assertEquals(1, fieldException.getRow());
        assertEquals(2, fieldException.getColumn());
        assertEquals("timeZone", fieldException.getProperty());
      }
    }
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
        "TEST PROPERTY",
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
  @CsvSource(value = {
      "UTC,true",
      "Asia/Aden,true",
      "America/Cuiaba,true",
      "Etc/GMT+9,true",
      "Etc/GMT+8,true",
      "Africa/Nairobi,true",
      "America/Marigot,true",
      "Asia/Aqtau,true",
      "Pacific/Kwajalein,true",
      "America/El_Salvador,true",
      "Asia/Pontianak,true",
      "Africa/Cairo,true",
      "Pacific/Pago_Pago,true",
      "Africa/Mbabane,true",
      "Asia/Kuching,true",
      "Pacific/Honolulu,true",
      "Pacific/Rarotonga,true",
      "America/Guatemala,true",
      "Australia/Hobart,true",
      "Europe/London,true",
      "America/Belize,true",
      "America/Panama,true",
      "Asia/Chungking,true",
      "America/Managua,true",
      "America/Indiana/Petersburg,true",
      "Asia/Yerevan,true",
      "Europe/Brussels,true",
      "GMT,true",
      "Europe/Warsaw,true",
      "America/Chicago,true",
      "Asia/Kashgar,true",
      "Chile/Continental,true",
      "Pacific/Yap,true",
      "CET,true",
      "Etc/GMT-1,true",
      "Etc/GMT-0,true",
      "Europe/Jersey,true",
      "America/Tegucigalpa,true",
      "Etc/GMT-5,true",
      "Europe/Istanbul,true",
      "America/Eirunepe,true",
      "Etc/GMT-4,true",
      "America/Miquelon,true",
      "Etc/GMT-3,true",
      "Europe/Luxembourg,true",
      "Etc/GMT-2,true",
      "Etc/GMT-9,true",
      "America/Argentina/Catamarca,true",
      "Etc/GMT-8,true",
      "Etc/GMT-7,true",
      "Etc/GMT-6,true",
      "Europe/Zaporozhye,true",
      "Canada/Yukon,true",
      "Canada/Atlantic,true",
      "Atlantic/St_Helena,true",
      "Australia/Tasmania,true",
      "Libya,true",
      "Europe/Guernsey,true",
      "America/Grand_Turk,true",
      "Asia/Samarkand,true",
      "America/Argentina/Cordoba,true",
      "Asia/Phnom_Penh,true",
      "Africa/Kigali,true",
      "Asia/Almaty,true",
      "US/Alaska,true",
      "Asia/Dubai,true",
      "Europe/Isle_of_Man,true",
      "America/Araguaina,true",
      "Cuba,true",
      "Asia/Novosibirsk,true",
      "America/Argentina/Salta,true",
      "Etc/GMT+3,true",
      "Africa/Tunis,true",
      "Etc/GMT+2,true",
      "Etc/GMT+1,true",
      "Pacific/Fakaofo,true",
      "Africa/Tripoli,true",
      "Etc/GMT+0,true",
      "Israel,true",
      "Africa/Banjul,true",
      "Etc/GMT+7,true",
      "Indian/Comoro,true",
      "Etc/GMT+6,true",
      "Etc/GMT+5,true",
      "Etc/GMT+4,true",
      "Pacific/Port_Moresby,true",
      "US/Arizona,true",
      "Antarctica/Syowa,true",
      "Indian/Reunion,true",
      "Pacific/Palau,true",
      "Europe/Kaliningrad,true",
      "America/Montevideo,true",
      "Africa/Windhoek,true",
      "Asia/Karachi,true",
      "Africa/Mogadishu,true",
      "Australia/Perth,true",
      "Brazil/East,true",
      "Etc/GMT,true",
      "Asia/Chita,true",
      "Pacific/Easter,true",
      "Antarctica/Davis,true",
      "Antarctica/McMurdo,true",
      "Asia/Macao,true",
      "America/Manaus,true",
      "Africa/Freetown,true",
      "Europe/Bucharest,true",
      "Asia/Tomsk,true",
      "America/Argentina/Mendoza,true",
      "Asia/Macau,true",
      "Europe/Malta,true",
      "Mexico/BajaSur,true",
      "Pacific/Tahiti,true",
      "Africa/Asmera,true",
      "Europe/Busingen,true",
      "America/Argentina/Rio_Gallegos,true",
      "Africa/Malabo,true",
      "Europe/Skopje,true",
      "America/Catamarca,true",
      "America/Godthab,true",
      "Europe/Sarajevo,true",
      "Australia/ACT,true",
      "GB-Eire,true",
      "Africa/Lagos,true",
      "America/Cordoba,true",
      "Europe/Rome,true",
      "Asia/Dacca,true",
      "Indian/Mauritius,true",
      "Pacific/Samoa,true",
      "America/Regina,true",
      "America/Fort_Wayne,true",
      "America/Dawson_Creek,true",
      "Africa/Algiers,true",
      "Europe/Mariehamn,true",
      "America/St_Johns,true",
      "America/St_Thomas,true",
      "Europe/Zurich,true",
      "America/Anguilla,true",
      "Asia/Dili,true",
      "America/Denver,true",
      "Africa/Bamako,true",
      "Europe/Saratov,true",
      "GB,true",
      "Mexico/General,true",
      "Pacific/Wallis,true",
      "Europe/Gibraltar,true",
      "Africa/Conakry,true",
      "Africa/Lubumbashi,true",
      "Asia/Istanbul,true",
      "America/Havana,true",
      "NZ-CHAT,true",
      "Asia/Choibalsan,true",
      "America/Porto_Acre,true",
      "Asia/Omsk,true",
      "Europe/Vaduz,true",
      "US/Michigan,true",
      "Asia/Dhaka,true",
      "America/Barbados,true",
      "Europe/Tiraspol,true",
      "Atlantic/Cape_Verde,true",
      "Asia/Yekaterinburg,true",
      "America/Louisville,true",
      "Pacific/Johnston,true",
      "Pacific/Chatham,true",
      "Europe/Ljubljana,true",
      "America/Sao_Paulo,true",
      "Asia/Jayapura,true",
      "America/Curacao,true",
      "Asia/Dushanbe,true",
      "America/Guyana,true",
      "America/Guayaquil,true",
      "America/Martinique,true",
      "Portugal,true",
      "Europe/Berlin,true",
      "Europe/Moscow,true",
      "Europe/Chisinau,true",
      "America/Puerto_Rico,true",
      "America/Rankin_Inlet,true",
      "Pacific/Ponape,true",
      "Europe/Stockholm,true",
      "Europe/Budapest,true",
      "America/Argentina/Jujuy,true",
      "Australia/Eucla,true",
      "Asia/Shanghai,true",
      "Universal,true",
      "Europe/Zagreb,true",
      "America/Port_of_Spain,true",
      "Europe/Helsinki,true",
      "Asia/Beirut,true",
      "Asia/Tel_Aviv,true",
      "Pacific/Bougainville,true",
      "US/Central,true",
      "Africa/Sao_Tome,true",
      "Indian/Chagos,true",
      "America/Cayenne,true",
      "Asia/Yakutsk,true",
      "Pacific/Galapagos,true",
      "Australia/North,true",
      "Europe/Paris,true",
      "Africa/Ndjamena,true",
      "Pacific/Fiji,true",
      "America/Rainy_River,true",
      "Indian/Maldives,true",
      "Australia/Yancowinna,true",
      "SystemV/AST4,true",
      "Asia/Oral,true",
      "America/Yellowknife,true",
      "Pacific/Enderbury,true",
      "America/Juneau,true",
      "Australia/Victoria,true",
      "America/Indiana/Vevay,true",
      "Asia/Tashkent,true",
      "Asia/Jakarta,true",
      "Africa/Ceuta,true",
      "Asia/Barnaul,true",
      "America/Recife,true",
      "America/Buenos_Aires,true",
      "America/Noronha,true",
      "America/Swift_Current,true",
      "Australia/Adelaide,true",
      "America/Metlakatla,true",
      "Africa/Djibouti,true",
      "America/Paramaribo,true",
      "Asia/Qostanay,true",
      "Europe/Simferopol,true",
      "Europe/Sofia,true",
      "Africa/Nouakchott,true",
      "Europe/Prague,true",
      "America/Indiana/Vincennes,true",
      "Antarctica/Mawson,true",
      "America/Kralendijk,true",
      "Antarctica/Troll,true",
      "Europe/Samara,true",
      "Indian/Christmas,true",
      "America/Antigua,true",
      "Pacific/Gambier,true",
      "America/Indianapolis,true",
      "America/Inuvik,true",
      "America/Iqaluit,true",
      "Pacific/Funafuti,true",
      "UTC,true",
      "Antarctica/Macquarie,true",
      "Canada/Pacific,true",
      "America/Moncton,true",
      "Africa/Gaborone,true",
      "Pacific/Chuuk,true",
      "Asia/Pyongyang,true",
      "America/St_Vincent,true",
      "Asia/Gaza,true",
      "Etc/Universal,true",
      "PST8PDT,true",
      "Atlantic/Faeroe,true",
      "Asia/Qyzylorda,true",
      "Canada/Newfoundland,true",
      "America/Kentucky/Louisville,true",
      "America/Yakutat,true",
      "America/Ciudad_Juarez,true",
      "Asia/Ho_Chi_Minh,true",
      "Antarctica/Casey,true",
      "Europe/Copenhagen,true",
      "Africa/Asmara,true",
      "Atlantic/Azores,true",
      "Europe/Vienna,true",
      "ROK,true",
      "Pacific/Pitcairn,true",
      "America/Mazatlan,true",
      "Australia/Queensland,true",
      "Pacific/Nauru,true",
      "Europe/Tirane,true",
      "Asia/Kolkata,true",
      "SystemV/MST7,true",
      "Australia/Canberra,true",
      "MET,true",
      "Australia/Broken_Hill,true",
      "Europe/Riga,true",
      "America/Dominica,true",
      "Africa/Abidjan,true",
      "America/Mendoza,true",
      "America/Santarem,true",
      "Kwajalein,true",
      "America/Asuncion,true",
      "Asia/Ulan_Bator,true",
      "NZ,true",
      "America/Boise,true",
      "Australia/Currie,true",
      "EST5EDT,true",
      "Pacific/Guam,true",
      "Pacific/Wake,true",
      "Atlantic/Bermuda,true",
      "America/Costa_Rica,true",
      "America/Dawson,true",
      "Asia/Chongqing,true",
      "Eire,true",
      "Europe/Amsterdam,true",
      "America/Indiana/Knox,true",
      "America/North_Dakota/Beulah,true",
      "Africa/Accra,true",
      "Atlantic/Faroe,true",
      "Mexico/BajaNorte,true",
      "America/Maceio,true",
      "Etc/UCT,true",
      "Pacific/Apia,true",
      "GMT0,true",
      "America/Atka,true",
      "Pacific/Niue,true",
      "Australia/Lord_Howe,true",
      "Europe/Dublin,true",
      "Pacific/Truk,true",
      "MST7MDT,true",
      "America/Monterrey,true",
      "America/Nassau,true",
      "America/Jamaica,true",
      "Asia/Bishkek,true",
      "America/Atikokan,true",
      "Atlantic/Stanley,true",
      "Australia/NSW,true",
      "US/Hawaii,true",
      "SystemV/CST6,true",
      "Indian/Mahe,true",
      "Asia/Aqtobe,true",
      "America/Sitka,true",
      "Asia/Vladivostok,true",
      "Africa/Libreville,true",
      "Africa/Maputo,true",
      "Zulu,true",
      "America/Kentucky/Monticello,true",
      "Africa/El_Aaiun,true",
      "Africa/Ouagadougou,true",
      "America/Coral_Harbour,true",
      "Pacific/Marquesas,true",
      "Brazil/West,true",
      "America/Aruba,true",
      "America/North_Dakota/Center,true",
      "America/Cayman,true",
      "Asia/Ulaanbaatar,true",
      "Asia/Baghdad,true",
      "Europe/San_Marino,true",
      "America/Indiana/Tell_City,true",
      "America/Tijuana,true",
      "Pacific/Saipan,true",
      "SystemV/YST9,true",
      "Africa/Douala,true",
      "America/Chihuahua,true",
      "America/Ojinaga,true",
      "Asia/Hovd,true",
      "America/Anchorage,true",
      "Chile/EasterIsland,true",
      "America/Halifax,true",
      "Antarctica/Rothera,true",
      "America/Indiana/Indianapolis,true",
      "US/Mountain,true",
      "Asia/Damascus,true",
      "America/Argentina/San_Luis,true",
      "America/Santiago,true",
      "Asia/Baku,true",
      "America/Argentina/Ushuaia,true",
      "Atlantic/Reykjavik,true",
      "Africa/Brazzaville,true",
      "Africa/Porto-Novo,true",
      "America/La_Paz,true",
      "Antarctica/DumontDUrville,true",
      "Asia/Taipei,true",
      "Antarctica/South_Pole,true",
      "Asia/Manila,true",
      "Asia/Bangkok,true",
      "Africa/Dar_es_Salaam,true",
      "Poland,true",
      "Atlantic/Madeira,true",
      "Antarctica/Palmer,true",
      "America/Thunder_Bay,true",
      "Africa/Addis_Ababa,true",
      "Asia/Yangon,true",
      "Europe/Uzhgorod,true",
      "Brazil/DeNoronha,true",
      "Asia/Ashkhabad,true",
      "Etc/Zulu,true",
      "America/Indiana/Marengo,true",
      "America/Creston,true",
      "America/Punta_Arenas,true",
      "America/Mexico_City,true",
      "Antarctica/Vostok,true",
      "Asia/Jerusalem,true",
      "Europe/Andorra,true",
      "US/Samoa,true",
      "PRC,true",
      "Asia/Vientiane,true",
      "Pacific/Kiritimati,true",
      "America/Matamoros,true",
      "America/Blanc-Sablon,true",
      "Asia/Riyadh,true",
      "Iceland,true",
      "Pacific/Pohnpei,true",
      "Asia/Ujung_Pandang,true",
      "Atlantic/South_Georgia,true",
      "Europe/Lisbon,true",
      "Asia/Harbin,true",
      "Europe/Oslo,true",
      "Asia/Novokuznetsk,true",
      "CST6CDT,true",
      "Atlantic/Canary,true",
      "America/Knox_IN,true",
      "Asia/Kuwait,true",
      "SystemV/HST10,true",
      "Pacific/Efate,true",
      "Africa/Lome,true",
      "America/Bogota,true",
      "America/Menominee,true",
      "America/Adak,true",
      "Pacific/Norfolk,true",
      "Europe/Kirov,true",
      "America/Resolute,true",
      "Pacific/Kanton,true",
      "Pacific/Tarawa,true",
      "Africa/Kampala,true",
      "Asia/Krasnoyarsk,true",
      "Greenwich,true",
      "SystemV/EST5,true",
      "America/Edmonton,true",
      "Europe/Podgorica,true",
      "Australia/South,true",
      "Canada/Central,true",
      "Africa/Bujumbura,true",
      "America/Santo_Domingo,true",
      "US/Eastern,true",
      "Europe/Minsk,true",
      "Pacific/Auckland,true",
      "Africa/Casablanca,true",
      "America/Glace_Bay,true",
      "Canada/Eastern,true",
      "Asia/Qatar,true",
      "Europe/Kiev,true",
      "Singapore,true",
      "Asia/Magadan,true",
      "SystemV/PST8,true",
      "America/Port-au-Prince,true",
      "Europe/Belfast,true",
      "America/St_Barthelemy,true",
      "Asia/Ashgabat,true",
      "Africa/Luanda,true",
      "America/Nipigon,true",
      "Atlantic/Jan_Mayen,true",
      "Brazil/Acre,true",
      "Asia/Muscat,true",
      "Asia/Bahrain,true",
      "Europe/Vilnius,true",
      "America/Fortaleza,true",
      "Etc/GMT0,true",
      "US/East-Indiana,true",
      "America/Hermosillo,true",
      "America/Cancun,true",
      "Africa/Maseru,true",
      "Pacific/Kosrae,true",
      "Africa/Kinshasa,true",
      "Asia/Kathmandu,true",
      "Asia/Seoul,true",
      "Australia/Sydney,true",
      "America/Lima,true",
      "Australia/LHI,true",
      "America/St_Lucia,true",
      "Europe/Madrid,true",
      "America/Bahia_Banderas,true",
      "America/Montserrat,true",
      "Asia/Brunei,true",
      "America/Santa_Isabel,true",
      "Canada/Mountain,true",
      "America/Cambridge_Bay,true",
      "Asia/Colombo,true",
      "Australia/West,true",
      "Indian/Antananarivo,true",
      "Australia/Brisbane,true",
      "Indian/Mayotte,true",
      "US/Indiana-Starke,true",
      "Asia/Urumqi,true",
      "US/Aleutian,true",
      "Europe/Volgograd,true",
      "America/Lower_Princes,true",
      "America/Vancouver,true",
      "Africa/Blantyre,true",
      "America/Rio_Branco,true",
      "America/Danmarkshavn,true",
      "America/Detroit,true",
      "America/Thule,true",
      "Africa/Lusaka,true",
      "Asia/Hong_Kong,true",
      "Iran,true",
      "America/Argentina/La_Rioja,true",
      "Africa/Dakar,true",
      "SystemV/CST6CDT,true",
      "America/Tortola,true",
      "America/Porto_Velho,true",
      "Asia/Sakhalin,true",
      "Etc/GMT+10,true",
      "America/Scoresbysund,true",
      "Asia/Kamchatka,true",
      "Asia/Thimbu,true",
      "Africa/Harare,true",
      "Etc/GMT+12,true",
      "Etc/GMT+11,true",
      "Navajo,true",
      "America/Nome,true",
      "Europe/Tallinn,true",
      "Turkey,true",
      "Africa/Khartoum,true",
      "Africa/Johannesburg,true",
      "Africa/Bangui,true",
      "Europe/Belgrade,true",
      "Jamaica,true",
      "Africa/Bissau,true",
      "Asia/Tehran,true",
      "WET,true",
      "Europe/Astrakhan,true",
      "Africa/Juba,true",
      "America/Campo_Grande,true",
      "America/Belem,true",
      "Etc/Greenwich,true",
      "Asia/Saigon,true",
      "America/Ensenada,true",
      "Pacific/Midway,true",
      "America/Jujuy,true",
      "Africa/Timbuktu,true",
      "America/Bahia,true",
      "America/Goose_Bay,true",
      "America/Virgin,true",
      "America/Pangnirtung,true",
      "Asia/Katmandu,true",
      "America/Phoenix,true",
      "Africa/Niamey,true",
      "America/Whitehorse,true",
      "Pacific/Noumea,true",
      "Asia/Tbilisi,true",
      "Europe/Kyiv,true",
      "America/Montreal,true",
      "Asia/Makassar,true",
      "America/Argentina/San_Juan,true",
      "Hongkong,true",
      "UCT,true",
      "Asia/Nicosia,true",
      "America/Indiana/Winamac,true",
      "SystemV/MST7MDT,true",
      "America/Argentina/ComodRivadavia,true",
      "America/Boa_Vista,true",
      "America/Grenada,true",
      "Asia/Atyrau,true",
      "Australia/Darwin,true",
      "Asia/Khandyga,true",
      "Asia/Kuala_Lumpur,true",
      "Asia/Famagusta,true",
      "Asia/Thimphu,true",
      "Asia/Rangoon,true",
      "Europe/Bratislava,true",
      "Asia/Calcutta,true",
      "America/Argentina/Tucuman,true",
      "Asia/Kabul,true",
      "Indian/Cocos,true",
      "Japan,true",
      "Pacific/Tongatapu,true",
      "America/New_York,true",
      "Etc/GMT-12,true",
      "Etc/GMT-11,true",
      "America/Nuuk,true",
      "Etc/GMT-10,true",
      "SystemV/YST9YDT,true",
      "Europe/Ulyanovsk,true",
      "Etc/GMT-14,true",
      "Etc/GMT-13,true",
      "W-SU,true",
      "America/Merida,true",
      "EET,true",
      "America/Rosario,true",
      "Canada/Saskatchewan,true",
      "America/St_Kitts,true",
      "Arctic/Longyearbyen,true",
      "America/Fort_Nelson,true",
      "America/Caracas,true",
      "America/Guadeloupe,true",
      "Asia/Hebron,true",
      "Indian/Kerguelen,true",
      "SystemV/PST8PDT,true",
      "Africa/Monrovia,true",
      "Asia/Ust-Nera,true",
      "Egypt,true",
      "Asia/Srednekolymsk,true",
      "America/North_Dakota/New_Salem,true",
      "Asia/Anadyr,true",
      "Australia/Melbourne,true",
      "Asia/Irkutsk,true",
      "America/Shiprock,true",
      "America/Winnipeg,true",
      "Europe/Vatican,true",
      "Asia/Amman,true",
      "Etc/UTC,true",
      "SystemV/AST4ADT,true",
      "Asia/Tokyo,true",
      "America/Toronto,true",
      "Asia/Singapore,true",
      "Australia/Lindeman,true",
      "America/Los_Angeles,true",
      "SystemV/EST5EDT,true",
      "Pacific/Majuro,true",
      "America/Argentina/Buenos_Aires,true",
      "Europe/Nicosia,true",
      "Pacific/Guadalcanal,true",
      "Europe/Athens,true",
      "US/Pacific,true",
      "Europe/Monaco,true",
      "Test/Invalid,false",
      ",false"
  })
  void testDateTimeZones(String zone, boolean expectedPass) {
    RuntimeException runtimeException = new RuntimeException();
    LocalDateTime result = ConversionUtils.localDateTimeFromMap(
        Map.of(
            "date", new ValueWithColumnNumber(Optional.of("1/1/24 01:02:03"), 1),
            "timeZone", new ValueWithColumnNumber(Optional.ofNullable(zone), 2)
        ),
        "TEST PROPERTY",
        DefaultTimeTranslator.builder()
            .time("date")
            .timeZone("timeZone")
            .build(),
        1,
        runtimeException
    );
    if (expectedPass) {
      assertNotNull(result);
      assertEquals(LocalDateTime.parse("2024-01-01T01:02:03"), result);
    } else {
      assertNull(result);
      
      if (StringUtils.isNotBlank(zone)) {
        FieldException fieldException = (FieldException) runtimeException.getSuppressed()[0];
        assertEquals("Time Zone", fieldException.getTargetProperty());
        assertEquals("Invalid time zone", fieldException.getMessage());
        assertEquals(1, fieldException.getRow());
        assertEquals(2, fieldException.getColumn());
        assertEquals("timeZone", fieldException.getProperty());
      }
    }
  }

  @Test
  void localDateTimeFromMap() {
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("test-property", new ValueWithColumnNumber(Optional.of("2024-01-01T01:00:00"), 1));
    map.put("test-zone", new ValueWithColumnNumber(Optional.of("UTC"), 2));

    LocalDateTime result = ConversionUtils.localDateTimeFromMap(map, "TEST PROPERTY", DefaultTimeTranslator.builder()
        .timeZone("test-zone")
        .time("test-property")
        .build(), 2, new RuntimeException());
    assertEquals(result, LocalDateTime.parse("2024-01-01T01:00:00"));

    RuntimeException runtimeException = new RuntimeException();
    map.put("test-property", new ValueWithColumnNumber(Optional.of("test"), 1));

    result = ConversionUtils.localDateTimeFromMap(map, "TEST PROPERTY", DefaultTimeTranslator.builder()
        .timeZone("test-zone")
        .time("test-property").build(), 2, runtimeException);
    assertNull(result);

    assertEquals(1, runtimeException.getSuppressed().length);
    Throwable throwable = runtimeException.getSuppressed()[0];
    assertInstanceOf(FieldException.class, throwable);
    FieldException fieldException = (FieldException) throwable;
    assertEquals("test-property", fieldException.getProperty());
    assertEquals("TEST PROPERTY", fieldException.getTargetProperty());
    assertEquals(1, fieldException.getColumn());
    assertEquals(2, fieldException.getRow());
    assertEquals("Invalid date time format", fieldException.getMessage());
    
    map.put("test-property", new ValueWithColumnNumber(Optional.of("2024-01-01"), 1));
    map.put("test-other-property", new ValueWithColumnNumber(Optional.of("12:00:00"), 2));

    runtimeException = new RuntimeException();
    result = ConversionUtils.localDateTimeFromMap(map, "TEST PROPERTY", new TimeTranslator() {
      @Override
      public String getTime() {
        return "test-property";
      }

      @Override
      public String getTimeZone() {
        return "test-zone-property";
      }
    }, 2, runtimeException);
    assertNull(result);
    assertEquals(0, runtimeException.getSuppressed().length);
    
    result = ConversionUtils.localDateTimeFromMap(map, "TEST PROPERTY", DateTimeSeparatedTimeTranslator.builder()
            .date("test-property")
            .time("test-other-property")
            .timeZone("test-zone")
        .build(), 2, new RuntimeException());
    
    assertEquals(LocalDateTime.parse("2024-01-01T12:00:00"), result);
    
    runtimeException = new RuntimeException();
    map.put("test-other-property", new ValueWithColumnNumber(Optional.of("-"), 2));
    result = ConversionUtils.localDateTimeFromMap(map, "TEST PROPERTY", DateTimeSeparatedTimeTranslator.builder()
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
    assertEquals("TEST PROPERTY", fieldException.getTargetProperty());
    assertEquals(2, fieldException.getColumn());
    assertEquals(2, fieldException.getRow());
    assertEquals("Invalid time format", fieldException.getMessage());

    runtimeException = new RuntimeException();
    map.remove("test-other-property");
    result = ConversionUtils.localDateTimeFromMap(map, "TEST PROPERTY", DateTimeSeparatedTimeTranslator.builder()
        .date("test-property")
        .time("test-other-property")
        .timeZone("test-zone")
        .build(), 2, runtimeException);
    assertEquals(LocalDateTime.parse("2024-01-01T00:00:00"), result);

    assertEquals(0, runtimeException.getSuppressed().length);

    runtimeException = new RuntimeException();
    map.remove("test-property");
    result = ConversionUtils.localDateTimeFromMap(map, "TEST PROPERTY", DateTimeSeparatedTimeTranslator.builder()
        .date("test-property")
        .time("test-other-property")
        .timeZone("test-zone")
        .build(), 2, runtimeException);
    assertNull(result);

    assertEquals(0, runtimeException.getSuppressed().length);

    result = ConversionUtils.localDateTimeFromMap(
        Map.of(
            "date", new ValueWithColumnNumber(Optional.of("inputDate"), 1),
            "timeZone", new ValueWithColumnNumber(Optional.of("UTC"), 2)
        ),
        "TEST PROPERTY",
        null,
        1,
        runtimeException
    );

    assertNull(result);

    result = ConversionUtils.localDateTimeFromMap(
        Map.of(
            "date", new ValueWithColumnNumber(Optional.of("inputDate"), 1),
            "timeZone", new ValueWithColumnNumber(Optional.of("UTC"), 2)
        ),
        "TEST PROPERTY",
        new TimeTranslator() {
          @Override
          public String getTime() {
            return "date";
          }

          @Override
          public String getTimeZone() {
            return "timeZone";
          }
        },
        1,
        runtimeException
    );
    assertNull(result);
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
  void testBlankStringFromProperty() {
    assertNull(
        ConversionUtils.stringFromProperty(
            new ValueWithColumnNumber(Optional.of(""), 1)
        )
    );
  }
  
  @Test
  void testPathListFromMap() {
    RuntimeException runtimeException = new RuntimeException();
    
    List<Path> result = ConversionUtils.pathListFromMap(
        Map.of( "propertyName", new ValueWithColumnNumber(Optional.of("path-one;path-two"), 1) ),
        "TEST_PROPERTY",
        "propertyName",
        1,
        runtimeException
    );
    
    assertEquals(
        Set.of(
            Path.of("path-one"), Path.of("path-two")
        ),
        new HashSet<>(result)
    );
    
    assertTrue(
        ConversionUtils.pathListFromMap(
            Collections.emptyMap(),
            "TEST_PROPERTY",
            "propertyName",
            1,
            runtimeException
        ).isEmpty()
    );
    
    assertTrue(
        ConversionUtils.pathListFromMap(
            Map.of( "propertyName", new ValueWithColumnNumber(Optional.of(""), 1) ),
            "TEST_PROPERTY",
            "propertyName",
            1,
            runtimeException
        ).isEmpty()
    );
  }
  
  @ParameterizedTest
  @CsvSource(value = {
      "150°35'21\"E,true",
      "150°35'21\"W,true",
      "150° 35' 21\" E,true",
      "150° 35' 21\" W,true",
      "150D35M21SE,true",
      "150D35M21SW,true",
      "150D 35M 21S E,true",
      "150D 35M 21S W,true",
      "150d35m21sE,true",
      "150d35m21sW,true",
      "150d 35m 21s E,true",
      "150d 35m 21s W,true",
      "150.589167,true",
      "-150.589167,true",
      "150°35.35'E,true",
      "150°35.35'W,true",
      "150° 35.35' E,true",
      "150° 35.35' W,true",
      "150D35.35ME,true",
      "150D35.35MW,true",
      "150D 35.35M E,true",
      "150D 35.35M W,true",
      "150d35.35mE,true",
      "150d35.35mW,true",
      "150d 35.35m E,true",
      "150d 35.35m W,true",
      ",false"
  })
  void testConvertDMSToDecimalDegreesLongitude(String dmsString, boolean expectedPass) {
    double expected = 150.589167;

    Double result = ConversionUtils.longitudeFromMap(
        Map.of("lon", new ValueWithColumnNumber(Optional.ofNullable(dmsString), 2)),
        "lon",
        1,
        new RuntimeException()
    );

    if (expectedPass) {
      assertEquals(
          (dmsString.endsWith("W") || dmsString.startsWith("-")) ? expected * -1 : expected,
          result
      );
    } else {
      assertNull(result);
    }
  }
  
  @ParameterizedTest
  @CsvSource(value = {
    "30°35'21\"N,true",
    "30°35'21\"S,true",
    "30° 35' 21\" N,true",
    "30° 35' 21\" S,true",
    "30D35M21SN,true",
    "30D35M21SS,true",
    "30D 35M 21S N,true",
    "30D 35M 21S S,true",
    "30d35m21sN,true",
    "30d35m21sS,true",
    "30d 35m 21s N,true",
    "30d 35m 21s S,true",
    "30.589167,true",
    "-30.589167,true",
    "30°35.35'N,true",
    "30°35.35'S,true",
    "30° 35.35' N,true",
    "30° 35.35' S,true",
    "30D35.35MN,true",
    "30D35.35MS,true",
    "30D 35.35M N,true",
    "30D 35.35M S,true",
    "30d35.35mN,true",
    "30d35.35mS,true",
    "30d 35.35m N,true",
    "30d 35.35m S,true",
    ",false"
  })
  void testConvertDMSToDecimalDegreesLatitude(String dmsString, boolean expectedPass) {
    double expected = 30.589167;
    
    Double result = ConversionUtils.latitudeFromMap(
        Map.of("lat", new ValueWithColumnNumber(Optional.ofNullable(dmsString), 2)),
        "lat",
        1,
        new RuntimeException()
    );
    
    if (expectedPass) {
      assertEquals(
          (dmsString.endsWith("S") || dmsString.startsWith("-")) ? expected * -1 : expected,
          result
      );
    } else {
      assertNull(result);
    }
  }
}