public class voter {
    private int studentId;
    private String name;
    private String password;
    private int deptId;
    private boolean isAdmin;
    private boolean hasVoted;
    private int votingYear;


public voter(){
    
}


    public voter(int studentId, String name, String password, int deptId, boolean isAdmin) {
        this.studentId = studentId;
        this.name = name;
        this.password = password;
        this.deptId = deptId;
        this.isAdmin = isAdmin;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean hasVoted() {
        return hasVoted;
    }


    
public String getDepartmentNameById(int deptId){
    switch(deptId){
        case 1: return "Computer Science";
        case 2: return "Electrical Engineering";
        case 3: return "Business Administration";
        default: return "Invalid Department id";
    }
}

    public void setVoteStatus(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }
    
    public int getVotingyear() {
        return votingYear;
    }

    public void setvotingYear(int votingYear) {
        this.votingYear = votingYear;
    }

}