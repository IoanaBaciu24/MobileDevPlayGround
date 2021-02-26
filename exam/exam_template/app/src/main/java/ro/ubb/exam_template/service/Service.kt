package ro.ubb.exam_june192020.service

import retrofit2.http.*
import ro.ubb.exam_template.domain.Entite

interface Service {

    @GET("/exams")
    suspend fun getData(): List<Entite>


    @GET("/exam/{id}")
    suspend fun getExamById(@Path("id") id: Int): Entite
    @GET("/draft")
    suspend fun getDraft():List<Entite>
    @GET("/group")
    suspend fun getGroup():List<Entite>

    @POST("/exam")
    suspend fun addData(@Body e: Entite): Entite
    @POST("/join")
    suspend fun joinExam(@Body e: Entite)
    @DELETE("/document/{id}")
    suspend fun deleteEntity(@Path("id") id: Int)

    companion object {
        const val SERVICE_ENDPOINT = "https://ac08586bc113.ngrok.io"
    }
}