import java.util.Scanner;

public class Program {

    private Calculator calc2;

    public void initial(String N){
        calc2 = new Calculator();
        calc2.setname(N);

    }

    public String getOperation(){
        //System.out.println("Welcome to the calculator designed by Lauren Engelbert and Isaac P");
        //System.out.println("Enter A to Add, S to Subtract, M to Multiply and Q to Quit");

        Scanner scan = new Scanner(System.in);
        String s = scan.next();
        return s;
    }

    public String getArgument(String A){

            System.out.println("Enter argument " + A);

            Scanner scan = new Scanner(System.in);
            String s = scan.next();
            return s;


    }

    public float handleOperation(float A, float B, String op){
        float result = -1000;
        if (op.equalsIgnoreCase("A")){
            result = calc2.addition( A, B);
        }
        else if (op.equalsIgnoreCase("S")){
            result = calc2.subtraction( A, B);
        }
        else if (op.equalsIgnoreCase("M")){
            result = calc2.multiplication(A , B);
        }
        return result;

    }

    public void printAnswer(float A, float B, float R, String op){
        String outcome = "";

        if (op.equalsIgnoreCase("A")){
            outcome = "sum";
        }
        else if (op.equalsIgnoreCase("S")){
            outcome = "difference";
        }
        else if (op.equalsIgnoreCase("M")){
            outcome = "product";
        }


        System.out.println("The " + outcome + " of " + A + " and " + B + " is " + R);
    }
}
