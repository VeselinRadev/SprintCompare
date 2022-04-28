package com.veselin.sprintcompare.models;

import java.util.HashMap;
import java.util.List;

public class Group {
    private String name;
    private String id;
    private List<String> memberIDs;
    private List<Run> runs;

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    private String inviteCode;
    public Group(){

    }
    public Group(String name, String id) {
        this.name = name;
        this.id = id;
        this.inviteCode = getInviteCode(id);
    }

    private String getInviteCode(String id) {
        return id.substring(id.length()-5);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getMemberIDs() {
        return memberIDs;
    }

    public void setMemberIDs(List<String> memberIDs) {
        this.memberIDs = memberIDs;
    }

    public List<Run> getRuns() {
        return runs;
    }

    public void setRuns(List<Run> runs) {
        this.runs = runs;
    }
    public HashMap<String, String> getHashMap(){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", this.name);
        hashMap.put("id", this.id);
        hashMap.put("code", this.inviteCode);
        return hashMap;
    }
}
