import java.sql.*;
public class VoterDAO {
        
public voter login (int id, String password){
    Connection con=null;
    PreparedStatement pst=null;
    ResultSet rs=null;
String sql= "SELECT * FROM Voters where student_id = ? and password = ?";

try{
     con= DBConnection.getConnection();
     pst= con.prepareStatement(sql);

 voter v=null;
    pst.setInt(1,id);
    pst.setString(2,password);
     rs = pst.executeQuery();
    if(rs.next()){
     v= new voter();
   v.setStudentId(rs.getInt("student_id"));
  v.setName(rs.getString("name"));
  v.setDeptId(rs.getInt("dept_id"));
  v.setIsAdmin(rs.getBoolean("is_admin"));
 
  return v;
}
        rs.close(); 
        pst.close();
        con.close();

} catch(Exception e){
    System.out.println("Error while checking login: "+ e.getMessage());
}

return null;
}





// 2. CHECK IF ALREADY VOTED (To disable vote button)
    public boolean hasVoted(int studentId, int electionId) {
          Connection con=null;
    PreparedStatement pst=null;
    ResultSet rs=null;
        String sql = "SELECT * FROM Vote WHERE student_id = ? AND election_id = ?";
        try {
             con = DBConnection.getConnection();
              pst = con.prepareStatement(sql);
            
            pst.setInt(1, studentId);
            pst.setInt(2, electionId);

             rs = pst.executeQuery();
            boolean exists = rs.next();
             rs.close(); 
            pst.close();
            con.close();
            return exists; // Returns true if record exists

        
        } catch (Exception e) {
            System.out.println("Vote Check Error: " + e.getMessage());
        }
        return false;
    }
}





