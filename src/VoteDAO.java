import java.sql.*;

public class VoteDAO {

    // 1. Cast a Vote
    public boolean castVote(int studentId, int candidateId, int electionId) {
        String sql = "INSERT INTO Vote (student_id, candidate_id, election_id) VALUES (?, ?, ?)";
        
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setInt(1, studentId);
            pst.setInt(2, candidateId);
            pst.setInt(3, electionId);

            // executeUpdate returns the number of rows affected
            int result = pst.executeUpdate();

            // Close manually before returning
            pst.close();
            con.close();

            // If result is 1, the vote was saved successfully
            return result > 0;

        } catch (Exception e) {
            // If the user tries to vote twice, the DB Unique Key will trigger this error
            System.out.println("Error while casting vote: " + e.getMessage());
        }
        return false;
    }

    // 2. Get Vote Count for a Candidate (Helpful for Results/Admin)
    public int getCandidateVotes(int candidateId, int electionId) {
        String sql = "SELECT COUNT(*) AS total FROM Vote WHERE candidate_id = ? AND election_id = ?";
        int totalVotes = 0;

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, candidateId);
            pst.setInt(2, electionId);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                totalVotes = rs.getInt("total");
            }

            rs.close();
            pst.close();
            con.close();

            return totalVotes;

        } catch (Exception e) {
            System.out.println("Error while counting votes: " + e.getMessage());
        }
        return 0;
    }

    public String getTopThreeResults(int electionId) {
    // This query joins Vote and Candidate to get names and counts
    String sql = "SELECT c.name, COUNT(v.vote_id) AS vote_count " +
                 "FROM Candidate c " +
                 "LEFT JOIN Vote v ON c.candidate_id = v.candidate_id AND v.election_id = ? " +
                 "WHERE c.election_id = ? " +
                 "GROUP BY c.candidate_id, c.name " +
                 "ORDER BY vote_count DESC " +
                 "LIMIT 3";
    
    StringBuilder sb = new StringBuilder("--- Top 3 Leaderboard ---\n");
    try (Connection con = DBConnection.getConnection();
         PreparedStatement pst = con.prepareStatement(sql)) {
        
        pst.setInt(1, electionId);
        pst.setInt(2, electionId);
        ResultSet rs = pst.executeQuery();

        int rank = 1;
        while (rs.next()) {
            sb.append(rank).append(". ")
              .append(rs.getString("name"))
              .append(" : ")
              .append(rs.getInt("vote_count"))
              .append(" votes\n");
            rank++;
        }
        
        if (rank == 1) return "No candidates found for this election.";
        
    } catch (SQLException e) {
        return "Database Error: " + e.getMessage();
    }
    return sb.toString();
}

}