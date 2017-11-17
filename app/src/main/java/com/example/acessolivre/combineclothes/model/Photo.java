package com.example.acessolivre.combineclothes.model;

import java.util.Date;

/**
 * Created by jandersonlemos on 16/11/17.
 */

public class Photo {
    private String id;
    private String imageBase64;
    private Long nota;
    private Date postDate;

    public Photo(){}
    public Photo(String id, String imageBase64, Long nota, Date postDate) {
        this.id = id;
        this.imageBase64 = imageBase64;
        this.nota = nota;
        this.postDate = postDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id='" + id + '\'' +
                ", imageBase64='" + imageBase64 + '\'' +
                ", postDate=" + postDate +
                '}';
    }

    public Long getNota() {
        return nota;
    }

    public void setNota(Long nota) {
        this.nota = nota;
    }
}
