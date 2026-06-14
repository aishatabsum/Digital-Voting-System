public class Election {
    private int electionId;
    private int deptId;
    private int votingYear;
    private String status;

    public Election(){
        
    }
    public Election(int electionId, int deptId, int votingYear, String status) {
        this.electionId = electionId;
        this.deptId = deptId;
        this.votingYear = votingYear;
        this.status = status;
    }

    public int getElectionId() {
        return electionId;
    }

    public void setElectionId(int electionId) {
        this.electionId = electionId;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public int getVotingYear() {
        return votingYear;
    }

    public void setVotingYear(int votingYear) {
        this.votingYear = votingYear;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}