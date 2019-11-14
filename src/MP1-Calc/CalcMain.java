public class CalcMain {
    public static void main(String[] args)
    {
            //test();
            Program prog = new Program();
            prog.initial("Group?");

            System.out.println("Welcome to the calculator designed by Lauren Engelbert and Isaac P");
            System.out.println("Enter A to Add, S to Subtract, M to Multiply and Q to Quit");

            while (true) {
                try {
                String op = prog.getOperation();
                System.out.println("Operation: " + op);
                if (op.equalsIgnoreCase("Q")) {
                    break;
                } else if (op.equalsIgnoreCase("A") || op.equalsIgnoreCase("S") || op.equalsIgnoreCase("M")) {
                    String arg1 = prog.getArgument("1");
                    String arg2 = prog.getArgument("2");

                    float res = prog.handleOperation(Float.parseFloat(arg1.trim()), Float.parseFloat(arg2.trim()), op);

                    prog.printAnswer(Float.parseFloat(arg1.trim()), Float.parseFloat(arg2.trim()), res, op);

                    System.out.println("To do another calculation, enter A for addition, S for subtraction or M for multiplication. To quit, enter Q.");
                } else {
                    System.out.println("That is not a valid operation. Please enter A, S, M or Q.");
                }
                }
                catch (Exception e){
                    System.out.println("That was not a valid argument. You must enter a number");
                    System.out.println("Enter A to Add, S to Subtract, M to Multiply and Q to Quit");
                }
        }

    }

    public static void test(){
        Calculator calc1 = new Calculator();
        Float addresult = calc1.addition(3.0f , 5.0f);
        System.out.println("Add Result: " + addresult);

        Float subresult = calc1.subtraction(3.0f , 5.0f);
        System.out.println("Subtraction Result: " + subresult);

        Float multresult = calc1.multiplication(3.0f , 5.0f);
        System.out.println("Multiplication Result: " + multresult);

        calc1.setname("group number");
        System.out.println("Name Result: " + calc1.getname());
    }
}
