package com.example.examnet2.service;

import com.example.examnet2.Entity.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CommentService {

    @GET("/comments")
    public abstract Call<List<Comment>> listaComentarios();

}
