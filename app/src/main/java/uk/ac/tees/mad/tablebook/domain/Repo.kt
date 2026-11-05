package uk.ac.tees.mad.tablebook.domain

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.tablebook.model.ResultState
import uk.ac.tees.mad.tablebook.model.UserData

interface Repo {
    fun registeruserwithemailandpassword(userdata: UserData): Flow<ResultState<String>>
    fun loginuserwithemailandpassword(userdata: UserData): Flow<ResultState<String>>
}