package no.nav.helse

import com.google.gson.Gson
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
class jsonToModel {
    var gson = Gson()

    @Test
    fun lagJsonFraObjekt(){
        val jsonString = gson.toJson(TestModel(1,"Test"))
        assertEquals(jsonString, """{"id":1,"description":"Test"}""")
    }
    @Test
    fun lesJsonTilObjekt(){
        val jsonString = """{"id":1,"description":"Test"}"""
        val testModel = gson.fromJson(jsonString, TestModel::class.java)
        assertEquals(testModel.id, 1)
        assertEquals(testModel.description, "Test")
    }
}