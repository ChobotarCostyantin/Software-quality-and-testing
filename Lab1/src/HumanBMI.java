/**
 * Class for calculating person's body mass index (IMT)
 */
public class HumanBMI {
    // Constants for BMI threshold values
    private static final double UNDERWEIGHT_LIMIT = 18.5;
    private static final double NORMAL_LIMIT = 25.0;
    private static final double OVERWEIGHT_LIMIT = 30.0;

    private double weight; // Person's weight in kg
    private double height; // Person's height in meters
    private double bmi; // Calculated BMI value

    /**
     * Constructor.
     *
     * @param weight Person's weight in kilograms.
     * @param height Person's height in meters.
     * @throws IllegalArgumentException if the weight or height values is invalid
     */
    public HumanBMI(double weight, double height) {
        validateParameters(weight, height);
        this.weight = weight;
        this.height = height;
        calculateBMI();
    }

    /**
     * Returns the calculated BMI value
     *
     * @return Person's BMI value
     */
    public double getBMI() {
        return bmi;
    }

    /**
     * Returns the person's weight
     *
     * @return Weight in kilograms.
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Sets a new weight value.
     *
     * @param weight New weight in kilograms.
     * @throws IllegalArgumentException if the weight or height values is invalid
     */
    public void setWeight(double weight) {
        validateParameters(this.weight, height);
        this.weight = weight;
        calculateBMI();
    }

    /**
     * Returns the person's height
     *
     * @return Height in meters.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Sets a new weight value.
     *
     * @param height New weight in kilograms.
     * @throws IllegalArgumentException if the weight or height values is invalid
     */
    public void setHeight(double height) {
        validateParameters(weight, this.height);
        this.height = height;
        calculateBMI();
    }

    /**
     * Validates input parameters.
     *
     * @param weight Person's weight.
     * @param height Person's height.
     * @throws IllegalArgumentException if the weight or height values are invalid
     */
    private void validateParameters(double weight, double height) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be a positive number");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be a positive number");
        }
    }

    /**
     * Calculates person's body mass index.
     */
    private void calculateBMI() {
        bmi = weight / (height * height);
    }

    /**
     * Returns textual BMI interpretation.
     *
     * @return BMI interpretation
     */
    public String getResult() {
        if (bmi < UNDERWEIGHT_LIMIT) {
            return String.format("Underweight (BMI: %.2f)", bmi);
        } else if (bmi < NORMAL_LIMIT) {
            return String.format("Normal weight (BMI: %.2f)", bmi);
        } else if (bmi < OVERWEIGHT_LIMIT) {
            return String.format("Overweight! (BMI: %.2f)", bmi);
        } else {
            return String.format("Obesity (BMI: %.2f)", bmi);
        }
    }
}
