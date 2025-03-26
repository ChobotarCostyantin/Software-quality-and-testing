public class Main {
    public static void main(String[] args) {
        HumanBMI humanBMI = new HumanBMI(80, 1.52);
        System.out.printf("Height: %.2f meters%n", humanBMI.getHeight());
        System.out.printf("Weight: %.2f kilograms%n", humanBMI.getWeight());
        System.out.printf("Result: %s%n", humanBMI.getResult());
    }
}