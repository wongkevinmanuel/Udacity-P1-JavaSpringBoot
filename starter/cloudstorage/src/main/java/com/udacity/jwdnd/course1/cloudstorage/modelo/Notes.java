package com.udacity.jwdnd.course1.cloudstorage.modelo;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Notes implements Serializable {

    private Integer noteId;
    private String noteTitle;
    private String noteDescription;
    private Integer userId;

//    public Notes(){
//
//    }
//
//    public Notes(Integer noteId, String noteTitle
//            , String noteDescription, Integer userId) {
//        this.noteId = noteId;
//        this.noteTitle = noteTitle;
//        this.noteDescription = noteDescription;
//        this.userId = userId;
//    }
//
//    public Integer getNoteId() {
//        return noteId;
//    }
//
//    public void setNoteId(Integer noteId) {
//        this.noteId = noteId;
//    }
//
//    public String getNoteTitle() {
//        return noteTitle;
//    }
//
//    public void setNoteTitle(String noteTitle) {
//        this.noteTitle = noteTitle;
//    }
//
//    public String getNoteDescription() {
//        return noteDescription;
//    }
//
//    public void setNoteDescription(String noteDescription) {
//        this.noteDescription = noteDescription;
//    }
//
//    public Integer getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Integer userId) {
//        this.userId = userId;
//    }

//    public Notes(){}
}
