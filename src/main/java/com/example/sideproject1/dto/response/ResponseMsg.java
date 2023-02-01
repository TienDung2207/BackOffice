package com.example.sideproject1.dto.response;

import com.example.sideproject1.dto.RoleData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ResponseMsg {
    private String rspCode;

    private String rspMessage;

    private List<RoleData> data;

    public ResponseMsg(String rspCode, String rspMessage) {
        this.rspCode = rspCode;
        this.rspMessage = rspMessage;
    }

    public ResponseMsg(String rspCode, String rspMessage, List<RoleData> data) {
        this.rspCode = rspCode;
        this.rspMessage = rspMessage;
        this.data = data;
    }
}
