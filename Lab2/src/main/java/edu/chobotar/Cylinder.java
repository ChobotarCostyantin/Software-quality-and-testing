package edu.chobotar;

/*
  @author Harsteel
  @project Lab2
  @class Cylinder
  @version 1.0.0
  @since 30.03.2025 - 19.40
*/
public class Cylinder {
    private double radius;
    private double height;

    public Cylinder(double radius, double height) {
        if (radius <= 0 || height <= 0) {
            throw new IllegalArgumentException("Радіус та висота повинні бути додатними числами");
        }
        this.radius = radius;
        this.height = height;
    }

    public double calculateVolume() {
        return Math.PI * radius * radius * height;
    }

    public double calculateTotalSurfaceArea() {
        // Площа повної поверхні = 2 * площа основи + площа бічної поверхні
        return 2 * Math.PI * radius * radius + 2 * Math.PI * radius * height;
    }

    public double calculateLateralSurfaceArea() {
        return 2 * Math.PI * radius * height;
    }

    public void setRadius(double newRadius) {
        if (newRadius <= 0) {
            throw new IllegalArgumentException("Радіус повинен бути додатним числом");
        }
        this.radius = newRadius;
    }

    public double calculateVolumeToSurfaceRatio() {
        double volume = calculateVolume();
        double surfaceArea = calculateTotalSurfaceArea();
        return volume / surfaceArea;
    }

    public double getRadius() {
        return radius;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double newHeight) {
        if (newHeight <= 0) {
            throw new IllegalArgumentException("Висота повинна бути додатним числом");
        }
        this.height = newHeight;
    }
}
