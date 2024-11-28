public class CardRequest {
    public enum RequestType {
        BLOCK, UNBLOCK
    }

    private String accountId;
    private RequestType requestType;
    private String requestStatus; // Pending, Approved, Rejected

    public CardRequest(String accountId, RequestType requestType) {
        this.accountId = accountId;
        this.requestType = requestType;
        this.requestStatus = "Pending";
    }

    public String getAccountId() {
        return accountId;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    @Override
    public String toString() {
        return "CardRequest{" +
                "accountId='" + accountId + '\'' +
                ", requestType=" + requestType +
                ", requestStatus='" + requestStatus + '\'' +
                '}';
    }
}
