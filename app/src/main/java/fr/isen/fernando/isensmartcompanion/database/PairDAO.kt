package fr.isen.fernando.isensmartcompanion.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PairDAO {
    @Query("SELECT * FROM PairQuestionAnswer")
    suspend fun getAll(): List<PairQuestionAnswer>

    @Query("SELECT * FROM PairQuestionAnswer WHERE id IN (:pairIds)")
    suspend fun loadAllByIds(pairIds: IntArray): List<PairQuestionAnswer>

    @Query("SELECT * FROM PairQuestionAnswer WHERE question LIKE :question AND " +
           "answer LIKE :answer LIMIT 1")
    suspend fun findByQuestionAnswer(question: String, answer: String): PairQuestionAnswer

    @Query("SELECT * FROM PairQuestionAnswer ORDER BY date ASC")
    suspend fun getOrderedQuestions(): List<PairQuestionAnswer>

    @Insert
    suspend fun insertAll(vararg pairs: PairQuestionAnswer)

    @Insert
    suspend fun insert(pair: PairQuestionAnswer)

    @Delete
    suspend fun delete(pair: PairQuestionAnswer)

    @Delete
    suspend fun deleteAll(vararg pairs: PairQuestionAnswer)
}