package network.client.udp;

import OrganizationDatabase.NetworkCode;

public class ResultFrame {
    NetworkCode code = NetworkCode.FAILURE;
    Object object;

    public ResultFrame() {
    }

    public ResultFrame(NetworkCode code, Object object) {
        this.code = code;
        this.object = object;
    }

    public NetworkCode getCode() {
        return code;
    }

    public void setCode(NetworkCode code) {
        this.code = code;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
