package com.example.acessolivre.combineclothes.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jandersonlemos on 16/11/17.
 */

public class Photo implements Serializable{
    private String id;
    private String urlImage;
    private List<Double> notas;
    private Date postDate;

    public Photo(){
        notas = new ArrayList<>();
    }
    public Photo(String id, String imageBase64, Date postDate) {
        this.notas = new ArrayList<>();
        this.id = id;
        this.urlImage = imageBase64;
        this.postDate = postDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    @Exclude
    public Double getNota(){
        Double nota = 0D;
        for(Double n : this.getNotas()){
            nota += n;
        }
        Double n = Math.floor(nota / this.getNotas().size());
        return Double.isNaN(n) ? 1.0D : n;
    }

    public void addNota(Double nota){
        this.notas.add(nota);
    }

    public List<Double> getNotas() {
        return notas;
    }

    @Exclude
    @Override
    public String toString() {
        return "Photo{" +
                "id='" + id + '\'' +
                ", urlImage='" + urlImage + '\'' +
                ", postDate=" + postDate +
                ", nota=" + getNota()+
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Photo)) return false;

        Photo photo = (Photo) o;

        return getId() != null ? getId().equals(photo.getId()) : photo.getId() == null;

    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
