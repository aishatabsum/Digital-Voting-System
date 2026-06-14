public class testJar {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println(" JAR is loaded! Ready to connect to MySQL.");
        } catch (ClassNotFoundException e) {
            System.out.println(" JAR not found in lib/ folder");
        }

        if (DBConnection.getConnection() != null) {
            System.out.println("YOU ARE READY FOR DATABASE WORK!");
        } else {
            System.out.println("STOP: Check your password or MySQL service.");
        }
    

    }



}