package edu.chobotar;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
  @author Harsteel
  @project Lab4
  @class RomanToArabicConverterTest
  @version 1.0.0
  @since 13.04.2025 - 20.51
*/
class RomanToArabicConverterTest {
    @Test
    void whenInputNullThenThrowsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> RomanToArabicConverter.romanToArabic(null));
    }

    @Test
    void whenInputBlankThenThrowsIllegalArgumentException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> RomanToArabicConverter.romanToArabic(" "));
        assertTrue(thrown.getMessage().contains(" cannot be converted to a Roman Numeral"));
    }

    @Test
    void whenInputCyrillicIThenThrowsIllegalArgumentException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> RomanToArabicConverter.romanToArabic("І"));
        assertTrue(thrown.getMessage().contains("І cannot be converted to a Roman Numeral"));
    }

    @Test
    void whenInputCyrillicCThenThrowsIllegalArgumentException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> RomanToArabicConverter.romanToArabic("С"));
        assertTrue(thrown.getMessage().contains("С cannot be converted to a Roman Numeral"));
    }

    @Test
    void whenInputCyrillicXThenThrowsIllegalArgumentException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> RomanToArabicConverter.romanToArabic("Х"));
        assertTrue(thrown.getMessage().contains("Х cannot be converted to a Roman Numeral"));
    }

    @Test
    void whenInputCyrillicMThenThrowsIllegalArgumentException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> RomanToArabicConverter.romanToArabic("М"));
        assertTrue(thrown.getMessage().contains("М cannot be converted to a Roman Numeral"));
    }

    @Test
    void whenInputLCThenThrowsIllegalArgumentException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> RomanToArabicConverter.romanToArabic("LC"));
        assertTrue(thrown.getMessage().contains("LC cannot be converted to a Roman Numeral"));
    }

    @Test
    void whenInputDMThenThrowsIllegalArgumentException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> RomanToArabicConverter.romanToArabic("DM"));
        assertTrue(thrown.getMessage().contains("DM cannot be converted to a Roman Numeral"));
    }

    @Test
    void whenInputXMThenThrowsIllegalArgumentException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> RomanToArabicConverter.romanToArabic("XM"));
        assertTrue(thrown.getMessage().contains("XM cannot be converted to a Roman Numeral"));
    }

    @Test
    void whenInputXDThenThrowsIllegalArgumentException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> RomanToArabicConverter.romanToArabic("XD"));
        assertTrue(thrown.getMessage().contains("XD cannot be converted to a Roman Numeral"));
    }
}