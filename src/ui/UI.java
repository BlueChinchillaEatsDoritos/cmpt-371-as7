package ui;

import java.util.Scanner;
import java.sql.*;

import static java.lang.Integer.parseInt;

public class UI {

    public static void main(String[] args) {
        Boolean continueProgram = true;

        String methodSelection;
        int methodSelected;
        int confirmation;
        Scanner keyboardListener = new Scanner(System.in);

        System.out.println("Airline Database");

        while (continueProgram) {
            System.out.println("To select a command, type the number and hit ENTER.\n");
            System.out.println("[1] Create new Passenger Profile");
            System.out.println("[2] View booked passengers on a flight");
            System.out.println("[3] Create a passenger booking");
            System.out.println("[4] Exit.\n");

            methodSelected = methodSelector(keyboardListener);

            if (methodSelected == 1) {
                addProfile(keyboardListener);
            } else if (methodSelected == 2) {
                System.exit(0);
            } else if (methodSelected == 3) {
                addBooking(keyboardListener);
            } else if (methodSelected == 4) {
                while (true) {
                    System.out.print("Are you sure you want to exit? [Y/N]  ");
                    confirmation = ynHandler(keyboardListener);
                    if (confirmation == 1) {
                        System.exit(0);
                    }
                    if (confirmation == 0) {
                        System.out.println();
                        break;
                    }
                }
            } else {
                System.out.println(("Oops! Command not recognized!\n"));
            }
        }
    }

    static int ynHandler(Scanner listener) {
        char confirmation;
        String keyboardInput = listener.nextLine().toLowerCase();
        if (!keyboardInput.isEmpty()) {
            confirmation = keyboardInput.charAt(0);
            if (confirmation == 'y') {
                return 1;
            }
            else if(confirmation == 'n') {
                return 0;
            }
        }
        return -1;
    }

    static void addProfile(Scanner listener) {
        String keyboardInput = " ";
        int confirmation;

        Boolean enterDetails = true;
        Boolean confirmed = false;

        int passengerID = getPassengerID();
        /*if (passengerID == 0){
            System.out.println("An unrecoverable error occurred with the connection to the server! Request aborted.");
            return;
        }*/

        int miles = 0;
        String lastName = " ";
        String firstName = " ";

        String sqlCommand = "INSERT INTO Passenger\nVALUES";

        while (enterDetails) {
            System.out.println("Please enter the first name of the new passenger.");
            firstName = listener.nextLine();
            System.out.println("Please enter the last name of the new passenger.");
            lastName = listener.nextLine();
            while (!confirmed) {
                System.out.println("Confirm new passenger is " + firstName + " " + lastName + "? [Y/N}");
                confirmation = ynHandler(listener);
                if (confirmation == 1) {
                    sqlCommand = sqlCommand + "(" + passengerID +", ' + firstName" + "', '" + lastName + "', 0);";
                    System.out.println("\nSQL Command: \n" + sqlCommand + "\n" );

                    System.out.println("Passenger " + firstName + " " + lastName + " created.\nPassenger ID is "
                            + passengerID + "\nreturning to menu...\n\n");
                    return;
                }
                if (confirmation == 0) {
                    System.out.println();
                    break;
                }
            }
        }
    }

    static int getPassengerID(){
        String connectionUrl = "jdbc:odbc:myDSN";
        String temp="";
        Connection con;
        ResultSet rs;
        PreparedStatement pstmt = null;
        String sqlStatement = "SELECT MAX(passenger_id) AS maxID\nFROM Passenger";
        int returnValue = 0;
        try
        {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        }catch(ClassNotFoundException ce)
        {
            System.out.println("\nNo JDBC-ODBC bridge; exit now.\n");
            return 0;
        }

        try
        {
            con = DriverManager.getConnection(connectionUrl,"gerland","Njf6m437P66FG62F");
        }catch (SQLException se)
        {
            System.out.println("\nNo proper DSN; exit now.\n");
            return 0;
        }
        try
        {
            pstmt = con.prepareStatement(sqlStatement);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                temp= rs.getString("maxID");	//the table has a field 'username'
                returnValue = parseInt(temp) + 1;
            }
            rs.close();
            return returnValue;
        }catch (SQLException se)
        {
            System.out.println("\nSQL Exception occured, the state : "+
                    se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
            return 0;
        }
    }

    static void sqlQueryConnector(String sqlStatement){
        String connectionUrl = "jdbc:odbc:myDSN";
        String temp="";
        Connection con;
        ResultSet rs;
        PreparedStatement pstmt = null;
        int returnValue = 0;
        try
        {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        }catch(ClassNotFoundException ce)
        {
            System.out.println("\nNo JDBC-ODBC bridge; exit now.\n");
            return;
        }

        try
        {
            con = DriverManager.getConnection(connectionUrl,"","");
        }catch (SQLException se)
        {
            System.out.println("\nNo proper DSN; exit now.\n");
            return;
        }
        try
        {
            pstmt = con.prepareStatement(sqlStatement);
            rs = pstmt.executeQuery();

            return;
        }catch (SQLException se)
        {
            System.out.println("\nSQL Exception occurred, the state : "+
                    se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
            return;
        }
    }

    static void addBooking(Scanner listener) {
        String date = "Error";
        String flight = "Error";
        System.out.println("Add Booking:");

        Boolean firstFlightComplete = false;
        Boolean confirmation = false;

        while(!firstFlightComplete) {
            Boolean finishedBooking = false;
            while (!finishedBooking) {
                System.out.println("Please enter the flight you wish to take");
                flight = listener.nextLine();
                // Print out list query of Flight
                break;
            }

            Boolean validDate = false;
            while (!validDate) {
                date = askDate(listener);
                if (dateChecker(date)) {
                    break;
                }
            }

            System.out.println("Flight Details:\nFlight Code: " + flight
                    + "\nDepart Date: " + date);

            int yn;
            while(!confirmation) {
                System.out.println("\nIs this correct? [Y/N]");
                yn = ynHandler(listener);
                if (yn == 1) {
                    firstFlightComplete = true;
                    break;
                }
                if (yn == 0) {
                    break;
                }
            }
        }

        Boolean decideRT = false;
        int RTselection;

        while(!decideRT) {
            System.out.println("Do you wish to add... ");
            System.out.println("[1] Return flight.");
            System.out.println("[2] A second leg to another city.");
            System.out.println("[3] None.");
            RTselection = methodSelector(listener);

            if(RTselection == 1){
                // Return flight
            }
            else if(RTselection == 2) {
                // Second leg handle
            }
            else if(RTselection == 3) {
                break;
            }
        }
    }


    static Boolean dateComparer(String date1, String date2) {
        int comp1;
        int comp2;
        // Compare year
        if (Integer.parseInt(date1.substring(0,4)) <=
                Integer.parseInt(date2.substring(0,4))) {
            // Compare month
            if(Integer.parseInt(date1.substring(5,7)) <=
                    Integer.parseInt(date2.substring(5,7))){
                // Compare date
                if(Integer.parseInt(date1.substring(8)) <=
                        Integer.parseInt(date2.substring(8))) {
                    return true;
                }
            }
        }
        return false;
    }

    static String askDate(Scanner listener) {
        String date;
        Boolean validDate = false;
        while (!validDate) {
            System.out.println("Please enter the date you wish to depart. Date should be in (YYYY-MM-DD).");
            date = listener.nextLine();
            if (dateChecker(date)) {
                return date;
            }
        }
        // Placeholder. Should never reach this point.
        return null;
    }

    static Boolean dateChecker(String date1) {
        String temp;
        int tempNumber;
        // Test format of string is valid (YYYY-MM-DD)
        if (date1.charAt(4) == '-' && date1.charAt(7) == '-') {
            temp = date1.substring(0,4);
            // Test each section of that date to make sure it's a valid number.
            try {
                tempNumber = Integer.parseInt(temp);
                temp = date1.substring(5,7);
                tempNumber = Integer.parseInt(temp);
                temp = date1.substring(8);
                tempNumber = Integer.parseInt(temp);
                // If any number is valid, exception should be thrown by this point.
                return true;
            }
            catch(NumberFormatException e){
                return false;
            }
        }
        return false;
    }

    static int methodSelector(Scanner listener) {
        String methodSelection = listener.nextLine().toLowerCase();
        if (methodSelection.length() == 1) {
            methodSelection = methodSelection.substring(0, 1);
            return Integer.parseInt(methodSelection);
        }
        return -1;
    }
}
