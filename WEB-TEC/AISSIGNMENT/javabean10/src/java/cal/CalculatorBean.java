/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author VIRAJ
 */

package cal;

public class CalculatorBean {

    private double operand1;
    private double operand2;
    private String operation;
    private double result;

    public void setOperand1(double operand1) {
        this.operand1 = operand1;
    }

    public void setOperand2(double operand2) {
        this.operand2 = operand2;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public double getResult() {

        switch (operation) {
            case "add":
                result = operand1 + operand2;
                break;
            case "sub":
                result = operand1 - operand2;
                break;
            case "mul":
                result = operand1 * operand2;
                break;
            case "div":
                result = operand1 / operand2;
                break;
            default:
                break;
        }

        return result;
    }
}

