public class Main {
    public static void main(String[] args) {
        Term t = Preparser.parse("1+2+3^2^2*3");
        System.out.println(t.toString());
        System.out.println("result: " + t.calc());
    }
}
