import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestAnswer {
	//any database can be added 
    private static final String DB_URL = "jdbc:mysql://localhost:3306/your_database_name";
    private static final String DB_USER = "your_database_user";
    private static final String DB_PASSWORD = "your_database_password";

    //Question 2
	public static List<String> transform_date_format(List<String> input) {
		List<String> output = new ArrayList<String>();
		for (String s : input) {
			if (s.length() ==10) { //make sure format is correct
			String day = "";
			String year = "";
			String month = "";
			if (s.contains("/")){
			if (s.charAt(2)=='/') {
				day = s.substring(0, 2);
				month = s.substring(3, 5);
				year = s.substring(6,10);
				}
			if (s.charAt(4)=='/') {
				day = s.substring(8, 10);
				month = s.substring(6, 8);
				year = s.substring(0,4);
			}}
			else if (s.charAt(2)=='-') {
				day = s.substring(3, 5);
				month = s.substring(0, 2);
				year = s.substring(6,10);
			}
			try {
			int dayV = Integer.parseInt(day);
			int monthV = Integer.parseInt(month);
			if (monthV>=1&&monthV<=12&&dayV>0&&dayV<=31) //verify that the numbers put in are actual dates
				output.add(year+month+day);
			}catch(NumberFormatException e){
				//handling any unknown date formats by not including them in output
			}
		}
		}
		return output;	
	}
	
	//question 2
	public boolean isValid(String s) {
		Stack<Character> x = new Stack<Character>();
		for(int i=0;i<s.length();i++) {
			char a = s.charAt(i);
			if (a=='('||a=='['||a=='{') {
				x.push(a);
			}
			else if(a==')'||a=='}'||a==']') {
				char b = (char)x.pop();
				if (a==')') {
					if (b!='(')
						return false;}
				if (a=='}') {
					if (b!='{')
						return false;}
				if (a==']') {
					if (b!='[')
						return false;}
			}
		}
		return x.isEmpty();
	}
	
	
	//Main method and query 
	public static void main(String[] args) {
        List<String> dates = List.of("2010/02/20", "19/12/2016", "11-18-2012", "20130720", "abcd/ef/ghij");
        List<String> transformedDates = transform_date_format(dates);
        System.out.println(transformedDates);
        
        
    // SQL query to get the required data
    String query = """
                        SELECT
                            c.name AS college_name,
                            MIN(r.ranking) AS best_ranking,
                            COUNT(*) AS number_of_students
                        FROM
                            colleges c
                        JOIN
                            students s ON c.id = s.collegeId
                        JOIN
                            rankings r ON s.id = r.studentId
                        WHERE
                            r.year = 2015
                            AND r.ranking BETWEEN 1 AND 3
                        GROUP BY
                            c.name
                        HAVING
                            COUNT(*) > 0
                        ORDER BY
                            best_ranking ASC;
                        """;

        // Try-with-resources block to ensure resources are closed properly
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                     PreparedStatement preparedStatement = connection.prepareStatement(query);
                     ResultSet resultSet = preparedStatement.executeQuery()) {

        // Process the result set
            while (resultSet.next()) {
            	String collegeName = resultSet.getString("college_name");
                int bestRanking = resultSet.getInt("best_ranking");
                int numberOfStudents = resultSet.getInt("number_of_students");

       // Print the results
            System.out.printf("College: %s, Best Ranking: %d, Number of Students: %d%n",
                collegeName, bestRanking, numberOfStudents);
                    }
            } catch (SQLException e) {
                    // Print any SQL exceptions that occur
                    e.printStackTrace();
            }
            }
    }
