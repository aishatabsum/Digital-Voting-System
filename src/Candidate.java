public class Candidate {
    private int candidateId;
    private String studentId;
    private int deptId;
    private String position;
    private int electionId;

    public Candidate(){
        
    }
    public Candidate(int candidateId, String studentId, int deptId, String position, int electionId) {
        this.candidateId = candidateId;
        this.studentId = studentId;
        this.deptId = deptId;
        this.position = position;
        this.electionId = electionId;
    }

    public int getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getElectionId() {
        return electionId;
    }

    public void setElectionId(int electionId) {
        this.electionId = electionId;
    }
}