public class Preparser {
    public Preparser() {

    }

    static Term parse(String input) {
        Operation term = null, current = null;
        int i = 0;
        while (i < input.length()) {
            String tmp = "";
            if (input.charAt(i) == '(') {
                // int parenthese_level = 1
                // while (i < input.length() parenthese_level > 0){
                // tmp += input
                // }

            } else {
                while (i < input.length() && Character.isDigit(input.charAt(i))) {
                    tmp += input.charAt(i);
                    i++;
                }
            }
            Number number = new Number(Integer.parseInt(tmp));
            if (input.length() == i) {
                if (term == null)
                    return number;
                else {
                    current.termRight = number;
                    term.sort();
                    return term;
                }
            }
            Operator operator = Operator.identify("" + input.charAt(i++));
            Operation newOp = new Operation(operator, number, null);
            if (term == null) {
                term = newOp;
                current = term;
            } else {
                current.termRight = newOp;
                current = newOp;
            }
        }
        return null;
    }
}