public class Session3 {
    private static Session3 instance;
    private String recName;
    private String recEmail;
    private int status1;
    private int userId;

    // Private constructor to restrict instantiation
    private Session3() {
    }

    // Method to get the singleton instance
    public static Session3 getInstance() {
        if (instance == null) {
            instance = new Session3();
        }
        return instance;
    }

    // Getter and Setter methods for recName
    public String getRecName() {
        return recName;
    }

    public void setRecName(String recName) {
        this.recName = recName;
    }

    // Getter and Setter methods for recEmail
    public String getRecEmail() {
        return recEmail;
    }

    public void setRecEmail(String recEmail) {
        this.recEmail = recEmail;
    }

    // Getter and Setter methods for userId
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Getter and Setter methods for status1
    public int getStatus1() {
        return status1;
    }

    public void setStatus1(int status1) {
        this.status1 = status1;
    }
}
