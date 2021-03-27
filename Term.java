public interface Term {
    public double calc();

    public String toString();

    public void sort();
}

class Number implements Term {
    double number;

    public Number(double n) {
        number = n;
    }

    public double calc() {
        return number;
    }

    public String toString() {
        return String.valueOf(number);
    }

    public void sort() {
        return;
    }
}

enum ParseDirection {
    LEFT_TO_RIGHT, RIGHT_TO_LEFT
}

enum Precedence {
    PADD(1, ParseDirection.LEFT_TO_RIGHT), PMULT(2, ParseDirection.LEFT_TO_RIGHT),
    PEXP(3, ParseDirection.RIGHT_TO_LEFT);

    int level;
    ParseDirection parseDirection;

    Precedence(int level, ParseDirection parseDirection) {
        this.level = level;
        this.parseDirection = parseDirection;
    }
}

enum Operator {
    Addition("+", Precedence.PADD) {
        public double apply(double termLeft, double termRight) {
            return termLeft + termRight;
        }
    },
    Substraction("-", Precedence.PADD) {
        public double apply(double termLeft, double termRight) {
            return termLeft - termRight;
        }
    },
    Multiplication("*", Precedence.PMULT) {

        public double apply(double termLeft, double termRight) {
            return termLeft * termRight;
        }
    },
    Division("/", Precedence.PMULT) {

        public double apply(double termLeft, double termRight) {
            return termLeft / termRight;
        }
    },
    Exponentiation("^", Precedence.PEXP) {
        public double apply(double termLeft, double termRight) {
            return Math.pow(termLeft, termRight);
        }
    };

    String operatorString;
    Precedence precedence; // true: left-right; false: right-left

    Operator(String s, Precedence precedence) {
        this.operatorString = s;
        this.precedence = precedence;
    }

    static Operator identify(String s) {
        for (Operator i : values()) {
            if (i.operatorString.equals(s))
                return i;
        }
        return null;
    }

    abstract double apply(double termLeft, double termRight);
}

class Operation implements Term {
    Term termLeft, termRight;
    Operator operator;

    public Operation(Operator op, Term tl, Term tr) {
        operator = op;
        termLeft = tl;
        termRight = tr;
    }

    public void setLeft(Term t) {
        termLeft = t;
    }

    public void setRight(Term t) {
        termRight = t;
    }

    public double calc() {
        return operator.apply(termLeft.calc(), termRight.calc());
    }

    public String toString() {
        return termLeft.toString() + operator.operatorString + termRight.toString();
    }

    // creates a tree, according to the precedences of the operations
    public void sort() {
        Operation t = this, lowest = this, prev = this, parent = this;
        // find lowest precedenced operation
        while (t.termRight instanceof Operation) {
            t = (Operation) t.termRight;
            if (t.operator.precedence.level < lowest.operator.precedence.level
                    || (t.operator.precedence.parseDirection == ParseDirection.RIGHT_TO_LEFT
                            && t.operator.precedence.level == lowest.operator.precedence.level)) {
                lowest = t;
                parent = prev;
            }
            prev = t;
        }

        if (lowest != this) {
            // swap the lowest precendence operator to treehead
            Operation newLeft = new Operation(operator, termLeft, termRight);

            // parent.termRight = lowest.termLeft;
            Operation pt = newLeft;
            while (pt.termRight != lowest) {
                pt = (Operation) pt.termRight;
            }
            pt.termRight = ((Operation) pt.termRight).termLeft;

            this.operator = lowest.operator;
            this.termLeft = newLeft;
            this.termRight = lowest.termRight;
        }
        this.termLeft.sort();
        this.termRight.sort();
    }
}

class ParentheseTerm implements Term {
    Term term;

    public ParentheseTerm(Term t) {
        term = t;
    }

    public String toString() {
        return "(" + term.toString() + ")";
    }

    public double calc() {
        return term.calc();
    }

    public void sort() {
        term.sort();
    }
}