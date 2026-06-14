public class Vote {
    private int voteId;
    private String studentId;
    private int candidateId;
    private int electionId;
    private String votedAt;


    public Vote(){
        
    }
    public Vote(int voteId, String studentId, int candidateId, int electionId, String votedAt) {
        this.voteId = voteId;
        this.studentId = studentId;
        this.candidateId = candidateId;
        this.electionId = electionId;
        this.votedAt = votedAt;
    }

    public int getVoteId() {
        return voteId;
    }

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
    }

    public int getElectionId() {
        return electionId;
    }

    public void setElectionId(int electionId) {
        this.electionId = electionId;
    }

    public String getVotedAt() {
        return votedAt;
    }

    public void setVotedAt(String votedAt) {
        this.votedAt = votedAt;
    }
}