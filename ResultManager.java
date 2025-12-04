import java.util.InputMismatchException;
import java.util.Scanner;

public class ResultManager {

    static class InvalidMarksException extends Exception {
        public InvalidMarksException(String message) {
            super(message);
        }
    }

   
    static class Student {
        private int rollNumber;
        private String studentName;
        private int[] marks;

        public Student(int rollNumber, String studentName, int[] marks) throws InvalidMarksException {
            this.rollNumber = rollNumber;
            this.studentName = studentName;
            this.marks = marks;
            validateMarks();
        }

        private void validateMarks() throws InvalidMarksException {
            if (marks == null || marks.length != 3) {
                throw new InvalidMarksException("Exactly 3 subjects are required.");
            }
            for (int i = 0; i < marks.length; i++) {
                int mark = marks[i];
                if (mark < 0 || mark > 100) {
                    throw new InvalidMarksException("Invalid marks in subject " + (i + 1) + ": " + mark + " (must be 0–100)");
                }
            }
        }

        public int getRollNumber() {
            return rollNumber;
        }

        public double calculateAverage() {
            int sum = 0;
            for (int mark : marks) sum += mark;
            return sum / (double) marks.length;
        }

        public void displayResult() {
            double avg = calculateAverage();
            String status = avg >= 40 ? "Pass" : "Fail";

            System.out.println("\n----- Student Result -----");
            System.out.println("Roll Number: " + rollNumber);
            System.out.println("Name: " + studentName);
            System.out.println("Marks: " + marks[0] + ", " + marks[1] + ", " + marks[2]);
            System.out.println("Average: " + String.format("%.2f", avg));
            System.out.println("Result: " + status);
        }
    }

    
    private Student[] students;
    private int count;
    private Scanner sc;

    public ResultManager(int size) {
        students = new Student[size];
        count = 0;
        sc = new Scanner(System.in);
    }

    public void addStudent() {
        try {
            if (count >= students.length) {
                System.out.println("Capacity full. Cannot add more students.");
                return;
            }

            System.out.print("Enter Roll Number: ");
            int roll = sc.nextInt();
            sc.nextLine(); 

            System.out.print("Enter Student Name: ");
            String name = sc.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty.");
                return;
            }

            int[] marks = new int[3];
            for (int i = 0; i < 3; i++) {
                System.out.print("Enter marks for subject " + (i + 1) + ": ");
                marks[i] = sc.nextInt();
            }

            students[count++] = new Student(roll, name, marks);
            System.out.println("Student added successfully!");
        } catch (InvalidMarksException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input type. Please enter numbers for roll and marks.");
            sc.nextLine(); 
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        } finally {
            System.out.println("Returning to main menu...");
        }
    }

    public void showStudentDetails() {
        try {
            System.out.print("Enter Roll Number to search: ");
            int roll = sc.nextInt();

            for (int i = 0; i < count; i++) {
                if (students[i] != null && students[i].getRollNumber() == roll) {
                    students[i].displayResult();
                    return;
                }
            }
            System.out.println("Student not found.");
        } catch (InputMismatchException e) {
            System.out.println("Error: Please enter a valid roll number.");
            sc.nextLine(); 
        }
    }

    public void mainMenu() {
        while (true) {
            System.out.println("\n===== Student Result Management System CLI =====");
            System.out.println("1. Add Student");
            System.out.println("2. Show Student Details");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            try {
                int choice = sc.nextInt();
                switch (choice) {
                    case 1: addStudent(); break;
                    case 2: showStudentDetails(); break;
                    case 3:
                        System.out.println("Exiting program. Thank you!");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a number (1-3).");
                sc.nextLine(); 
            }
        }
    }

    public static void main(String[] args) {
        ResultManager rm = new ResultManager(10); 
        rm.mainMenu();
    }
}
