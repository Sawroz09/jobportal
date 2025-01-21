public class Session1 {
    private static Session1 instance;
    private String userName;
    private String caname;
    private String recruiterUsername;
    private int userId;
    private int RecruiterId;


    private Session1() {
        // Private constructor to restrict instantiation
    }

    public static Session1 getInstance() {
        if (instance == null) {
            instance = new Session1();
        }
        return instance;
    }

    // Getters and setters for userName, caname, recruiterUsername, and userId
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCanName() {
        return caname;
    }


    public void setCanName(String caname) {
        this.caname = caname;
    }

    public String getRecruiterUsername() {
        return recruiterUsername;
    }

    public void setRecruiterUsername(String recruiterUsername) {
        this.recruiterUsername = recruiterUsername;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getRecruiterId() {
        return RecruiterId;
    }

    public void setRecruiterId(int RecruiterId) {
        this.RecruiterId = RecruiterId;
    }

    // Method to apply using the candidate name and recruiter username
//    public void apply() {
//        if (caname != null && recruiterUsername != null) {
//            System.out.println(caname + " has applied to " + recruiterUsername + "'s job posting.");
//        } else {
//            System.out.println("Application failed: Candidate name or Recruiter username is missing.");
//        }
   // }
}
