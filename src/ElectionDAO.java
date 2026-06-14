import java.sql.*;
import javax.swing.JComboBox;

public class ElectionDAO {

    // 1. Get the Active Election for the student's department
    public Election getActiveElection(int deptId) {
        String sql = "SELECT * FROM Election WHERE dept_id = ? AND status = 'active'";
        
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, deptId);
            
            ResultSet rs = pst.executeQuery();
            Election el = null;

            if (rs.next()) {
                el = new Election();
                el.setElectionId(rs.getInt("election_id"));
                el.setDeptId(rs.getInt("dept_id"));
                el.setStatus(rs.getString("status"));
                // Add any other attributes your Election class has (like title or year)
            }

            // Close manualy before returning
            rs.close();
            pst.close();
            con.close();

            return el;

        } catch (Exception e) {
            System.out.println("Error while fetching election: " + e.getMessage());
        }
        return null;
    }

    // 2. Populate a JComboBox with candidates (Avoids ArrayList)
    public void populateCandidateDropdown(int electionId, JComboBox<String> box) {
        String sql = "SELECT name FROM Candidate WHERE election_id = ?";
        
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, electionId);
            
            ResultSet rs = pst.executeQuery();

            // Logic for GUI: Clear existing items first
            box.removeAllItems();
            box.addItem("-- Select Candidate --");

            while (rs.next()) {
                // We add the string directly from the DB to the Swing component
                box.addItem(rs.getString("name"));
            }

            rs.close();
            pst.close();
            con.close();

        } catch (Exception e) {
            System.out.println("Error while loading candidates: " + e.getMessage());
        }
    }

    // 3. Find Candidate ID by Name (Needed for the final Vote insertion)
    public int getCandidateIdByName(String name, int electionId) {
        String sql = "SELECT candidate_id FROM Candidate WHERE name = ? AND election_id = ?";
        int id = -1;

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, name);
            pst.setInt(2, electionId);
            
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                id = rs.getInt("candidate_id");
            }

            rs.close();
            pst.close();
            con.close();

        } catch (Exception e) {
            System.out.println("Error finding candidate ID: " + e.getMessage());
        }
        return id;
    }

// Inside ElectionDAO.java
public boolean insertElection(int deptId, int year, String start, String end, String status) {
    String sql = "INSERT INTO Election (dept_id, voting_year, start_date, end_date, status) VALUES (?, ?, ?, ?, ?)";
    
    try {
        Connection con = DBConnection.getConnection();
        PreparedStatement pst = con.prepareStatement(sql);
        
        pst.setInt(1, deptId);
        pst.setInt(2, year);
        pst.setString(3, start);
        pst.setString(4, end);
        pst.setString(5, status);

        int result = pst.executeUpdate();

        pst.close();
        con.close();
        return result > 0;

    } catch (SQLException e) {
        // This catch block enforces the "Referential Rule"
        if (e.getErrorCode() == 1452) { 
            System.out.println("Constraint Error: Dept ID " + deptId + " does not exist!");
        } else {
            System.out.println("DB Error: " + e.getMessage());
        }
    } catch (Exception e) {
        System.out.println("General Error: " + e.getMessage());
    }
    return false;
}
}



     