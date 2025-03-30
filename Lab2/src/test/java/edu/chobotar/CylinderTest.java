package edu.chobotar;

import org.junit.Test;

import static junit.framework.TestCase.*;

/*
  @author Harsteel
  @project Lab2
  @class CylinderTest
  @version 1.0.0
  @since 30.03.2025 - 19.53
*/
public class CylinderTest {
    private final Cylinder cylinder = new Cylinder(3, 5);
    private static final double DELTA = 1e-10; // Допустима похибка для порівняння чисел з плаваючою комою

    @Test
    public void volumeMustBe141_37() {
        // Очікуваний об'єм: π * r² * h = π * 3² * 5 = π * 9 * 5 = 45π ≈ 141.37
        double expectedVolume = Math.PI * 9 * 5;
        assertEquals(expectedVolume, cylinder.calculateVolume(), DELTA);
    }

    @Test
    public void totalAreaMustBe150_80() {
        // Очікувана площа: 2πr² + 2πrh = 2π * 3² + 2π * 3 * 5 = 2π * 9 + 2π * 15 = 18π + 30π = 48π ≈ 150.80
        double expectedArea = 2 * Math.PI * 9 + 2 * Math.PI * 3 * 5;
        assertEquals(expectedArea, cylinder.calculateTotalSurfaceArea(), DELTA);
    }

    @Test
    public void lateralAreaMustBe94_25() {
        // Очікувана бічна площа: 2πrh = 2π * 3 * 5 = 30π ≈ 94.25
        double expectedLateralArea = 2 * Math.PI * 3 * 5;
        assertEquals(expectedLateralArea, cylinder.calculateLateralSurfaceArea(), DELTA);
    }

    @Test
    public void volumeAfterRadiusChangeMustBe251_33() {
        cylinder.setRadius(4.0);
        assertEquals(4.0, cylinder.getRadius(), DELTA);

        // Перевіряємо, що об'єм також змінився відповідно до нового радіусу
        // π * 4² * 5 = π * 16 * 5 = 80π ≈ 251.33
        double expectedVolume = Math.PI * 16 * 5;
        assertEquals(expectedVolume, cylinder.calculateVolume(), DELTA);
    }

    @Test
    public void ratioMustBe0_9375() {
        // Очікуване відношення = Об'єм / Площа = (π * 3² * 5) / (2π * 9 + 2π * 15) = 45π / 48π = 45/48 = 0.9375
        double expectedRatio = (Math.PI * 9 * 5) / (2 * Math.PI * 9 + 2 * Math.PI * 3 * 5);
        assertEquals(expectedRatio, cylinder.calculateVolumeToSurfaceRatio(), DELTA);
    }
}