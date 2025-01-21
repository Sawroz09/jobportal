public class Session2 {
    private static Session2 instance;

    private String caname;
    private int userId;
    private String Qualification;
    private String Email;

    private Session2() {
        // Private constructor to restrict instantiation
    }

    public static Session2 getInstance() {
        if (instance == null) {
            instance = new Session2();
        }
        return instance;
    }


    public String getCanName() {
        return caname;
    }

    public void setCanName(String caname) {
        this.caname = caname;
    }



    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getQualification() {
        return Qualification;
    }

    public void setQualification(String Qualification) {
        this.Qualification = Qualification;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }


}
