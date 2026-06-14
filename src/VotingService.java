

public class VotingService {
    private VoterDAO voterDao = new VoterDAO();
    private ElectionDAO electionDao = new ElectionDAO();
    private VoteDAO voteDao = new VoteDAO();


public String castStudentVote(int sid, int eid, String cand_name){
    if (voterDao.hasVoted(sid,eid)) {
            return "You have already voted in this election!";
        }
int cid= electionDao.getCandidateIdByName(cand_name, eid);
if(cid == -1){
    System.out.println("Invalid candidate selected!");
}
 
boolean success= voteDao.castVote(sid, eid, cid);
if(success){
   return "Vote casted successfully!";
}else
    return "Database error: vote can not be saved successfully!"; 
}


  public String adminCreateElection(voter user, int deptId, int year, String start, String end) {
    // 1. Verify Role
    if (!user.isAdmin()) {
        return "Access Denied: You are not an admin.";
    }

    // 2. Call the DAO
    boolean success = electionDao.insertElection(deptId, year, start, end, "active");
    
    return success ? "Election Created Successfully!" : "Error: Check Dept ID or DB Connection.";
}
  
 public String viewLiveResults(voter user, Election currentElection) {
    // 1. Role Check
    if (!user.isAdmin()) {
        return "Access Denied: Admin only.";
    }

    // 2. Status Logic
    String status = currentElection.getStatus().toLowerCase();
    
    if (status.equals("upcoming")) {
        return "Results unavailable: This election has not started yet.";
    }

    // 3. Fetch from DAO
    return voteDao.getTopThreeResults(currentElection.getElectionId());
}
}