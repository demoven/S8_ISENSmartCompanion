package fr.isen.fernando.isensmartcompanion.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PairDAO {
    @Query("SELECT * FROM PairQuestionAnswer")
    suspend fun getAll(): List<PairQuestionAnswer>

    @Query(
        "SELECT * FROM PairQuestionAnswer WHERE question LIKE :question AND " +
                "answer LIKE :answer LIMIT 1"
    )
    suspend fun findByQuestionAnswer(question: String, answer: String): PairQuestionAnswer

    @Query("DELETE FROM PairQuestionAnswer")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll(vararg pairs: PairQuestionAnswer)

    @Insert
    suspend fun insert(pair: PairQuestionAnswer)

    @Delete
    suspend fun delete(pair: PairQuestionAnswer)

}