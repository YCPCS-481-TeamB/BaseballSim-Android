package kylemeyers22.heroku.apiObjects;

public class Approval {
    private int approvalId;
    private String status;
    private String date;
    private String approvalOwner;

    public Approval(int Id, String status, String date, String owner) {
        this.approvalId = Id;
        this.status = status;
        this.date = date;
        this.approvalOwner = owner;
    }

    public int getId() {
        return this.approvalId;
    }

    public String getStatus() {
        return this.status;
    }

    public String getDate() {
        return this.date;
    }

    public String getOwner() {
        return this.approvalOwner;
    }

    public void setStatus(String newStatus) {
        this.status = newStatus;
    }
}
